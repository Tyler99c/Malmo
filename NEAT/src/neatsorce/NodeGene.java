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
		notConnected = null;
	}
	private TYPE type;
	private int id;
	private ArrayList<Integer> notConnected;

	public void setNotConnected(ArrayList<Integer> notConnected) {
		this.notConnected = notConnected;
	}
	
	public ArrayList<Integer> getNotConnected(){
		return notConnected;
	}
	
	public void establishConnection(int number) {
		notConnected.remove(number);
	}
	
	public void addNotConnected(int number) {
		notConnected.add(number);
	}
	
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
