package jsontogenome;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.InnovationGenerator;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

public class Converter {

	
	public static Genome toGenome(JSONObject Jobj, int size) throws JSONException {
		Genome gen = new Genome();
		//Add in Nodes
		JSONObject nodes = Jobj.getJSONObject("Genome").getJSONObject("nodes");
		Iterator<?> nd = nodes.keys();
		int g = 0;
		int limit = Jobj.getJSONObject("Genome").getJSONObject("nodes").getInt("ammount");
		while(g < limit) {
			JSONObject node = nodes.getJSONObject("node"+g);
			int id = node.getInt("id");
			TYPE t = null;
			if(node.getInt("TYPE") == 0) {
				t = TYPE.INPUT;
			}else if (node.getInt("TYPE") == 1) {
				t = TYPE.OUTPUT;
				//System.out.println("Found an output node");
			}else {
				t = TYPE.HIDDEN;
			}
			NodeGene gene = new NodeGene(t, id);
			gen.addNodeGene(gene);
			g++;
		}
		//System.out.println(g);
		JSONObject connections = Jobj.getJSONObject("Genome").getJSONObject("connections");
		g = 0;
		limit = Jobj.getJSONObject("Genome").getJSONObject("connections").getInt("ammount");
		while(g < limit) {
			JSONObject connection = connections.getJSONObject("connection"+g);
			int innode = connection.getInt("innode");
			int outnode = connection.getInt("outnode");
			double d = connection.getDouble("weight");
			float weight = (float) d;
			//System.out.println(weight);
			boolean expressed = connection.getBoolean("expressed");
			int innovation = connection.getInt("innovation");
			ConnectionGene connect = new ConnectionGene(innode,outnode,weight,expressed,innovation);
			gen.addConnectionGene(connect);
			g++;
		}
		//System.out.println("Made the Genome");
		return gen;
	}
	
	public static void constructGenomeFile(Genome gen, int i) throws JSONException {
		JSONObject genome = new JSONObject();
		//Do Connections
		JSONObject connections = new JSONObject();
		int g = 0;
		for(ConnectionGene con: gen.getConnectionGenes().values()) {
			JSONObject connection = new JSONObject();
			connection.put("innode", con.getInNode());
			connection.put("outnode", con.getOutNode());
			connection.put("weight", con.getWeight());
			connection.put("expressed", con.getExpressed());
			connection.put("innovation", con.getInnovation());
			connections.put("connection" + g, connection);
			g++;
		}
		genome.put("connections", connections);
		//Do Nodes
		g = 0;
		JSONObject nodes = new JSONObject();
		for(NodeGene node: gen.getNodeGenes().values()) {
			JSONObject ng = new JSONObject();
			ng.put("id", node.getId());
			if(node.getType() == TYPE.INPUT) {
				ng.put("TYPE", 0);
			}else if(node.getType() == TYPE.OUTPUT) {
				ng.put("TYPE", 1);
			}else {
				ng.put("TYPE", 2);
			}
			nodes.put("node"+ g, ng);
			//System.out.print("Node:" + g);
			g++;
		}
		genome.put("nodes", nodes);
		genome.put("id", i);
		genome.put("reward", 0);
		//Do connections
		JSONObject genomeObject = new JSONObject();
		genomeObject.put("Genome", genome);
		try(FileWriter file = new FileWriter("5%Networks/Genomes/Genomes" + i + ".json")){
			file.write(genomeObject.toString());
			file.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getConstructGenomeFile(Genome gen, int i) throws JSONException {
		JSONObject genome = new JSONObject();
		//Do Connections
		JSONObject connections = new JSONObject();
		int g = 0;
		for(ConnectionGene con: gen.getConnectionGenes().values()) {
			JSONObject connection = new JSONObject();
			connection.put("innode", con.getInNode());
			connection.put("outnode", con.getOutNode());
			connection.put("weight", con.getWeight());
			connection.put("expressed", con.getExpressed());
			connection.put("innovation", con.getInnovation());
			connections.put("connection" + g, connection);
			//System.out.print("Connection:" +g);
			g++;
		}
		connections.put("ammount", g);
		genome.put("connections", connections);
		//Do Nodes
		g = 0;
		JSONObject nodes = new JSONObject();
		for(NodeGene node: gen.getNodeGenes().values()) {
			JSONObject ng = new JSONObject();
			ng.put("id", node.getId());
			if(node.getType() == TYPE.INPUT) {
				ng.put("TYPE", 0);
			}else if(node.getType() == TYPE.OUTPUT) {
				ng.put("TYPE", 1);
				//System.out.println("Added and output node");
			}else {
				ng.put("TYPE", 2);
			}
			nodes.put("node"+ g, ng);
			//System.out.print("Node:" + g);
			g++;
		}
		nodes.put("ammount", g);
		//System.out.println(g);
		//System.out.println();
		genome.put("nodes", nodes);
		genome.put("id", i);
		genome.put("reward", 0);
		//Do connections
		JSONObject genomeObject = new JSONObject();
		genomeObject.put("Genome", genome);
		//System.out.println("Genome made");
		return genomeObject;
	}
}
