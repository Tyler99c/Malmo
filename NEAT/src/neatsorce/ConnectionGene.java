package neatsorce;

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
	
	public float adjustSignal(float f) {
		return f*weight;
	}
}
