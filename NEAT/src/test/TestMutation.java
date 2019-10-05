package test;

import java.util.Random;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class TestMutation {
	public static Genome testAddNode(Genome parent1) {
		parent1.addNodeMutation(new Random());
		return parent1;
	}
	public static Genome testAddNode(Genome parent1,InnovationGenerator i) {
		parent1.addNodeMutation(new Random(),i);
		return parent1;
	}
	public static Genome testAddNodeWithMutationCalls(Genome parent1) {
		return parent1;
	}
	
	public static Genome testChangeWeight(Genome parent) {
		parent.changeWeight(new Random());
		return parent;
	}

	public static Genome testAddConnection(Genome parent,InnovationGenerator i) {
		parent.addConnectionMutation(new Random(),i);
		return parent;
	}
}
