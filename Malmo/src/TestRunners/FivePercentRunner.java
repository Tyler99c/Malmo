package TestRunners;

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

import neatsorce.AllGenomeHandler;
import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.GenomePrinter;
import neatsorce.InnovationGenerator;
import neatsorce.MyNeuralNetwork;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class FivePercentRunner {

	public static final String WORLD = "default_flat_1.xml";

	public static void main(String[] args) throws Exception {
		int j = 0;
		for (int testNumber = 0; testNumber < 10; testNumber++) {
			int genomeSize = 60 * 80 * 3;
			BufferedWriter out = null;
			List<Long> rewards = new ArrayList<Long>();
			InnovationGenerator connInnovation = new InnovationGenerator();
			InnovationGenerator nodeInnovation = new InnovationGenerator();
			Random r = new Random();
			GenomePrinter print = new GenomePrinter();
			Genome parent1 = new Genome();
			for (int i = 0; i < genomeSize; i++) {
				NodeGene node = new NodeGene(TYPE.INPUT, nodeInnovation.getInnovation());
				parent1.addNodeGene(node);
			}
			parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
			parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
			parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
			parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
			for (int y = 15; y < 45; y++) {
				for (int x = 20; x < 60; x++) {
					for (int pixel = 0; pixel < 3; pixel++) {
						parent1.addConnectionGene(new ConnectionGene(x * 3 + (y * 3 * 80) + pixel, genomeSize, 1.0f,
								true, connInnovation.getInnovation()));
						parent1.addConnectionGene(new ConnectionGene(x * 3 + (y * 3 * 80) + pixel, genomeSize + 1, 1.0f,
								true, connInnovation.getInnovation()));
						parent1.addConnectionGene(new ConnectionGene(x * 3 + (y * 3 * 80) + pixel, genomeSize + 2, 1.0f,
								true, connInnovation.getInnovation()));
						parent1.addConnectionGene(new ConnectionGene(x * 3 + (y * 3 * 80) + pixel, genomeSize + 3, 1.0f,
								true, connInnovation.getInnovation()));
					}
				}
			}
			long startTime = System.nanoTime();
			// MyNeuralNetwork n = new MyNeuralNetwork(parent1);
			MalmoMission min = new MalmoMission(0);
			AllGenomeHandler eval = new AllGenomeHandler(50, parent1, nodeInnovation, connInnovation, .05f, testNumber, 5) {
				@Override
				public float evaluateGenome(Genome genome, int reset) throws Exception {
					MyNeuralNetwork n = new MyNeuralNetwork(genome);
					min.setNetwork(n);
					float f = (float) min.runMission();

					System.out.println(" Score:" + f);
					long printable = (long) f;
					rewards.add(printable);
					return (f);
				}
			};
			for (int i = 0; i < 100; i++) {
				eval.evaluate();
				out = new BufferedWriter(new FileWriter("5%ResultsFolder/File" + j + ".txt"));
				j++;
				for (long l : rewards) {
					out.write(l + "\n");
				}
				System.out.print("Generation:" + i);
				System.out.print("\tHighest fitness: " + eval.getHighestFitness());
				long l = (long) eval.getHighestFitness();
				out.write("Highest Fitness: " + l + "\n");
				System.out.print("\tAmount of species: " + eval.getSpeciesAmount());
				System.out.print("\tConnections in best performer: " + eval.getFittestGenome());
				out.close();
			}
		}
		
		

	}
}
