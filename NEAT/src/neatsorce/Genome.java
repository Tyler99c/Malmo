package neatsorce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		NodeGene node1 = nodes.get(r.nextInt(nodes.size()));
		NodeGene node2 = nodes.get(r.nextInt(nodes.size()));
		float weight = r.nextFloat()*2f-1f;
		boolean reversed = false;
		if (node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT) {
			reversed = true;
		} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN) {
			reversed = true;
		} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT) {
			reversed = true;
		}
		
		boolean connectionExists = false;
		for (ConnectionGene con : connections.values()) {
			if (con.getInNode() == node1.getId() && con.getOutNode() == node2.getId()) {
				connectionExists = true;
				break;
			}
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
		//Gets all the connections in the Genome
		ConnectionGene con = connections.get(r.nextInt(connections.size()));
		
		
		NodeGene inNode = nodes.get(con.getInNode());
		NodeGene outNode = nodes.get(con.getOutNode());
		
		con.disable();
		NodeGene newNode = new NodeGene(TYPE.HIDDEN, nodes.size()+1);
		ConnectionGene inToNew = new ConnectionGene(inNode.getId(), newNode.getId(), r.nextFloat() ,true,innovation.getInnovation());
		ConnectionGene newToOut = new ConnectionGene(newNode.getId(), outNode.getId(), con.getWeight(), true,innovation.getInnovation());
		
		nodes.put(newNode.getId(), newNode);
		connections.put(inToNew.getInnovation(),inToNew);
		connections.put(newToOut.getInnovation(),newToOut);
	}
	/**
	 * Performs a cossover between two networks
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
	

