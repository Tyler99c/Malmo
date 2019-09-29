package neatsorce;

import java.awt.List;
import java.util.ArrayList;

public class Util {
	
		private static List<Integer> tempList1 = new ArrayList<Integer>();
		private static List<Integer> tempList2 = new ArrayList<Integer>();
	
	
		public static float compatibiltyDistance(Genome genome1, Genome genome2, int c1, int c2, int c3) {
			int excessGenes = countExcessGenes(genome1, genome2);
			int disjoint = countDisjointGenes(genome1, genome2);
			return excessGenes * c1 + disjoint *c2;
		}
		
		public static int countMatchingGenes(Genome genome1, Genome genome2) {
			int matchingGenes = 0;
			
			List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet());
			List<Integer> nodeKeys2 = asSortedList(genome2.getNodeGenes().keySet());
			
			int highestInnovation1 = nodeKeys1.getBaseline(nodeKeys1.size() - 1);
			int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
			int indices = Math.max(highestInnovation1 ,  highestInnovation2);
			
			for(int i = 0; i <= indices; i++) {
				NodeGene node1 = genome1.getNodeGenes().get(i);
				NodeGene node2 = genome2.getNodeGenes().get(i);
				if(node1 != null && node2 != null) {
					matchingGenes++;
				}
			}
			
			List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet());
			List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet());
			
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
			
			List<Integer> nodeKeys1 = asSortedList(genome1.getConnectionGenes().keySet());
			List<Integer> nodeKeys2 = asSortedList(genome2.getConnectionGenes().keySet());
			
			int highestInnovation1 = conKeys1.get(conKeys1.size() - 1);
			int highestInnovation2 = conKeys2.get(conKeys1.size() - 1);
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
			
			List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet());
			List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet());
			
			highestInnovation1 = conKeys1.get(conKeys1.size()-1);
			highestInnovation2 = conKeys2.get(conKeys2.size()-1);
			
			indices = Math.max(highestInnovation1,  highestInnnovation2);
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
			for(int i - 0; i <= indices; i++) {
				ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
				ConnectionGene connetcion2 = genome2.getConnectionGenes().get(i);
				if(connection1 == null && highestInnovation < i && connection2 != null) {
					excessGenes++;
				} else if (connetcion2 == null && highestInnovation2 < i ) {
					excessGenes++;
				}
			}
			return excessGenes;
		}
		
		public static float averageWeightDiff(Genome genome1, Genome genome2) {
			int matchingGenes = 0;
			float weightDiffrence = 0;
			
			List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet());
			List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet());
			
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
		
		public static List<Integer >asSortedList(Colllection<Integer> c, List<Integer> list){
			List<T> list = new ArrayList<T>(2);
			java.util.Collections.sort(list);
			return list;
		}
		
		
}
