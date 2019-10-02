package neatsorce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.msr.malmo.ByteVector;

import neatsorce.NodeGene.TYPE;

public class NodeGene {

	public enum TYPE {
		INPUT,
		HIDDEN,
		OUTPUT,
		;
	}
	
	public NodeGene(TYPE t, int i) {
		id = i;
		type = t;
		cons = new ArrayList<ConnectionGene>();
	}
	private TYPE type;
	private int id;
	private List<ConnectionGene> cons;


	public TYPE getType() {
		return type;
	}
	
	public int getId() {
		return id;
	}
	
	public NodeGene copy() {
		// TODO Auto-generated method stub
		return new NodeGene(type, id);
	}
	
	public float getSignal(ByteVector inputs, Map<Integer,ConnectionGene> connections, Map<Integer,NodeGene> nodes) {
		if(type == TYPE.INPUT) {
			return inputs.get(id);
		}
		float total = 0;
		ArrayList<Integer> availableNodes;
		for(ConnectionGene con : connections.values()) {
			if(con.getOutNode() == id) {
				if(con.getExpressed() == true) {
					total = total + con.sendThrough(inputs, connections, nodes);
				}
			}
		}
		return (float) (1/( 1 + Math.pow(Math.E,(-1*total))));
	}
	
	public float getSignal(ArrayList<Float> inputs, Map<Integer,ConnectionGene> connections, Map<Integer,NodeGene> nodes) {
		if(type == TYPE.INPUT) {
			return inputs.get(id);
		}
		float total = 0;
		ArrayList<Integer> availableNodes;
		for(ConnectionGene con : connections.values()) {
			if(con.getOutNode() == id) {
				if(con.getExpressed() == true) {
					total = total + con.sendThrough(inputs, connections, nodes);
				}
			}
		}
		return (float) (1/( 1 + Math.pow(Math.E,(-1*total))));
	}
	
	public float addCon() {
		
	}
}
