package neatsorce;

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
}
