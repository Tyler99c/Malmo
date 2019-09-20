package neatsorce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import neatsorce.NodeGene.TYPE;


public class Mutations {
	
	public void addConnectionMutationTest(Random r) {
		//Finds a random node
		NodeGene node1 = nodes.get(r.nextInt(nodes.size()));
		Map<Integer, ConnectionGene> AllConnections = new HashMap<Integer, ConnectionGene>();
		int track = 0;
		for(ConnectionGene con: connections.values()) {
			if(con.getInNode() == node1.getId() || con.getOutNode() == node1.getId()) {
				AllConnections.put(track, con);
			}
		}
		if (AllConnections.size() == nodes.size()) {
			
		}
		//Finds another random node
		NodeGene node2 = nodes.get(r.nextInt(nodes.size()));
		int i = nodes.size();
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
		System.out.println("Connection between" + node1.getId() + "and" + node2.getId());
		ConnectionGene newCon =	new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight,true, innovation.getInnovation());
		connections.put(newCon.getInnovation(),newCon);
	}
	
	public void addConnectionMutation(Random r) {
		//Finds a random node
		int k = r.nextInt(nodes.size());
		System.out.println(k);
		NodeGene node1 = nodes.get(k);
		if(node1 == null) {
			System.out.println("REEEEEE");
		}
		//Finds another random node
		int q = r.nextInt(nodes.size());
		System.out.println(q);
		NodeGene node2 = nodes.get(q);
		if(node2 == null) {
			System.out.println("RAAAAAA");
		}
		int i = nodes.size();
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
			addConnectionMutation(r);
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
		connections.put(inToNew.getInnovation(),inToNew);
		connections.put(newToOut.getInnovation(),newToOut);
	}
}
