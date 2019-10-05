package neatsorce;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.microsoft.msr.malmo.ByteVector;

import neatsorce.NodeGene.TYPE;

public class Genome {
	private Map<Integer, ConnectionGene> connections;
	private Map<Integer, NodeGene> nodes;
	private InnovationGenerator innovation;
	private int rewards;
	
	public Genome() {
		nodes = new HashMap<Integer, NodeGene>();
		connections = new HashMap<Integer, ConnectionGene>();
		innovation  = new InnovationGenerator();
	}
	
	public Genome(InnovationGenerator i) {
		nodes = new HashMap<Integer, NodeGene>();
		connections = new HashMap<Integer, ConnectionGene>();
		innovation  = i;
	}
	
	public Genome(Genome g) {
		nodes = new HashMap<Integer, NodeGene>();
		connections = new HashMap<Integer, ConnectionGene>();
		
		for(Integer index : g.getNodeGenes().keySet()) {
			nodes.put(index,  new NodeGene(g.getNodeGenes().get(index)));
		}
		
		for(Integer index : g.getConnectionGenes().keySet()) {
			connections.put(index,  new ConnectionGene(g.getConnectionGenes().get(index)));
		}
	}
	
	
	public void addNodeGene(NodeGene gene) {
		nodes.put(gene.getId(),gene);
	}
	public void addConnectionGene(ConnectionGene gene) {
		connections.put(gene.getInnovation(),gene);
	}
	public Map<Integer, ConnectionGene> getConnectionGenes(){
		return connections;
	}
	public Map<Integer, NodeGene> getNodeGenes(){
		return nodes;
	}
	
