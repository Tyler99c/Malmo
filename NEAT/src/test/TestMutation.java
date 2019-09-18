package test;

import java.util.Random;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class TestMutation {
	public static void main(String[] args) {
		
		GenomePrinter printer = new GenomePrinter();
		Genome parent1 = new Genome();
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
		GenomePrinter.printGenome(parent1, "output/premutation.png");
		parent1.addNodeMutation(new Random());

		
		GenomePrinter.printGenome(parent1, "output/postmutation.png");
		
		
	}
	public static Genome testMutation(Genome parent1) {
		parent1.addNodeMutation(new Random());
		return parent1;
	}
}
