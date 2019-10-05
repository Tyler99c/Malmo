import java.util.List;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import com.microsoft.msr.malmo.ByteVector;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.MyNeuralNetwork;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;
import neatsorce.GenomePrinter;

public class TestLargeNetworks {
	public static void main(String[] args) {
		InnovationGenerator innovation = new InnovationGenerator();
		Random r = new Random();
		GenomePrinter print = new GenomePrinter();
		Genome parent1 = new Genome(innovation);
		for(int i = 0; i < 230400; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, i);
			parent1.addNodeGene(node);
		}
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 230400));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 230401));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 230402));
		for(int i = 0; i < 230400; i++){
			parent1.addConnectionGene(new ConnectionGene(i,230400, r.nextFloat(),true,innovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,230401, r.nextFloat(),true,innovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,230402, r.nextFloat(),true,innovation.getInnovation()));
			System.out.println(i);
		}
		Toolkit.getDefaultToolkit().beep();

		long startTime = System.nanoTime();
		MyNeuralNetwork n = new MyNeuralNetwork(parent1);
		ArrayList<Float> inputs = new ArrayList();
		for(int i = 0; i < 230400; i++) {
			inputs.add(1.0f);
		}
		long endTime = System.nanoTime();
		double total = (endTime - startTime)/1000000;
		System.out.println("Total time to construct a network" + total);
		System.out.println("Lets go");
		Toolkit.getDefaultToolkit().beep();
		startTime = System.nanoTime();
		List<Float> outputs = n.compute(inputs);
		endTime = System.nanoTime();
		total = (endTime - startTime)/ 1000000;
		System.out.println("Total Time for first loop: " + total);
		System.out.println("Outputs : " + outputs.get(0) +" "+ outputs.get(1) +" "+ outputs.get(2));
		
		//GenomePrinter.printGenome(parent1, "output/parent1.png");
		//MalmoMission min = new MalmoMission(parent1);
		//min.runMission();
	}
}
