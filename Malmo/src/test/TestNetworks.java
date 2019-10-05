package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.GenomePrinter;
import neatsorce.InnovationGenerator;
import neatsorce.MyNeuralNetwork;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class TestNetworks {
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
		MyNeuralNetwork n = new MyNeuralNetwork(parent1);
		int j = n.getNeuron(4).getNode().getId();
		if(j == 4) {
			System.out.println("Good Job");
		}
		
		
		
	}
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
		MyNeuralNetwork n = new MyNeuralNetwork(parent1);
		ArrayList<Float> inputs = new ArrayList();
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		long startTime = System.nanoTime();
		List<Float> outputs = n.compute(inputs);
		long endTime = System.nanoTime();
		assertEquals(outputs.get(0), 0.95257413f);
		long duration = (endTime - startTime);
		double derp = duration/1000000;
		System.out.println(derp);
		
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
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		inputs.add(1.0f);
		n = new MyNeuralNetwork(parent1);
		outputs = n.compute(inputs);
		
		
		startTime = System.nanoTime();
		outputs = n.compute(inputs);
		endTime = System.nanoTime();
		assertEquals(outputs.get(0),0.87047310f);
		duration = (endTime - startTime);
		derp = duration/1000;
		System.out.println(derp + "Thousanth of a secound");
	}
}
