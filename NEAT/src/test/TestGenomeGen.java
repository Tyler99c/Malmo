package test;

import java.util.Random;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class TestGenomeGen {
	public static void main(String[] args) {
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

		
		Genome addNode = TestMutation.testAddNode(parent1);
		GenomePrinter.printGenome(addNode, "output/TestMutation.png");

		
		Genome changeWeight = TestMutation.testChangeWeight(addNode);
		GenomePrinter.printGenome(changeWeight, "output/TestWeight.png");
		
		
		Genome addConnection = TestMutation.testAddConnection(parent1);
	    GenomePrinter.printGenome(addConnection, "output/TestConn.png");
		}
	}
}
