

import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONException;

import com.microsoft.msr.malmo.MissionSpec;

import TestRunners.MalmoMission;
import neatsorce.AllGenomeHandler;
import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.GenomePrinter;
import neatsorce.InnovationGenerator;
import neatsorce.MyNeuralNetwork;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class MasterRunner {
	
	private static String xml;
	public static final String WORLD = "default_flat_1.xml";

	public static void main(String[] args) throws Exception {
		int genomeSize = 60 * 80 * 3;
	    BufferedWriter out = new BufferedWriter(new FileWriter("Networks/5%Networks/File1.txt"));
	    List<Long> rewards = new ArrayList<Long>();
		InnovationGenerator connInnovation = new InnovationGenerator();
		InnovationGenerator nodeInnovation = new InnovationGenerator();
		Random r = new Random();
		GenomePrinter print = new GenomePrinter();
		Genome parent1 = new Genome();
		for(int i = 0; i < genomeSize; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, nodeInnovation.getInnovation());
			parent1.addNodeGene(node);
		}
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
		//Goes through half the the array 30 * 40
		//Skip the first 80 * 15 pixels 1200
		//Gid is 
		//Goes through each row, starting at row 3
		for(int y = 15; y < 45; y++) {
				for(int x = 20; x < 60; x++) {
					for(int pixel = 0; pixel < 3; pixel++) {
						parent1.addConnectionGene(new ConnectionGene(x*3+(y*3*80)+pixel,genomeSize, 1.0f,true,connInnovation.getInnovation()));
						parent1.addConnectionGene(new ConnectionGene(x*3+(y*3*80)+pixel,genomeSize + 1, 1.0f,true,connInnovation.getInnovation()));
						parent1.addConnectionGene(new ConnectionGene(x*3+(y*3*80)+pixel,genomeSize + 2, 1.0f,true,connInnovation.getInnovation()));
						parent1.addConnectionGene(new ConnectionGene(x*3+(y*3*80)+pixel,genomeSize + 3, 1.0f,true,connInnovation.getInnovation()));
					}
				}
		}
		//Skip the next 20 pixels, then capture the next 40, then skip 20, repeat 30 times
		//Skip the last 80 * 15 pixels
		//Goes through entire array 60 * 80
		/*for(int i = 0; i < genomeSize; i++){
			parent1.addConnectionGene(new ConnectionGene(i,genomeSize, 1.0f,true,connInnovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,genomeSize + 1, 1.0f,true,connInnovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,genomeSize + 2, 1.0f,true,connInnovation.getInnovation()));
			//System.out.println(i);
		}*/
		//xml = loadXML();
		//MissionSpec my_mission = new MissionSpec(xml, true);

		long startTime = System.nanoTime();
		//MyNeuralNetwork n = new MyNeuralNetwork(parent1);
		MalmoMission min = new MalmoMission(0);
		
		AllGenomeHandler eval = new AllGenomeHandler(50, parent1, nodeInnovation, connInnovation, .05f, 5) {
			@Override
			public float evaluateGenome(Genome genome, int reset) throws Exception {
				//System.out.println("Running Test");
				 MyNeuralNetwork n = new MyNeuralNetwork(genome);
				 //MalmoMission min = new MalmoMission(n, reset);
				 min.setNetwork(n);
				float f = (float)min.runMission();
				
				System.out.println(" Score:" + f);
				long printable = (long)f;
				rewards.add(printable);
				return (f);
			}
		};
		
		for(int i = 0; i < 100; i++) {
			//System.out.println("Hello");
			eval.evaluate();
			out = new BufferedWriter(new FileWriter("Networks/5%Networks/File" + i + ".txt"));
			for(long l : rewards) {
				out.write(l + "\n");
			}
			System.out.print("Generation:" + i);
			System.out.print("\tHighest fitness: " + eval.getHighestFitness());
			long l = (long) eval.getHighestFitness();
			out.write("Highest Fitness: "+ l + "\n");
			System.out.print("\tAmount of species: " + eval.getSpeciesAmount());
			System.out.print("\tConnections in best performer: " + eval.getFittestGenome());
			out.close();
		}
		
	}
	
	public static String loadXML() throws FileNotFoundException {
		StringBuffer data = new StringBuffer("");
		Scanner scan = new Scanner(new File(WORLD));
		while (scan.hasNextLine()) {
			data.append(scan.nextLine() + "\n");
		}
		return data.toString();
	}
}

