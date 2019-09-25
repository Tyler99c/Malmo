package neatsorce;

import java.util.ArrayList;

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
	
	public float sigmoid(float f) {
		return (float) (1/( 1 + Math.pow(Math.E,(-1*f))));
	}
}
