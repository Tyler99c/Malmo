package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class TestGeneCounting {
	@Test
	void test() {
		GenomePrinter printer = new GenomePrinter();
		InnovationGenerator innovation = new InnovationGenerator();
		Genome parent1 = new Genome(innovation);
		for(int i = 0; i < 3; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, i);
			parent1.addNodeGene(node);
		}
		
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 3));
		parent1.addConnectionGene(new ConnectionGene(0,3,1.0f,true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(1,3,1.0f,true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(2,3,1.0f,true,innovation.getInnovation()));
		GenomePrinter.printGenome(parent1, "output/BasicNetwork.png");
		ArrayList<Float> inputs = new ArrayList();
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		ArrayList<Float> outputs = parent1.runGenome(inputs);
		assertEquals(outputs.get(0), 0.95257413f);
		
		parent1 = new Genome(innovation);
		for(int i = 0; i < 6; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, i);
			parent1.addNodeGene(node);
		}
		for(int i = 6; i < 8; i++){
			NodeGene node = new NodeGene(TYPE.HIDDEN, i);
			parent1.addNodeGene(node);
		}
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 8));
		parent1.addConnectionGene(new ConnectionGene(0,6,1.0f,true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(1,6,1.0f,true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(2,6,1.0f,true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(3,7,1.0f,true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(4,7,1.0f,true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(5,7,1.0f,true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(6,8,1.0f,true,innovation.getInnovation()));
		parent1.addConnectionGene(new ConnectionGene(7,8,1.0f,true,innovation.getInnovation()));
		GenomePrinter.printGenome(parent1, "output/BasicNetwork2.png");
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		outputs = parent1.runGenome(inputs);
		System.out.println("Hello");
		assertEquals(outputs.get(0),0.87047310f);
	}
}
