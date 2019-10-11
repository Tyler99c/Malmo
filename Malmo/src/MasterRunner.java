

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import neatsorce.AllGenomeHandler;
import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.GenomePrinter;
import neatsorce.InnovationGenerator;
import neatsorce.MyNeuralNetwork;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class MasterRunner {
	public static void main(String[] args) {
		InnovationGenerator innovation = new InnovationGenerator();
		Random r = new Random();
		GenomePrinter print = new GenomePrinter();
		Genome parent1 = new Genome(innovation);
		for(int i = 0; i < 230400; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, i);
			parent1.addNodeGene(node);
		}
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 230400));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 230401));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 230402));
		for(int i = 0; i < 230400; i++){
			parent1.addConnectionGene(new ConnectionGene(i,230400, r.nextFloat(),true,innovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,230401, r.nextFloat(),true,innovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,230402, r.nextFloat(),true,innovation.getInnovation()));
			System.out.println(i);
		}
		Toolkit.getDefaultToolkit().beep();

		long startTime = System.nanoTime();
		MyNeuralNetwork n = new MyNeuralNetwork(parent1);
		
		AllGenomeHandler eval = new AllGenomeHandler(100, genome, nodeInnovation, connectionInnovation) {
			@Override
			public float evaluateGenome(Genome genome) {
				float weightSum = 0f;
				for(ConnectionGene cg : genome.getConnectionGenes().values()) {
					if(cg.getExpressed()) {
						weightSum += Math.abs(cg.getWeight());
					}
				}
				float difference = Math.abs(weightSum-100f);
				return (1000f/difference);
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

