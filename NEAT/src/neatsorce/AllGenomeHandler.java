package neatsorce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public abstract class AllGenomeHandler {
	
	private FitnessGeneomeComparator fitComp = new FitnessGeneomeComparator();
	
	private float C1 = 1.0f;
	private float C2 = 1.0f;
	private float C3 = 0.4f;
	private float DT = 10.0f;
	private InnovationGenerator nodeInnovation;
	private InnovationGenerator connectionInnovation;
	private int populationSize;
	private List<Genome> genomes;
	private Map<Genome, Species> speciesMap;
	private Map<Genome, Float> scoreMap;
	private List<Species> species;
	private List<Genome> nextGenGenomes;
	private Random random;
	private float highestScore;
	private Genome fittestGenome;
	
	private float MUTATION_RATE = .5f;
	private float ADD_CONNECTION_RATE = .1f;
	private float ADD_NODE_RATE = .01f;
	

	public AllGenomeHandler(int populationSize, Genome startingGenome, InnovationGenerator nodeInnovation, InnovationGenerator connectionInnovation) {
		this.populationSize = populationSize;
		this.nodeInnovation = nodeInnovation;
		this.connectionInnovation = connectionInnovation;
		//Adds all Genomes into the array list
		genomes = new ArrayList<Genome>(populationSize);
		for(int i = 0; i < populationSize; i++) {
			genomes.add(new Genome(startingGenome));
		}
		nextGenGenomes = new ArrayList<Genome>(populationSize);
		
	}
	
	/**
	 * Runs a generation
	 */
	public void evaluate() {
		
		//Finds and resets all speices
		for(Species s : species) {
			s.reset(random);
		}
		scoreMap.clear();
		speciesMap.clear();
		nextGenGenomes.clear();
		
		//Places genomes into species
		for (Genome gen : genomes) {
			boolean foundSpecies = false;
			for(Species s : species) {
				if (Genome.compatibiltyDistance(gen, s.mascot, C1, C2, C3) < DT) {
					s.members.add(gen);
					speciesMap.put(gen,s);
					foundSpecies = true;
					break;
				}
			}
			//If the genome doesn't have a speices that if fit in makes one
			if(foundSpecies == false) {
				Species newSpecies = new Species(gen);
				species.add(newSpecies);
				speciesMap.put(gen,newSpecies);
			}
		}
		
		//Remove species not used
		Iterator<Species> iter = species.iterator();
		while(iter.hasNext()) {
			Species s = iter.next();
			if(s.members.isEmpty()) {
				iter.remove();
			}
		}
		
		//Evaluates Genome and Assign fitness
		for (Genome g : genomes) {
			Species s = speciesMap.get(g);
			
			float score = evaluateGenome(g);
			float adjustedScore = score / speciesMap.get(g).members.size();
			
			s.addAdjustedFitness(adjustedScore);
			s.fitnessPop.add(new FitnessGenome(g, adjustedScore));
			scoreMap.put(g,  adjustedScore);
			if(score > highestScore) {
				highestScore = score;
				fittestGenome = g;
			}
			
		}
		
		//Put best genome from each species into next gen
		for(Species s : species) {
			Collections.sort(s.fitnessPop, fitComp);
			Collections.reverse(s.fitnessPop);
			FitnessGenome fittestInSpecies = s.fitnessPop.get(0);
			nextGenGenomes.add(fittestInSpecies.genome);
		}
	
	
		//Breed the rest of the genomes
		while (nextGenGenomes.size() < populationSize) {
			Species s = getRandomSpeciesBaisedAdjustedFitness(random);
			Genome p1 = getRandomGenomeBiasedAdjustedFitness(s, random);
			Genome p2 = getRandomGenomeBiasedAdjustedFitness(s, random);
					
			Genome child;
			if(scoreMap.get(p1) >= scoreMap.get(p2)) {
				child = Genome.crossover(p1, p2, random);
			}else {
				child = Genome.crossover(p2, p1, random);
			}
			//Runs through each random chance to add a mutation
			if(random.nextFloat() < MUTATION_RATE) {
				child.changeWeight(random);
			}
			if(random.nextFloat() < ADD_CONNECTION_RATE) {
				child.addConnectionMutation(random, connectionInnovation);
			}
			if(random.nextFloat() < ADD_NODE_RATE) {
				child.addConnectionMutation(random, nodeInnovation);
			}
			nextGenGenomes.add(child);
		}
		
		genomes = nextGenGenomes;
		nextGenGenomes = new ArrayList<Genome>();
	}
	
	public abstract float evaluateGenome(Genome genome);
	
	/**
	 * Selects a random genome from the species chosen, where genomes with a higher adjusted fitness have a higher chance of being selected
	 * @param selectFrom
	 * @param random
	 * @return
	 */
	private Genome getRandomGenomeBiasedAdjustedFitness(Species selectFrom, Random random) {
		double completeWeight = 0.0;
		for(FitnessGenome fg : selectFrom.fitnessPop) {
			completeWeight += fg.fitness;
		}
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for(FitnessGenome fg: selectFrom.fitnessPop) {
			countWeight += fg.fitness;
			if(countWeight >= r) {
				return fg.genome;
			}
		}
		throw new RuntimeException("Couldn't find a genome... Number is genomes in selected species is ");
	}
	
	/**
	 * Selects a random species, species with higher total fitness have a better chance
	 * @param random
	 * @return
	 */
	
	private Species getRandomSpeciesBaisedAdjustedFitness(Random random) {
		double completeWeight = 0.0;
		for(Species s : species) {
			completeWeight += s.totalAdjustedFitness;
		}
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (Species s : species) {
			countWeight += s.totalAdjustedFitness;
			if(countWeight >= r) {
				return s;
			}
		}
		throw new RuntimeException("Couldn't find a species... Number is species in total is ");
	}
	
	/**
	 * gets the number of species in the generation
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
	public Genome getFittestGenome() {
		return fittestGenome;
	}
	
	/**
	 * Creates a FitnessGenome 
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
	
	
	
	public class FitnessGeneomeComparator implements Comparator<FitnessGenome>{
		
		@Override
		public int compare(FitnessGenome one, FitnessGenome two) {
			if (one.fitness > two.fitness) {
				return 1;
			}
			else if(one.fitness < two.fitness){
				return -1;
			}
			return 0;
		}
	}
	
	
	/**
	 * THis class creates a species, with members, a mascot to represent it, a list of fintesses and and adjustment
	 * @author Hydroza
	 *
	 */
	public class Species {
		
		public Genome mascot;
		public List<Genome> members;
		public List<FitnessGenome> fitnessPop;
		public float totalAdjustedFitness = 0f;
		
		public Species(Genome mascot) {
			this.mascot = mascot;
			this.members = new LinkedList<Genome>();
			this.members.add(mascot);
			this.fitnessPop = new ArrayList<FitnessGenome>();
		}
		
		public void addAdjustedFitness(float adjustedFitness) {
			this.totalAdjustedFitness += adjustedFitness;
		}
		
		public void reset(Random r) {
			int newMascotIndex = r.nextInt(members.size());
			this.mascot = members.get(newMascotIndex);
			members.clear();
			fitnessPop.clear();
			totalAdjustedFitness = 0f;
		}
		

	}
}
