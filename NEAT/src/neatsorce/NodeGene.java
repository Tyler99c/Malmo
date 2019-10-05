package neatsorce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	}
	public NodeGene(NodeGene nodeGene) {
		id = nodeGene.getId();
		type = nodeGene.getType();
	}
	
	private TYPE type;
	private int id;


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
}
