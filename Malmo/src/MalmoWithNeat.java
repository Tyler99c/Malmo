import java.util.ArrayList;
import java.util.Random;

import com.microsoft.msr.malmo.ByteVector;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;
import neatsorce.GenomePrinter;

public class MalmoWithNeat {
	public static void main(String[] args) {
		InnovationGenerator innovation = new InnovationGenerator();
		Random r = new Random();
		GenomePrinter print = new GenomePrinter();
		Genome parent1 = new Genome(innovation);
		for(int i = 0; i < 57600; i++){
			NodeGene node = new NodeGene(TYPE.INPUT, i + 1);
			parent1.addNodeGene(node);
		}
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 57601));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 57602));
		parent1.addNodeGene(new NodeGene(TYPE.OUTPUT, 57603));
		for(int i = 0; i < 57600; i++){
			parent1.addConnectionGene(new ConnectionGene(i,57601, r.nextFloat(),true,innovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,57602, r.nextFloat(),true,innovation.getInnovation()));
			parent1.addConnectionGene(new ConnectionGene(i,57603, r.nextFloat(),true,innovation.getInnovation()));
			System.out.println(i);
		}
		//GenomePrinter.printGenome(parent1, "output/parent1.png");
		MalmoMission min = new MalmoMission(parent1);
		min.runMission();
	}
}
