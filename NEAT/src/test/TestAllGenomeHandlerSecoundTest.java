package test;

import neatsorce.AllGenomeHandler;
import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class TestAllGenomeHandlerSecoundTest {
	public static void main(String[] args) {
		InnovationGenerator nodeInnovation = new InnovationGenerator();
		InnovationGenerator connectionInnovation = new InnovationGenerator();
		
		Genome genome = new Genome();
		int n1 = nodeInnovation.getInnovation();
		int n2 = nodeInnovation.getInnovation();
		int n3 = nodeInnovation.getInnovation();
		int n4 = nodeInnovation.getInnovation();
		int n5 = nodeInnovation.getInnovation();
		int n6 = nodeInnovation.getInnovation();
		genome.addNodeGene(new NodeGene(TYPE.INPUT, n1));
		genome.addNodeGene(new NodeGene(TYPE.INPUT, n2));
		genome.addNodeGene(new NodeGene(TYPE.INPUT, n3));
		genome.addNodeGene(new NodeGene(TYPE.OUTPUT, n4));
		genome.addNodeGene(new NodeGene(TYPE.OUTPUT, n5));
		genome.addNodeGene(new NodeGene (TYPE.OUTPUT, n6));
		
		int c1 = connectionInnovation.getInnovation();
		int c2 = connectionInnovation.getInnovation();
		int c3 = connectionInnovation.getInnovation();
		int c4 = connectionInnovation.getInnovation();
		int c5 = connectionInnovation.getInnovation();
		int c6 = connectionInnovation.getInnovation();
		int c7 = connectionInnovation.getInnovation();
		int c8 = connectionInnovation.getInnovation();
		int c9 = connectionInnovation.getInnovation();
		genome.addConnectionGene(new ConnectionGene(n1, n4, 0.5f, true, c1));
		genome.addConnectionGene(new ConnectionGene(n2, n4, 0.5f, true, c2));
		genome.addConnectionGene(new ConnectionGene(n3, n4, 0.5f, true, c3));
		genome.addConnectionGene(new ConnectionGene(n1, n5, 0.5f, true, c4));
		genome.addConnectionGene(new ConnectionGene(n2, n5, 0.5f, true, c5));
		genome.addConnectionGene(new ConnectionGene(n3, n5, 0.5f, true, c6));
		genome.addConnectionGene(new ConnectionGene(n1, n6, 0.5f, true, c7));
		genome.addConnectionGene(new ConnectionGene(n2, n6, 0.5f, true, c8));
		genome.addConnectionGene(new ConnectionGene(n3, n6, 0.5f, true, c9));
		
		AllGenomeHandler eval = new AllGenomeHandler(100, genome, nodeInnovation, connectionInnovation) {
			@Override
			public float evaluateGenome(Genome genome) {
					return genome.getConnectionGenes().values().size();
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
