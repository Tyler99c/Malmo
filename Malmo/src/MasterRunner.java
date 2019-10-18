

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;

import neatsorce.AllGenomeHandler;
import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.GenomePrinter;
import neatsorce.InnovationGenerator;
import neatsorce.MyNeuralNetwork;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class MasterRunner {
	public static void main(String[] args) throws JSONException {
		InnovationGenerator connInnovation = new InnovationGenerator();
		InnovationGenerator nodeInnovation = new InnovationGenerator();
		Random r = new Random();
		GenomePrinter print = new GenomePrinter();
		Genome parent1 = new Genome();
		for(int i = 0; i < 19200; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, nodeInnovation.getInnovation());
			parent1.addNodeGene(node);
		}
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
		for(int i = 0; i < 19200; i++){
			parent1.addConnectionGene(new ConnectionGene(i,19200, r.nextFloat(),true,connInnovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,19201, r.nextFloat(),true,connInnovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,19202, r.nextFloat(),true,connInnovation.getInnovation()));
			//System.out.println(i);
		}
		Toolkit.getDefaultToolkit().beep();

		long startTime = System.nanoTime();
		//MyNeuralNetwork n = new MyNeuralNetwork(parent1);
		
		AllGenomeHandler eval = new AllGenomeHandler(100, parent1, nodeInnovation, connInnovation) {
			@Override
			public float evaluateGenome(Genome genome) throws JSONException {
				System.out.println("Running Test");
				 MyNeuralNetwork n = new MyNeuralNetwork(genome);
				 MalmoMission min = new MalmoMission(n);
				 return (float)(1000 - min.runMission());
			}
		};
		
		for(int i = 0; i < 100; i++) {
			eval.evaluate();
			System.out.print("Generation:" + i);
			System.out.print("\tHighest fitness: " + eval.getHighestFitness());
			System.out.print("\tAmount of species: " + eval.getSpeciesAmount());
			System.out.print("\tConnections in best performer: " + eval.getFittestGenome().getConnectionGenes().values().size());
			float weightSum = 0;
			for (ConnectionGene cg : eval.getFittestGenome().getConnectionGenes().values()) {
				if(cg.getExpressed()) {
					weightSum += Math.abs(cg.getWeight());
				}
			}
			System.out.println("\t Weight sum:" + weightSum);
			if (i % 10 == 0){
				GenomePrinter.printGenome(eval.getFittestGenome(), "output/connection_sum_100" + i +".png");
			}
		}
		
	}
}

