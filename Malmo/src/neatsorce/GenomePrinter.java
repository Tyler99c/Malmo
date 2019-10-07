package neatsorce;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import neatsorce.ConnectionGene;
import neatsorce.Genome;
import neatsorce.NodeGene;
import neatsorce.NodeGene.TYPE;

/**
 * Prints a Genome display
 * @author Hydrozoa on Youtube
 *
 */
public class GenomePrinter {

	public static void printGenome(Genome genome, String path) {
		Random r = new Random();
		HashMap<Integer, Point> nodeGenePositions = new HashMap<Integer, Point>();
		int nodeSize = 20;
		int connectionSizeBulb = 6;
		int imageSize = 512;
		
		BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, imageSize, imageSize);
		
		g.setColor(Color.RED);
		for (NodeGene gene : genome.getNodeGenes().values()) {
			if (gene.getType() == TYPE.INPUT) {
				g.setColor(Color.RED);
				float x = ((float)gene.getId()/((float)countNodesByType(genome, TYPE.INPUT)+1f)) * imageSize;
				float y = imageSize-nodeSize/2;
				g.fillOval((int)(x-nodeSize/2), (int)(y-nodeSize/2), nodeSize, nodeSize);
				nodeGenePositions.put(gene.getId(), new Point((int)x,(int)y));
			} else if (gene.getType() == TYPE.HIDDEN) {
				g.setColor(Color.BLUE);
				int x = r.nextInt(imageSize-nodeSize*2)+nodeSize;
				int y = r.nextInt(imageSize-nodeSize*3)+(int)(nodeSize*1.5f);
				g.fillOval((int)(x-nodeSize/2), (int)(y-nodeSize/2), nodeSize, nodeSize);
				nodeGenePositions.put(gene.getId(), new Point((int)x,(int)y));
			} else if (gene.getType() == TYPE.OUTPUT) {
				g.setColor(Color.GREEN);
				int x = r.nextInt(imageSize-nodeSize*2)+nodeSize;
				int y = nodeSize/2;
				g.fillOval((int)(x-nodeSize/2), (int)(y-nodeSize/2), nodeSize, nodeSize);
				nodeGenePositions.put(gene.getId(), new Point((int)x,(int)y));
			}
		}
		
		g.setColor(Color.BLACK);
		for (ConnectionGene gene : genome.getConnectionGenes().values()) {
			if (!gene.getExpressed()) {
				continue;
			}
			Point inNode = nodeGenePositions.get(gene.getInNode());
			Point outNode = nodeGenePositions.get(gene.getOutNode());
			
			Point lineVector = new Point((int)((outNode.x - inNode.x) * 0.95f), (int)((outNode.y - inNode.y) * 0.95f));
			
			g.drawLine(inNode.x, inNode.y, inNode.x+lineVector.x, inNode.y+lineVector.y);
			g.fillRect(inNode.x+lineVector.x-connectionSizeBulb/2, inNode.y+lineVector.y-connectionSizeBulb/2, connectionSizeBulb, connectionSizeBulb);
			g.drawString(""+gene.getWeight(), (int)(inNode.x+lineVector.x*0.25f+5), (int)(inNode.y+lineVector.y*0.25f));
		}
		
		g.setColor(Color.WHITE);
		for (NodeGene nodeGene : genome.getNodeGenes().values()) {
			Point p = nodeGenePositions.get(nodeGene.getId());
			g.drawString(""+nodeGene.getId(), p.x, p.y);
		}
		
		
		try {
			ImageIO.write(image, "PNG", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int countNodesByType(Genome genome, TYPE type) {
		int c = 0;
		for (NodeGene node : genome.getNodeGenes().values()) {
			if (node.getType() == type) {
				c++;
			}
		}
		return c;
	}
}