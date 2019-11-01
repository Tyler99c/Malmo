package neatsorce;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import jsontogenome.Converter;;

public abstract class AllGenomeHandler {

	private FitnessGeneomeComparator fitComp = new FitnessGeneomeComparator();
	private IntegerComparator intComp = new IntegerComparator();

	private float C1 = 1.0f;
	private float C2 = 1.0f;
	private float C3 = 0.4f;
	private float DT = 10.0f;
	private InnovationGenerator nodeInnovation;
	private InnovationGenerator connectionInnovation;
	private int populationSize;
	private int integerFittestGenome;
	//Like above by uses ints, first 
	private Map<Integer, Species> intSpeciesMap;
	private Map<Integer, Float> intScoreMap;
	private List<Species> species;
	private List<Integer> intNextGenGenomes;
	private Map<Integer,Float> scoreKeeper;
	private List<Float> adjustedScoreKeeper;
	private Random random = new Random();
	private float highestScore;
	private Genome fittestGenome;
	private float MUTATION_RATE = .05f;
	private float ADD_CONNECTION_RATE = .05f;
	private float ADD_NODE_RATE = .05f;
	private int generation;
	private int trial;
	private int Folder;

	public AllGenomeHandler(int populationSize, Genome startingGenome, InnovationGenerator nodeInnovation,
			InnovationGenerator connectionInnovation, float mutationRate, int trail) throws IOException, JSONException {
		this.populationSize = populationSize;
		this.nodeInnovation = nodeInnovation;
		this.connectionInnovation = connectionInnovation;
		// Adds all Genomes into the array list
		long startTime = System.nanoTime();
		if(mutationRate == .05f) {
			Folder = 5;
		}
		if(mutationRate == .15f) {
			Folder = 15;
		}
		if(mutationRate == .25f) {
			Folder = 25;
		}
		for (int i = 0; i < populationSize; i++) {
			System.out.println(i);
			JSONObject genomeObject = Converter.getConstructGenomeFile(new Genome(startingGenome), i);
			try(FileWriter file = new FileWriter("Networks/"+Folder+"%Networks"+trial+"/Genomes/Genomes" + i + ".json")){
			//try(FileWriter file = new FileWriter("Networks/5%Networks/Genomes/test" + i + ".json")){
			//try(FileWriter file = new FileWriter("test/liltest/Genomes/Genomes"+i+".json")){
				System.out.println("Networks/"+Folder+"%Networks" + trial +"/Genomes/Genomes" + i + ".json");
				file.write(genomeObject.toString());
				file.flush();
				file.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
			//Converter.constructGenomeFile(new Genome(startingGenome), i);
			
		}
		long stopTime = System.nanoTime();
		double timed = (stopTime - startTime)/1000000000;
		System.out.println("Took " + timed + "Secounds to make all genomes");
		//System.out.println("Creates an array list of Genomes");
		intNextGenGenomes = new ArrayList<Integer>();
		intSpeciesMap = new HashMap<Integer, Species>();
		intScoreMap = new HashMap<Integer, Float>();
		species = new ArrayList<Species>();
		scoreKeeper = new HashMap<Integer,Float>();
		adjustedScoreKeeper = new ArrayList<Float>();
		//System.out.println("Done making Evaluater");
		generation = 0;
		MUTATION_RATE = mutationRate;
		ADD_CONNECTION_RATE = mutationRate;
		ADD_NODE_RATE = mutationRate;
		this.trial = trial;
	}
	
	
	/**
	 * Runs a generation
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void evaluate() throws Exception {
		
		// Finds and resets all speices
		for (Species s : species) {
			s.reset(random);
		}
		intScoreMap.clear();
		intSpeciesMap.clear();
		intNextGenGenomes.clear();
		integerFittestGenome = 0;
		highestScore = 0.0f;
		scoreKeeper.clear();
		adjustedScoreKeeper.clear();

		//For each genome
		int q = 0;
		long startTime = System.nanoTime();
		for (int i = 0; i < populationSize; i++) {
			//Gen the genome in question
			FileReader fr = new FileReader("Networks/"+Folder+"%Networks"+trial+"/Genomes/Genomes"+i+".json");
			JSONObject jObj = new JSONObject(new JSONTokener(fr));
			fr.close();
			Genome gen = Converter.toGenome(jObj, i);
			
			//evaluates the genomes
			int id = jObj.getJSONObject("Genome").getInt("id");

			float score = evaluateGenome(gen, i);
				
			scoreKeeper.put(jObj.getJSONObject("Genome").getInt("id"),score);
			
			boolean foundSpecies = false;
			//For each species
			System.out.println("Finding a species for Genome: " + q++);
			for (Species s : species) {
				//System.out.println("Checking a species");
				//Find the number of the genome that's the rep
				int rep = s.rep;
				//Retreive the json file
				fr = new FileReader("Networks/"+Folder+"%Networks"+trial+"/Genomes/Genomes"+rep+".json");
				//Get an object from a file
				JSONObject speciesRep = new JSONObject(new JSONTokener(fr));
				//Create a genome from the file
				Genome genomeRep = Converter.toGenome(speciesRep, i);
				//Compare the genomes
				if (Genome.compatibiltyDistance(gen, genomeRep, C1, C2, C3) < DT) {
					s.members.put(jObj.getJSONObject("Genome").getInt("id"),0);
					//Add a species to the species map
					intSpeciesMap.put(jObj.getJSONObject("Genome").getInt("id"),s);
					foundSpecies = true;
					break;
				}
			}
			
			// If the genome doesn't have a speices that if fit in makes one
			if (foundSpecies == false) {
				//System.out.println("Creating a new species");
				Species newSpecies = new Species(jObj.getJSONObject("Genome").getInt("id"),0);
				species.add(newSpecies);
				intSpeciesMap.put(jObj.getJSONObject("Genome").getInt("id"), newSpecies);
			}
			
			// Remove species not used
			Iterator<Species> iter = species.iterator();
			while (iter.hasNext()) {
				Species s = iter.next();
				if (s.members.isEmpty()) {
					iter.remove();
				}
			}
		}	
		long stopTime = System.nanoTime();
		double timed = (stopTime - startTime) / 1000000000; 
		System.out.println("Took " + timed + "Secounds to evaluate and speciate");

		int cough = 0;
		for(Float f : scoreKeeper.values()) {
			float adjustedScore = f/intSpeciesMap.get(cough).members.size();
			Species s = intSpeciesMap.get(cough);
			s.addAdjustedFitness(adjustedScore);
			s.fitnessInt.add(new integerFitnessGenome(cough,adjustedScore));
			intScoreMap.put(cough, adjustedScore);
			if(f > highestScore) {
				highestScore = f;
				integerFittestGenome = cough;
			}
			cough++;
		}	
		

		if(1 == 1) {
		
			FileReader fr = new FileReader("Networks/"+Folder+"%Networks"+trial+"/Genomes/Genomes"+integerFittestGenome+".json");
		
			JSONObject jObj = new JSONObject(new JSONTokener(fr));
			fr.close();
			try(FileWriter file = new FileWriter("Networks/"+Folder+"%Networks"+trial+"/FittestGenomes/Generation" + generation + ".json")){
				file.write(jObj.toString());
				file.flush();
				file.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		//JSONObject jObj = new JSONObject(new JSONTokener(fr));
		

		int newGeneration = 0;
		// Put best genome from each species into next gen
		//System.out.println("Search all species");
		for (Species s : species) {
			Collections.sort(s.fitnessInt, intComp);
			Collections.reverse(s.fitnessInt);
			integerFitnessGenome fitIntSpecies = s.fitnessInt.get(0);
			//Grab the id of the fittest genomes and add it to the
			intNextGenGenomes.add(fitIntSpecies.id);
			
			System.out.println("Fittest ID: " + fitIntSpecies.id);
			
			FileReader fr = new FileReader("Networks/"+Folder+"%Networks"+trial+"/Genomes/Genomes"+newGeneration+".json");
			
			JSONObject jObj = new JSONObject(new JSONTokener(fr));
			fr.close();
			Genome g = Converter.toGenome(jObj, newGeneration);
			
			//write genome to file, wth a new id
			JSONObject genomeObject = Converter.getConstructGenomeFile(g, newGeneration);
			try(FileWriter file = new FileWriter("Networks/"+Folder+"%Networks"+trial+"/Genomes/NewGenomes" + newGeneration + ".json")){
				file.write(genomeObject.toString());
				file.flush();
				file.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
			newGeneration++;
		}

		// Breed the rest of the genomes
		//System.out.println(intNextGenGenomes.size() + " " + populationSize);
		startTime = System.nanoTime();
		while (newGeneration < populationSize) {
			System.out.println("Adding all genome: " + newGeneration + " to next gen");
			Species s = getRandomSpeciesBaisedAdjustedFitness(random);
			int idp1 = intGetRandomGenomeBiasedAdjustedFitness(s, random);
			int idp2 = intGetRandomGenomeBiasedAdjustedFitness(s, random);

			
			FileReader fr = new FileReader("Networks/"+Folder+"%Networks"+trial+"/Genomes/Genomes"+idp1+".json");
			JSONObject jObjp1 = new JSONObject(new JSONTokener(fr));
			
			fr = new FileReader("Networks/"+Folder+"%Networks"+trial+"/Genomes/Genomes"+idp2+".json");
			JSONObject jObjp2 = new JSONObject(new JSONTokener(fr));
			
			Genome p1 = Converter.toGenome(jObjp1, idp1);
			Genome p2 = Converter.toGenome(jObjp2, idp2);
			Genome child;
			fr.close();
			
			if (intScoreMap.get(idp1) >= intScoreMap.get(idp2)) {
				child = Genome.crossover(p1, p2, random);
			} else {
				child = Genome.crossover(p2, p1, random);
			}
			// Runs through each random chance to add a mutation
			if (random.nextFloat() < MUTATION_RATE) {
				child.changeWeight(random);
			}
			if (random.nextFloat() < ADD_CONNECTION_RATE) {
				child.addConnectionMutation(random, connectionInnovation);
			}
			if (random.nextFloat() < ADD_NODE_RATE) {
				child.addNodeMutation(random, connectionInnovation, nodeInnovation);
			}
			//nextGenGenomes.add(child);
			JSONObject genomeObject = Converter.getConstructGenomeFile(child, newGeneration);
			try(FileWriter file = new FileWriter("Networks/"+Folder+"%Networks"+trial+"/Genomes/NewGenomes" + newGeneration + ".json")){
				file.write(genomeObject.toString());
				file.flush();
				file.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
			newGeneration++;
			
		}
		stopTime = System.nanoTime();
		timed = (stopTime - startTime)/1000000000;
		System.out.println("Took " + timed + "Secounds to make the new generation");
		//System.out.println("Converting all genomes to next gen");
		for(int i = 0; i < populationSize; i++) {
			File f1 = new File("Networks/"+Folder+"%Networks"+trial+"/Genomes/Genomes" + i + ".json");
			f1.delete();
			File f2 = new File("Networks/"+Folder+"%Networks"+trial+"/Genomes/NewGenomes" + i + ".json");
			f2.renameTo(f1);
		}
		generation++;
	}
	
	
	public abstract float evaluateGenome(Genome genome, int reset) throws JSONException, FileNotFoundException, IOException, Exception;

	/**
	 * Selects a random genome from the species chosen, where genomes with a higher
	 * adjusted fitness have a higher chance of being selected
	 * 
	 * @param selectFrom
	 * @param random
	 * @return
	 */
	private int intGetRandomGenomeBiasedAdjustedFitness(Species selectFrom, Random random) {
		double completeWeight = 0.0;
		for (integerFitnessGenome fg : selectFrom.fitnessInt) {
			completeWeight += fg.fitness;
		}
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (integerFitnessGenome fg : selectFrom.fitnessInt) {
			countWeight += fg.fitness;
			if (countWeight >= r) {
				return fg.id;
			}
		}
		throw new RuntimeException("Couldn't find a genome... Number is genomes in selected species is ");
	}
	
	/**
	 * Selects a random species, species with higher total fitness have a better
	 * chance
	 * 
	 * @param random
	 * @return
	 */

	private Species getRandomSpeciesBaisedAdjustedFitness(Random random) {
		double completeWeight = 0.0;
		for (Species s : species) {
			completeWeight += s.totalAdjustedFitness;
		}
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (Species s : species) {
			countWeight += s.totalAdjustedFitness;
			if (countWeight >= r) {
				return s;
			}
		}
		throw new RuntimeException("Couldn't find a species... Number is species in total is " + species.size()
				+ ", and the toatl adjusted fitness is " + completeWeight);
	}

	/**
	 * gets the number of species in the generation
	 * 
	 * @return
	 */
	public int getSpeciesAmount() {
		return species.size();
	}

	/**
	 * Returns the highest fitness score
	 *
	 */
	public float getHighestFitness() {
		return highestScore;
	}

	/**
	 * Returns the fittestGenome;
	 *
	 */
	public int getFittestGenome() {
		return integerFittestGenome;
	}

	/**
	 * Creates a FitnessGenome which is a genome with a fitness score odls code
	 * 
	 * @author Hydroza
	 *
	 */
	public class FitnessGenome {
		float fitness;
		Genome genome;

		public FitnessGenome(Genome genome, float fitness) {
			this.genome = genome;
			this.fitness = fitness;
		}
	}
	
	/**
	 * Does what fitness Genome does, but with the id number
	 * @author engruser
	 *
	 */
	public class integerFitnessGenome{
		float fitness;
		int id;
		
		public integerFitnessGenome(int id, float fitness) {
			this.id = id;
			this.fitness = fitness;
		}
	}

	public class FitnessGeneomeComparator implements Comparator<FitnessGenome> {

		@Override
		public int compare(FitnessGenome one, FitnessGenome two) {
			if (one.fitness > two.fitness) {
				return 1;
			} else if (one.fitness < two.fitness) {
				return -1;
			}
			return 0;
		}
	}
	
	public class IntegerComparator implements Comparator<integerFitnessGenome> {

		@Override
		public int compare(integerFitnessGenome one, integerFitnessGenome two) {
			if (one.fitness > two.fitness) {
				return 1;
			} else if (one.fitness < two.fitness) {
				return -1;
			}
			return 0;
		}
	}


	/**
	 * THis class creates a species, with members, a mascot to represent it, a list
	 * of fintesses and and adjustment
	 * 
	 * @author Hydroza
	 *
	 */
	public class Species {

		public Genome mascot;
		public HashMap<Integer,Integer> members;
		public List<integerFitnessGenome> fitnessInt;
		public float totalAdjustedFitness = 0f;
		public int rep;

		
		public Species(int rep, int score) {
			this.rep = rep;
			this.members = new HashMap<Integer,Integer>();
			this.members.put(rep, score);
			this.fitnessInt = new ArrayList<integerFitnessGenome>();
		}

		public void addAdjustedFitness(float adjustedFitness) {
			this.totalAdjustedFitness += adjustedFitness;
		}

		public void reset(Random r) {
			int newMascotIndex = r.nextInt(members.size());
			members.clear();
			fitnessInt.clear();
			totalAdjustedFitness = 0f;
			rep = 0;
		}

	}
}
