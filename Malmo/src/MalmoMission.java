// --------------------------------------------------------------------------------------------------
//  Copyright (c) 2016 Microsoft Corporation
//  
//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
//  associated documentation files (the "Software"), to deal in the Software without restriction,
//  including without limitation the rights to use, copy, modify, merge, publish, distribute,
//  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//  
//  The above copyright notice and this permission notice shall be included in all copies or
//  substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
//  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
//  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// --------------------------------------------------------------------------------------------------

// To compile:  javac -cp MalmoJavaJar.jar JavaExamples_run_mission.java
// To run:      java -cp MalmoJavaJar.jar:. JavaExamples_run_mission  (on Linux)
//              java -cp MalmoJavaJar.jar;. JavaExamples_run_mission  (on Windows)

// To run from the jar file without compiling:   java -cp MalmoJavaJar.jar:JavaExamples_run_mission.jar -Djava.library.path=. JavaExamples_run_mission (on Linux)
//                                               java -cp MalmoJavaJar.jar;JavaExamples_run_mission.jar -Djava.library.path=. JavaExamples_run_mission (on Windows)
//Comment
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.microsoft.msr.malmo.*;

import neatsorce.Genome;
import neatsorce.MyNeuralNetwork;

public class MalmoMission {
	private MyNeuralNetwork nn;
	private int hi;

	public MalmoMission(MyNeuralNetwork nn) {
		this.nn = nn;
	}
	public static final String WORLD = "default_flat_1.xml";
	static {
		System.loadLibrary("MalmoJava"); // attempts to load MalmoJava.dll (on Windows) or libMalmoJava.so (on Linux)
	}
	
	public static String loadXML() throws FileNotFoundException
	{
		StringBuffer data = new StringBuffer("");
		Scanner scan = new Scanner(new File(WORLD));
		while(scan.hasNextLine()) {
			data.append(scan.nextLine()+"\n");
		}
		return data.toString();
	}

	public double runMission() throws Exception {
		AgentHost agent_host = new AgentHost();
		if (agent_host.receivedArgument("help")) {
			System.out.println(agent_host.getUsage());
			System.exit(0);
		}
		String xml = loadXML();
		MissionSpec my_mission = new MissionSpec(xml,true);
		System.out.println("Checkpoint 1");
		//my_mission.forceWorldReset();
		my_mission.timeLimitInSeconds(10);
		//my_mission.requestVideo(320, 240);
		my_mission.requestVideo(240, 180);
		my_mission.rewardForReachingPosition(19.5f, 0.0f, 19.5f, 100.0f, 1.1f);

		MissionRecordSpec my_mission_record = new MissionRecordSpec("./saved_data.tgz");
		my_mission_record.recordCommands();
		//my_mission_record.recordMP4(20, 400000);
		my_mission_record.recordRewards();
		my_mission_record.recordObservations();

		try {
			agent_host.startMission(my_mission, my_mission_record);
		} catch (MissionException e) {
			System.err.println("Error starting mission: " + e.getMessage());
			System.err.println("Error code: " + e.getMissionErrorCode());
			// We can use the code to do specific error handling, eg:
			if (e.getMissionErrorCode() == MissionException.MissionErrorCode.MISSION_INSUFFICIENT_CLIENTS_AVAILABLE) {
				// Caused by lack of available Minecraft clients.
				System.err.println("Is there a Minecraft client running?");
			}
			System.exit(1);
		}

		WorldState world_state;
		double d = 0.0;
		double life = 20.0;
		boolean alive = false;
		int timesObserved = 0;
		int missedObservations = 0;
		String turn = null;
		int time = 0;
		boolean goalReached = false;
		long endTime = 0;
		double totalTime = 1500;
		long startTime = System.nanoTime();
		
		//System.out.print("Waiting for the mission to start");
		do {
			//System.out.print(".");
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				System.err.println("User interrupted while waiting for mission to start.");
				return 0.0;
			}
			world_state = agent_host.getWorldState();
			for (int i = 0; i < world_state.getErrors().size(); i++)
				System.err.println("Error: " + world_state.getErrors().get(i).getText());
		} while (!world_state.getIsMissionRunning());
		//System.out.println("");

		agent_host.sendCommand("Jump 1");
		agent_host.sendCommand("Jump 0");
		// main loop:
		do {
			try {
				Thread.sleep(25);
			} catch (InterruptedException ex) {
				System.err.println("User interrupted while mission was running.");
				return 0.0;
			}
			world_state = agent_host.getWorldState();
			TimestampedVideoFrameVector troy = world_state.getVideoFrames();
			try {
				if (troy != null) {
					TimestampedVideoFrame bob = troy.get(0);
					ByteVector hi = bob.getPixels();
					List<Float> commands = nn.computeByte(hi);
					if (commands.get(0) > 0.5f) {
						agent_host.sendCommand("move 1");
					} else {
						agent_host.sendCommand("move 0");
					}
					double speed = commands.get(1) - 0.5;
					speed = speed * 2;
					turn = "turn " + speed;
					agent_host.sendCommand(turn);
					if (commands.get(2) > 0.5f) {
						agent_host.sendCommand("jump 1");
					} else {
						agent_host.sendCommand("jump 0");
					}

					for (int i = 0; i < world_state.getRewards().size(); i++) {
						TimestampedReward reward = world_state.getRewards().get(i);
						System.out.println("Summed reward: " + reward.getValue());
					}
					for (int i = 0; i < world_state.getErrors().size(); i++) {
						TimestampedString error = world_state.getErrors().get(i);
						System.err.println("Error: " + error.getText());
					}

					JSONObject root = new JSONObject(world_state.getObservations().get(0).getText());
					d = root.getDouble("XPos");
					if(d > 20) {
						goalReached = true;
						endTime = System.nanoTime();
					}else if(d > 12) {
						while(root.getDouble("Pitch") < 20) {
							agent_host.sendCommand("pitch -1");
						}
					}
					if(root.getDouble("Life") < life) {
						life = root.getDouble("Life");
					}
					if(root.getBoolean("IsAlive") == false) {
						alive = false;
					}
					timesObserved++;
				}
			} catch (Exception ob) {
				missedObservations++;
				timesObserved++;
			}

		} while (world_state.getIsMissionRunning());

		//System.out.println("Mission has stopped.");
		if(goalReached == true) {
			totalTime = endTime - startTime;
			totalTime = (totalTime * 4)/1000000;
		}
		double percentage;
		percentage = missedObservations/timesObserved;
		System.out.println("Observations: " + timesObserved);
		System.out.println("Miss Chance:" + percentage);
		if(alive == false) {
			if(goalReached == true) {
				return d + (1500.0 -totalTime) - ((20.0 - life) * 100) + 2000;
			}
			return d + 500 - ((20.0 - life) * 100) + 2000;
		}
		if(goalReached == true) {
			return d + (1500.0 - totalTime) + 2000 - ((20.0-life) * 100) + 2000;
		}
		return d + 1500.0 - ((20.0 - life) * 100) + 2000;

	}

}
