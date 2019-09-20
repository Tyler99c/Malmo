package neatsorce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import neatsorce.NodeGene.TYPE;

public class Genome {
	private Map<Integer, ConnectionGene> connections;
	private Map<Integer, NodeGene> nodes;
	private InnovationGenerator innovation;
	
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
	
	public void addConnectionMutation(Random r) {
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
		ConnectionGene newCon =	new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight,true, innovation.getInnovation());
		connections.put(newCon.getInnovation(),newCon);
	}
	
	
	public void addConnectionMutationNew(Random r) {
		//Finds a random node
		NodeGene node1 = nodes.get(r.nextInt(nodes.size()));
		ArrayList<Integer> options = node1.getNotConnected();
		int hello = r.nextInt(options.size());
		NodeGene node2 = nodes.get(r.nextInt(hello));
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
		ConnectionGene newCon =	new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight,true, innovation.getInnovation());
		connections.put(newCon.getInnovation(),newCon);
	}
	/**
	 * Adds a node
	 * @param r
	 */
	public void addNodeMutation (Random r) {
		//Gets a random connection between two nodes
		ConnectionGene con = connections.get(r.nextInt(connections.size()));
		
		//Finds the starting node in the connection
		NodeGene inNode = nodes.get(con.getInNode());
		//Finds the ending node in the connection
		NodeGene outNode = nodes.get(con.getOutNode());
		
		//Disables the existsing connection
		con.disable();
		//Creates a new Hidden layer node
		NodeGene newNode = new NodeGene(TYPE.HIDDEN, nodes.size());
		//Connects it to the inNode
		ConnectionGene inToNew = new ConnectionGene(inNode.getId(), newNode.getId(), r.nextFloat() ,true,innovation.getInnovation());
		//Creates a connection from the new node to the out node
		ConnectionGene newToOut = new ConnectionGene(newNode.getId(), outNode.getId(), con.getWeight(), true,innovation.getInnovation());
		
		nodes.put(newNode.getId(), newNode);
		
		for(NodeGene gen : nodes.values()) {
			gen.addNotConnected(newNode.getId());
		}
		connections.put(inToNew.getInnovation(),inToNew);
		inNode.establishConnection(newNode.getId());
		connections.put(newToOut.getInnovation(),newToOut);
		outNode.establishConnection(newNode.getId());
	}
	
	/**
	 * Performs the change weight mutation where it changes a random connection's weight
	 * @param random
	 */
	public void changeWeight(Random random) {
		//Gets a random connection between two nodes
		ConnectionGene con = connections.get(random.nextInt(connections.size()));
		con.setWeight(random.nextFloat());
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
				ConnectionGene childConGene = r.nextBoolean() ? parent1Node.copy() : parent2.getConnectionGenes().get(parent1Node.getInnovation()).copy();
				child.addConnectionGene(childConGene);
			}else {
				ConnectionGene childConGene = parent1Node.copy();
				child.addConnectionGene(childConGene);
			}
		}
		
		return child;
	}

}
	

