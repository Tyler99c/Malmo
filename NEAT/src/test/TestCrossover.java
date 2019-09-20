package test;

import java.util.Random;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class TestCrossover {
	public static void main(String[] args) {
		InnovationGenerator innovation = new InnovationGenerator();
		GenomePrinter printer = new GenomePrinter();
		Genome parent1 = new Genome(innovation);
		for(int i = 0; i < 3; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, i + 1);
			parent1.addNodeGene(node);
		}
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 4));
		parent1.addNodeGene(new NodeGene(TYPE.HIDDEN, 5));
		parent1.addConnectionGene(new ConnectionGene(1,4,1f,true,1));
		parent1.addConnectionGene(new ConnectionGene(2,4,1f,true,2));
		parent1.addConnectionGene(new ConnectionGene(3,4,1f,true,3));
		parent1.addConnectionGene(new ConnectionGene(2,5,1f,true,4));
		parent1.addConnectionGene(new ConnectionGene(5,4,1f,true,5));
		parent1.addConnectionGene(new ConnectionGene(1,5,1f,true,8));
	    innovation = new InnovationGenerator();
		Genome parent2 = new Genome(innovation);
		for (int i = 0; i < 3; i++)
		{
			NodeGene node = new NodeGene(TYPE.INPUT, i+1);
			parent2.addNodeGene(node);
		}
		parent2.addNodeGene(new NodeGene(TYPE.OUTPUT, 4));
		parent2.addNodeGene(new NodeGene(TYPE.HIDDEN, 5));
		parent2.addNodeGene(new NodeGene(TYPE.HIDDEN, 6));
		parent2.addConnectionGene(new ConnectionGene(1,4,1f,true,1));
		parent2.addConnectionGene(new ConnectionGene(2,4,1f,true,2));
		parent2.addConnectionGene(new ConnectionGene(3,4,1f,true,3));
		parent2.addConnectionGene(new ConnectionGene(2,5,1f,true,4));
		parent2.addConnectionGene(new ConnectionGene(5,4,1f,true,5));
		parent2.addConnectionGene(new ConnectionGene(5,6,1f,true,6));		
		parent2.addConnectionGene(new ConnectionGene(6,4,1f,true,7));
		parent2.addConnectionGene(new ConnectionGene(3,5,1f,true,9));
		parent2.addConnectionGene(new ConnectionGene(1,6,1f,true,10));	
		
		GenomePrinter.printGenome(parent1, "output/parent1.png");
		GenomePrinter.printGenome(parent2, "output/parent2.png");
		
		Genome child = Genome.crossover(parent2, parent1, new Random());
		GenomePrinter.printGenome(child, "output/child.png");
		
	}
	
}
