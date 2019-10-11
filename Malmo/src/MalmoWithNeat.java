import java.util.List;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;

import com.microsoft.msr.malmo.ByteVector;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.MyNeuralNetwork;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;
import neatsorce.GenomePrinter;

public class MalmoWithNeat {
	public static void main(String[] args) throws JSONException {
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
		MalmoMission min = new MalmoMission(n);
		double d = min.runMission();
		System.out.println(d);
	}
}
