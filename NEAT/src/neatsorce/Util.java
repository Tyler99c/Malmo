package neatsorce;

import java.awt.List;

public class Util {

		public static float compatibiltyDistance(Genome genome1, Genome genome2) {
			return 0f;
		}
		
		public static int countMatchingGenes(Genome genome1, Genome genome2) {
			int matchingGenes = 0;
			
			List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet());
			List<Integer> nodeKays2 = asSortedList(genome2.getNodeGenes().keySet());
			
			int highestInnovation1 = nodeKeys1.getBaseline(nodeKeys1.size() - 1);
			int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
			int indices - Math.max(highestInnovation1 ,  highestInnovation2);
			
			
		}
}
