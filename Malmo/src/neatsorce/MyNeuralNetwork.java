package neatsorce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.msr.malmo.ByteVector;

public class MyNeuralNetwork {
	private List<Integer> inputIds;
	private List<Integer> outputIds;

	private Map<Integer, Neuron> Neurons;
	
	//Find input nodes of Genome
	//Find output nodes of Genome
	public MyNeuralNetwork(Genome gen){
		int input = 0;
		int output = 0;
		Neurons = new HashMap<Integer, Neuron>();
		inputIds = new ArrayList<Integer>();
		outputIds = new ArrayList<Integer>();

		for(NodeGene node: gen.getNodeGenes().values()) {
			if(node.getType() == NodeGene.TYPE.INPUT) {
				inputIds.add(node.getId());
			}
			if(node.getType() == NodeGene.TYPE.OUTPUT) {
				outputIds.add(node.getId());
			}
			Neurons.put(node.getId(), new Neuron(node));
		}
		//Map all connections that connect to a neuron
			int j = 0;
			for(ConnectionGene con : gen.getConnectionGenes().values()) {
				int connected = con.getOutNode();
				Neurons.get(connected).addInputs(con.getInnovation(), con);
				System.out.println(j++);
			}
	}
	
	public Neuron getNeuron(int id) {
		return Neurons.get(id);
	}
	
	/**
	 * Test compute using floats
	 * @param inputs
	 * @return
	 */
	public List<Float> compute(List<Float> inputs) {
		
		ArrayList<Float> outputs = new ArrayList();
		//For every input nueron
        for(Integer outputNode: outputIds) {
        	outputs.add(Neurons.get(outputNode).sigmoid(Neurons, inputs));
        }
		return outputs;
		
	}
	
	/**
	 * Test compute using floats
	 * @param inputs
	 * @return
	 */
	public List<Float> computeByte(ByteVector inputs) {
		ByteVector i = null;
		ArrayList<Float> outputs = new ArrayList();
		//For every input nueron
        for(Integer outputNode: outputIds) {
        	outputs.add(Neurons.get(outputNode).sigmoidByte(Neurons, inputs));
        }
		return outputs;
		
	}
	
	//Create a Neuron out of every node in the genome

	//Run through a genome recursively to find every output
	
	
	
	public class Neuron {
		private Map<Integer, ConnectionGene>inputIds;
		private Map<Integer, ConnectionGene>outputIds;
		private NodeGene node;
		private NodeGene.TYPE type;
		
		public Neuron(NodeGene node) {
			this.setNode(node);
			inputIds = new HashMap<Integer, ConnectionGene>();
			outputIds = new HashMap<Integer, ConnectionGene>();
			type = node.getType();
		}
		
		
		//Adds all connections that this neuron is connected to
		public void addInputs(int innovation, ConnectionGene connection) {
			inputIds.put(innovation, connection);
		}
		
		public NodeGene getNode() {
			return node;
		}


		public void setNode(NodeGene node) {
			this.node = node;
		}
		
		public float multiplyByWeight(float value, int input) {
			return value * inputIds.get(input).getWeight();
		}
		
		public float sigmoid(Map<Integer, Neuron> neurons, List<Float> inputs) {
			//If it's input type return
			if(type == NodeGene.TYPE.INPUT) {
				return inputs.get(node.getId());
			}
			//If not for every connection to it mutlipley by the weight and ask for the node signal
			float total = 0.0f;
			for(ConnectionGene con : inputIds.values()) {
				//Find every neuron attached to this neural network
				total = total + con.getWeight() * neurons.get(con.getInNode()).sigmoid(neurons, inputs);
				//Ask for the signal from that network it returns
				//Multiply it by the wieght of the connection
			}
			//TImes the entire thing by the sigmoid fucntion
			return (float) (1/( 1 + Math.pow(Math.E,(-1*total))));
		}
		
		public Float sigmoidByte(Map<Integer, Neuron> neurons, ByteVector inputs) {
			//If it's input type return
			if(type == NodeGene.TYPE.INPUT) {
				return (float) inputs.get(node.getId());
			}
			//If not for every connection to it mutlipley by the weight and ask for the node signal
			float total = 0.0f;
			for(ConnectionGene con : inputIds.values()) {
				//Find every neuron attached to this neural network
				total = total + con.getWeight() * neurons.get(con.getInNode()).sigmoidByte(neurons, inputs);
				//Ask for the signal from that network it returns
				//Multiply it by the wieght of the connection
			}
			//TImes the entire thing by the sigmoid fucntion
			return (float) (1/( 1 + Math.pow(Math.E,(-1*total))));
		}
	}

}
