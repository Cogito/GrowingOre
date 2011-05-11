package net.gamesketch.bukkit.growingore.methods;

import net.gamesketch.bukkit.growingore.Core;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SelectedOre {
	Block original;
	Player selector;
	
	Material origmat;
	byte origbyte;
	
	public SelectedOre(Player p, Block b) {
		this.original = b;
		this.selector = p;
		origmat = b.getType();
		origbyte = b.getData();
		b.setType(Material.SANDSTONE);
	}
	public Block getBlock() { return original; }
	
	public void restore() {
		original.setType(origmat);
		original.setData(origbyte);
	}
	
	public void register(int itemid, int minutes) {
		this.restore();
		RegisteredOre ore = new RegisteredOre(original, itemid, minutes);
		Core.REGISTEREDORES.add(ore);
	}
	
}
