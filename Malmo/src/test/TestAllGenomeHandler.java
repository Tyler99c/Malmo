package test;

import org.json.JSONException;

import neatsorce.AllGenomeHandler;
import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.GenomePrinter;
import neatsorce.InnovationGenerator;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class TestAllGenomeHandler {
	public static void main(String[] args) throws JSONException {
		InnovationGenerator nodeInnovation = new InnovationGenerator();
		InnovationGenerator connectionInnovation = new InnovationGenerator();
		
		Genome genome = new Genome();
		int n1 = nodeInnovation.getInnovation();
		int n2 = nodeInnovation.getInnovation();
		int n3 = nodeInnovation.getInnovation();
		genome.addNodeGene(new NodeGene(TYPE.INPUT, n1));
		genome.addNodeGene(new NodeGene(TYPE.INPUT, n2));
		genome.addNodeGene(new NodeGene(TYPE.OUTPUT, n3));
		
		int c1 = connectionInnovation.getInnovation();
		int c2 = connectionInnovation.getInnovation();
		genome.addConnectionGene(new ConnectionGene(n1, n3, 0.5f, true, c1));
		genome.addConnectionGene(new ConnectionGene(n2, n3, 0.5f, true, c2));
		
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