	public void addConnectionMutationold(Random r, InnovationGenerator i) {
		//Finds a random node
		NodeGene node1 = nodes.get(r.nextInt(nodes.size()));
		NodeGene node2 = nodes.get(r.nextInt(nodes.size()));
		if(node1.getId() == node2.getId()) {
			return;
		}
		float weight = r.nextFloat()*2f-1f;
		boolean reversed = false;
		//Tests to see if the connection should be reversed, so node 1 is the output while node 2 is input
		if (node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT) {
			reversed = true;
		} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN) {
			reversed = true;
		} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT) {
			reversed = true;
		}
		
		boolean connectionExists = false;
		ConnectionGene conBetween = null;
		for (ConnectionGene con : connections.values()) {
			//If a connection where node1 is the innode and node2 is an outnode
			if (con.getInNode() == node1.getId() && con.getOutNode() == node2.getId()) {
				connectionExists = true;
				break;
			}
			//If a connection where node2 is the innode and node1 is 
			else if (con.getInNode() == node2.getId() && con.getOutNode() == node1.getId()) {
				connectionExists = true;
				break;
			}
		}
		if(connectionExists) {
			return;
		}
		ConnectionGene newCon =	new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight,true, i.getInnovation());
		connections.put(newCon.getInnovation(),newCon);
	}
	
	
	public void addConnectionMutation(Random r, InnovationGenerator i) {
		//Finds a random node
		Integer[] nodeInnovationNumbers = new Integer[nodes.keySet().size()];
		nodes.keySet().toArray(nodeInnovationNumbers);
		Integer keyNode1 = nodeInnovationNumbers[r.nextInt(nodeInnovationNumbers.length)];
		NodeGene node1 = nodes.get(keyNode1);
		
		//NodeGene node1 = nodes.get(r.nextInt(nodes.size()));
		Map<Integer, NodeGene> connectionNodes = new HashMap<Integer, NodeGene>();
		int increment = 0;
		//Runs the find all nodes not connected to it, if the selected node is of the Type INPUT
		if(node1.getType() == NodeGene.TYPE.INPUT) {
			for(NodeGene node2 : nodes.values()) {
				if (node1.getId() != node2.getId()) {
					if(node2.getType() != NodeGene.TYPE.INPUT) {
						boolean connection = false;
						for (ConnectionGene con : connections.values()) {
							//If a connection where node1 is the innode and node2 is an outnode
							if (con.getInNode() == node1.getId() || con.getOutNode() == node1.getId()) {
								if (con.getInNode() == node2.getId() || con.getOutNode() == node2.getId()) {
									connection = true;
								}
							}
						}
						//No node 1 and node 2 are not connected this goes off
						if(connection == false) {
							connectionNodes.put(increment, node2);
							increment++;
						}
					}
				}
			}
		}
		//Runs to find all the nodes not connected to it, if the selected nose is not of the Type INPU
		else {
			for(NodeGene node2 : nodes.values()) {
				if (node1.getId() != node2.getId()) {
					if(node2.getType() != NodeGene.TYPE.INPUT) {
							boolean connection = false;
							for (ConnectionGene con : connections.values()) {
							//If a connection where node1 is the innode and node2 is an outnode
							if (con.getInNode() == node1.getId() || con.getOutNode() == node1.getId()) {
								if (con.getInNode() == node2.getId() || con.getOutNode() == node2.getId()) {
									connection = true;
								}
							}
						}
							//No node 1 and node 2 are not connected this goes off
						if(connection == false) {
							connectionNodes.put(increment, node2);
							increment++;
						}
					}
				}
			}
		}
		//If this node is connected to all other nodes
		if (increment == 0) {
			//System.out.println("All nodes connected");
			return;
		}
		NodeGene node2 = connectionNodes.get(r.nextInt(connectionNodes.size()));
		float weight = r.nextFloat()*2f-1f;
		boolean reversed = false;
		//Tests to see if the connection should be reversed, so node 1 is the output while node 2 is input
		if (node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT) {
			reversed = true;
		} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN) {
			reversed = true;
		} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT) {
			reversed = true;
		}
		ConnectionGene newCon =	new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight,true, i.getInnovation());
		connections.put(newCon.getInnovation(),newCon);
	}
	
	/**
	 * Old version of add Connection Mutation to keep all old tests working
	 * @param r
	 */
	public void addConnectionMutation(Random r) {
		//Finds a random node
		NodeGene node1 = nodes.get(r.nextInt(nodes.size()));
		Map<Integer, NodeGene> connectionNodes = new HashMap<Integer, NodeGene>();
		int increment = 0;
		//Runs the find all nodes not connected to it, if the selected node is of the Type INPUT
		if(node1.getType() == NodeGene.TYPE.INPUT) {
			for(NodeGene node2 : nodes.values()) {
				if (node1.getId() != node2.getId()) {
					if(node2.getType() != NodeGene.TYPE.INPUT) {
						boolean connection = false;
						for (ConnectionGene con : connections.values()) {
							//If a connection where node1 is the innode and node2 is an outnode
							if (con.getInNode() == node1.getId() || con.getOutNode() == node1.getId()) {
								if (con.getInNode() == node2.getId() || con.getOutNode() == node2.getId()) {
									connection = true;
								}
							}
						}
						//No node 1 and node 2 are not connected this goes off
						if(connection == false) {
							connectionNodes.put(increment, node2);
							increment++;
						}
					}
				}
			}
		}
		//Runs to find all the nodes not connected to it, if the selected nose is not of the Type INPU
		else {
			for(NodeGene node2 : nodes.values()) {
				if (node1.getId() != node2.getId()) {
					if(node2.getType() != NodeGene.TYPE.INPUT) {
							boolean connection = false;
							for (ConnectionGene con : connections.values()) {
							//If a connection where node1 is the innode and node2 is an outnode
							if (con.getInNode() == node1.getId() || con.getOutNode() == node1.getId()) {
								if (con.getInNode() == node2.getId() || con.getOutNode() == node2.getId()) {
									connection = true;
								}
							}
						}
							//No node 1 and node 2 are not connected this goes off
						if(connection == false) {
							connectionNodes.put(increment, node2);
							increment++;
						}
					}
				}
			}
		}
		//If this node is connected to all other nodes
		if (increment == 0) {
			System.out.println("All nodes connected");
			addNodeMutation(r);
			return;
		}
		NodeGene node2 = connectionNodes.get(r.nextInt(connectionNodes.size()));
		float weight = r.nextFloat()*2f-1f;
		boolean reversed = false;
		//Tests to see if the connection should be reversed, so node 1 is the output while node 2 is input
		if (node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT) {
			reversed = true;
		} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN) {
			reversed = true;
		} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT) {
			reversed = true;
		}
		ConnectionGene newCon =	new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight,true, innovation.getInnovation());
		connections.put(newCon.getInnovation(),newCon);
	}
	/**
	 * an Old version of add node Muation using the global innovation
	 * @param r
	 */
	public void addNodeMutation (Random r) {
		//Gets a random connection between two nodes
		ConnectionGene con = (ConnectionGene) connections.values().toArray()[r.nextInt(connections.size())];		
		//Finds the starting node in the connection
		NodeGene inNode = nodes.get(con.getInNode());
		//Finds the ending node in the connection
		NodeGene outNode = nodes.get(con.getOutNode());
		
		//Disables the existsing connection
		con.disable();
		//Creates a new Hidden layer node
		NodeGene newNode = new NodeGene(TYPE.HIDDEN, nodes.size());
		//Connects it to the inNode
		ConnectionGene inToNew = new ConnectionGene(inNode.getId(), newNode.getId(), 1.0f ,true,innovation.getInnovation());
		//Creates a connection from the new node to the out node
		ConnectionGene newToOut = new ConnectionGene(newNode.getId(), outNode.getId(), con.getWeight(), true,innovation.getInnovation());
		
		nodes.put(newNode.getId(), newNode);
		connections.put(inToNew.getInnovation(),inToNew);
		connections.put(newToOut.getInnovation(),newToOut);
	}
	
	/**
	 * A new version of add node using the parmaterized innovation number
	 * @param r
	 * @param i
	 */
	public void addNodeMutation (Random r, InnovationGenerator conInn, InnovationGenerator nodeInn) {
		//Gets a random connection between two nodes
		ConnectionGene con = (ConnectionGene) connections.values().toArray()[r.nextInt(connections.size())];		
		//Finds the starting node in the connection
		if(con == null) {
			System.out.print("Yo");
		}
		NodeGene inNode = nodes.get(con.getInNode());
		//Finds the ending node in the connection
		NodeGene outNode = nodes.get(con.getOutNode());
		
		//Disables the existsing connection
		con.disable();
		//Creates a new Hidden layer node
		NodeGene newNode = new NodeGene(TYPE.HIDDEN, nodeInn.getInnovation());
		//Connects it to the inNode
		ConnectionGene inToNew = new ConnectionGene(inNode.getId(), newNode.getId(), 1.0f ,true,conInn.getInnovation());
		//Creates a connection from the new node to the out node
		ConnectionGene newToOut = new ConnectionGene(newNode.getId(), outNode.getId(), con.getWeight(), true,conInn.getInnovation());
		
		nodes.put(newNode.getId(), newNode);
		connections.put(inToNew.getInnovation(),inToNew);
		connections.put(newToOut.getInnovation(),newToOut);
	}
	
	/**
	 * Performs the change weight mutation where it changes a random connection's weight
	 * @param random
	 */
	public void changeWeight(Random random) {
		for(ConnectionGene con : connections.values()) {	
			if(random.nextFloat() < 0.9f) {
			//Gets a random connection between two nodes
			  con.setWeight(con.getWeight() * (random.nextFloat() * 4f-2f));
			}
			else {
				con.setWeight(random.nextFloat()*4f-2f);

			}
		}
			
	}
	
	public void changeWeight(Random random, ConnectionGene con) {
		con.setWeight(random.nextFloat());
	}
	/**
	 * Performs a crossover between two networks
	 * @param parent1
	 * @param parent2
	 * @param r
	 * @return
	 */
	public static Genome crossover(Genome parent1, Genome parent2, Random r) {
		Genome child = new Genome();
		
		for (NodeGene parent1Node : parent1.getNodeGenes().values()) {
			child.addNodeGene(parent1Node.copy());
		}
		
		for (ConnectionGene parent1Node: parent1.getConnectionGenes().values()) {
			if(parent2.getConnectionGenes().containsKey(parent1Node.getInnovation())) {
				//ConnectionGene childConGene = r.nextBoolean() ? parent1Node.copy() : parent2.getConnectionGenes().get(parent1Node.getInnovation()).copy();
				ConnectionGene childConGene = r.nextBoolean() ? new ConnectionGene(parent1Node) : new ConnectionGene(parent2.getConnectionGenes().get(parent1Node.getInnovation()));
				child.addConnectionGene(childConGene);
			}else {
				ConnectionGene childConGene = parent1Node.copy();
				child.addConnectionGene(childConGene);
			}
		}
		
		return child;
	}
	
	public Map<Integer, NodeGene> getOutputNodes(){
		Map<Integer, NodeGene> outputNodes = new HashMap<Integer, NodeGene>();
		int i = 0;
		for(NodeGene gen: nodes.values()) {
			if(gen.getType() == TYPE.OUTPUT) {
				i++;
				outputNodes.put(i,gen);
			}		
		}
		return outputNodes;
	}
	/**
	 * Runs the network to return an output
	 * @return
	 */
	public ArrayList<Float> runGenome(ArrayList<Float> inputs) {
		ArrayList<Float> outputs = new ArrayList();
		Map<Integer, NodeGene> outputNodes = getOutputNodes();
        for(NodeGene gen: outputNodes.values()) {
        	outputs.add(gen.getSignal(inputs, connections, nodes));
        }
		return outputs;
	}

	private static List<Integer> tempList1 = new ArrayList<Integer>();
	private static List<Integer> tempList2 = new ArrayList<Integer>();


	public static float compatibiltyDistance(Genome genome1, Genome genome2, float c1, float c2, float c3) {
		int excessGenes = countExcessGenes(genome1, genome2);
		int disjoint = countDisjointGenes(genome1, genome2);
		float avgWeightDiff = averageWeightDiff(genome1, genome2);
		//System.out.print(excessGenes + "\t");
		//System.out.print(disjoint + "\t");
		//System.out.println(avgWeightDiff * c3);
		return excessGenes * c1 + disjoint *c2 + avgWeightDiff * c3;
	}
	
	public static int countMatchingGenes(Genome genome1, Genome genome2) {
		int matchingGenes = 0;
		
		List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet(),tempList1);
		List<Integer> nodeKeys2 = asSortedList(genome2.getNodeGenes().keySet(),tempList2);
		
		int highestInnovation1 = nodeKeys1.get(nodeKeys1.size() - 1);
		int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
		int indices = Math.max(highestInnovation1 ,  highestInnovation2);
		
		for(int i = 0; i <= indices; i++) {
			NodeGene node1 = genome1.getNodeGenes().get(i);
			NodeGene node2 = genome2.getNodeGenes().get(i);
			if(node1 != null && node2 != null) {
				matchingGenes++;
			}
		}
		
		List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet(),tempList1);
		List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet(),tempList2);
		
		highestInnovation1 = conKeys1.get(conKeys1.size()-1);
		highestInnovation2 = conKeys2.get(conKeys2.size()-1);
		
		indices = Math.max(highestInnovation1,  highestInnovation2);
		for(int i = 0; i <= indices; i++) {
			ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
			ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
			if(connection1 != null && connection2 != null) {
				 matchingGenes++;
			}
		}
		return matchingGenes;
	}
	
	public static int countDisjointGenes(Genome genome1, Genome genome2) {
		int disjointGenes = 0;
		
		List<Integer> nodeKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tempList1);
		List<Integer> nodeKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tempList2);
		
		int highestInnovation1 = nodeKeys1.get(nodeKeys1.size() - 1);
		int highestInnovation2 = nodeKeys2.get(nodeKeys2.size() - 1);
		int indices = Math.max(highestInnovation1, highestInnovation2);
		
		for (int i = 0; i <= indices; i++) {
			NodeGene node1 = genome1.getNodeGenes().get(i);
			NodeGene node2 = genome2.getNodeGenes().get(i);
			if (node1 == null && highestInnovation1 > i && node2 != null) {
				disjointGenes++;
			}else if (node2 == null && highestInnovation2 > i && node1 != null) {
				disjointGenes++;
			}
		}
		
		List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet(),tempList1);
		List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet(),tempList2);
		
		highestInnovation1 = conKeys1.get(conKeys1.size()-1);
		highestInnovation2 = conKeys2.get(conKeys2.size()-1);
		
		indices = Math.max(highestInnovation1,  highestInnovation2);
		for(int i = 0; i <= indices; i++) {
			ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
			ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
			if(connection1 == null && highestInnovation1 > i && connection2 != null) {
				disjointGenes++;
			}else if (connection2 == null && highestInnovation2 > i && connection1 != null) {
				disjointGenes++;
			}
		}
		return disjointGenes;
	}
	
	public static int countExcessGenes(Genome genome1, Genome genome2) {
		int excessGenes = 0;
		
		List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet(), tempList1);
		List<Integer> nodeKeys2 = asSortedList(genome2.getNodeGenes().keySet(), tempList2);
		
		
		int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
		int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
		int indices = Math.max(highestInnovation1,  highestInnovation2);
		
		for(int i = 0; i <= indices; i++){
			NodeGene node1 = genome1.getNodeGenes().get(i);
			NodeGene node2 = genome2.getNodeGenes().get(i);
			if(node1 == null && highestInnovation1 < i && node2 != null) {
				excessGenes++;
			}else if(node2 == null && highestInnovation2 < i && node1 != null) {
				excessGenes++;
			}
		}
		
		List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tempList1);
		List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tempList2);
		
		highestInnovation1 = conKeys1.get(conKeys1.size()-1);
		highestInnovation2 = conKeys2.get(conKeys2.size()-1);
		
		indices = Math.max(highestInnovation1,  highestInnovation2);
		for(int i = 0; i <= indices; i++) {
			ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
			ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
			if(connection1 == null && highestInnovation1 < i && connection2 != null) {
				excessGenes++;
			} else if (connection2 == null && highestInnovation2 < i  && connection1 != null) {
				excessGenes++;
			}
		}
		return excessGenes;
	}
	
	public static float averageWeightDiff(Genome genome1, Genome genome2) {
		int matchingGenes = 0;
		float weightDiffrence = 0;
		
		List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet(),tempList1);
		List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tempList2);
		
		int highestInnovation1 = conKeys1.get(conKeys1.size()-1);
		int highestInnovation2 = conKeys2.get(conKeys2.size()-1);
		
		int indices = Math.max(highestInnovation1, highestInnovation2);
		for (int i = 0; i <= indices; i++) {
			ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
			ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
			if(connection1 != null && connection2 != null) {
				matchingGenes++;
				weightDiffrence += Math.abs(connection1.getWeight() - connection2.getWeight());
			}
		}
		
		return weightDiffrence/matchingGenes;
	}
	
	public static List<Integer >asSortedList(Collection<Integer> c, List<Integer> list){
		list.clear();
		list.addAll(c);
		java.util.Collections.sort(list);
		return list;
	}

	public void runGenome(ByteVector hi) {
		// TODO Auto-generated method stub
		
	}

}
	

