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
	
	public static Genome testChangeWeight(Genome parent) {
		parent.changeWeight(new Random());
		return parent;
	}

	public static Genome testAddConnection(Genome parent) {
		parent.addConnectionMutation(new Random());
		return parent;
	}
}
