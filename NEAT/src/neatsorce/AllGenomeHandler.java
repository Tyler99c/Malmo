package neatsorce;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AllGenomeHandler {
	private Map<Integer, Genome> population = new HashMap<Integer, Genome>();
	    
	
	
	public Map<Integer, Genome> cullWeakling()
	{
		
	}
	
	public Map<Integer, Genome> reorganize(){
		List<Genome> peopleByAge = new ArrayList<>(population.values());
		Collections.checkedSortedMap(population, Comparator.comparing(Genome.getRewards()));
	}
}
