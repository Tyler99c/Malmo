package test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
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
		for(int j = 0; j < 1; j++) {
		InnovationGenerator innovation = new InnovationGenerator();
		GenomePrinter printer = new GenomePrinter();
		Genome parent1 = new Genome(innovation);
		for(int i = 0; i < 3; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, i);
			parent1.addNodeGene(node);
		}
		Random r = new Random();
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 3));
		parent1.addConnectionGene(new ConnectionGene(0,3,r.nextFloat(),true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(1,3,r.nextFloat(),true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(2,3,r.nextFloat(),true,innovation.getInnovation()));
		GenomePrinter.printGenome(parent1, "output/TestNeuralNetwork.png");

		Map<Integer, NodeGene> test = parent1.getNodeGenes();
		//Tests to make sure all nodes are present
		assertEquals(test.get(0).getId(),0);
		assertEquals(test.get(1).getId(),1);
		assertEquals(test.get(2).getId(),2);
		assertEquals(test.get(3).getId(),3);
		
		//Make a random node and check the ID
		parent1 = TestMutation.testAddNode(parent1);
		GenomePrinter.printGenome(parent1, "output/TestMutation.png");
		test = parent1.getNodeGenes();
		Map<Integer, ConnectionGene> cons = parent1.getConnectionGenes();
		assertEquals(cons.get(3).getInNode(),3);
		assertNotEquals(test.get(4).);
		
		//make 3 random no
		parent1 = TestMutation.testAddNode(parent1);
		parent1 = TestMutation.testAddNode(parent1);
		parent1 = TestMutation.testAddNode(parent1);
		GenomePrinter.printGenome(parent1, "output/TestMutation.png");
		test = parent1.getNodeGenes();
		assertEquals(test.get(4).getId(),4);
		
		Genome changeWeight = TestMutation.testChangeWeight(addNode);
		GenomePrinter.printGenome(changeWeight, "output/TestWeight.png");
		}
	}
}
