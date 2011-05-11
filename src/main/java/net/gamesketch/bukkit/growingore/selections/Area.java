package net.gamesketch.bukkit.growingore.selections;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Area {

	public static List<Block> getBlocks(Block reference, int radius, List<Material> ids) {
		if (radius >= 200) { radius = 200; }
		int min = 0 - radius;
		int max = radius;
		
		Block middle = reference.getWorld().getBlockAt(reference.getX(), 0, reference.getY());
		
		List<Block> result = new LinkedList<Block>();
		
		int a = min;
		int b = 128;
		int c = min;
		boolean needNewB = false;
		
		while (c <= max) {
			Block cur = middle.getRelative(a,b,c);
			if (cur != null) {
				if (ids.contains(cur.getType())) { 
					result.add(cur);
				}
			}
			b -= 1;
			if (b <= 0) { a += 1; needNewB = true; }
			if (a > max) { a = min; c += 1; }
			if (needNewB) { needNewB = false; b = middle.getWorld().getHighestBlockYAt(a,c); }
		}
		
		
		
		return result;
	}

	

	
}
