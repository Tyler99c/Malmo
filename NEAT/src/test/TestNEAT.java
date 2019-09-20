package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

class TestNEAT {

	@Test
	void test() {
		for(int j = 0; j < 1000; j++) {
		InnovationGenerator innovation = new InnovationGenerator(0);
		GenomePrinter printer = new GenomePrinter();
		Genome parent1 = new Genome(innovation);
		for(int i = 0; i < 3; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, i + 1);
			parent1.addNodeGene(node);
		}
		Random r = new Random();
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 4));
		parent1.addConnectionGene(new ConnectionGene(1,4,r.nextFloat(),true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(2,4,r.nextFloat(),true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(3,4,r.nextFloat(),true,innovation.getInnovation()));
		GenomePrinter.printGenome(parent1, "output/TestNeuralNetwork.png");

		
			Genome addNode = TestMutation.testAddNode(parent1);
			GenomePrinter.printGenome(addNode, "output/TestMutation.png");

		
		  Genome changeWeight = TestMutation.testChangeWeight(addNode);
		  GenomePrinter.printGenome(changeWeight, "output/TestWeight.png");
		}
	}
}
