package neatsorce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionGene {
	private int inNode;
	private int outNode;
	private float weight;
	private boolean expressed;
	private int innovation;
	
	public ConnectionGene(int in, int out, float wei, boolean exp, int inno){
		inNode = in;
		outNode = out;
		weight = wei;
		expressed = exp;
		innovation = inno;
	}
	public ConnectionGene(ConnectionGene connectionGene) {
		inNode = connectionGene.getInNode();
		outNode = connectionGene.getOutNode();
		weight = connectionGene.getWeight();
		expressed = connectionGene.getExpressed();
		innovation = connectionGene.getInnovation();
	}
	/**
	 * All the getters and setters for the variables above
	 * @return
	 */
	public int getInNode() {
		return inNode;
	}
	
	public void setInNode(int i) {
		inNode = i;
	}
	
	public int getOutNode() {
		return outNode;
	}
	
	public void setOutNode(int i) {
		outNode = i;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public void setWeight(float i) {
		weight = i;
	}
	
	public boolean getExpressed() {
		return expressed;
	}
	
	public void setExpressed(boolean i) {
		expressed = i;
	}
	
	public int getInnovation() {
		return innovation;
	}
	
	public void setInnovation(int i) {
		innovation = i;
	}
	public void disable() {
		expressed = false;
		
	}
	
	public ConnectionGene copy() {
		return new ConnectionGene(inNode, outNode, weight, expressed, innovation);
	}
	
	public float sendThrough(ArrayList<Float> inputs, Map<Integer,ConnectionGene> connections, Map<Integer,NodeGene> nodes) {
		return nodes.get(inNode).getSignal(inputs, connections, nodes) * weight;
	}
}
