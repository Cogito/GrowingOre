package net.gamesketch.bukkit.growingore.methods;

import java.util.ArrayList;
import java.util.List;

import net.gamesketch.bukkit.growingore.Core;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerData {
	Player player;
	List<SelectedOre> selectedOres;
	
	public PlayerData(Player p) {
		this.player = p;
		this.selectedOres = new ArrayList<SelectedOre>();
	}
	
	
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isBlockSelected(Block b) {
		for (SelectedOre sel : selectedOres) {
			Block block = sel.getBlock();
			if (!block.getWorld().getName().equals(b.getWorld().getName())) { continue; }
			if (block.getX() != b.getX()) { continue; }
			if (block.getY() != b.getY()) { continue; }
			if (block.getZ() != b.getZ()) { continue; }
			return true;
		}
		return false;
	}
	
	public void addSelectedBlock(Block b) {
		selectedOres.add(new SelectedOre(this.player, b));
	}
	public void removeSelectedBlock(SelectedOre s) {
		if (selectedOres.contains(s)) { selectedOres.remove(s); }
	}
	public List<SelectedOre> getSelectedBlocks() {
		return selectedOres;
	}
	
	public void remove(Core plugin) {
		for (SelectedOre ore : selectedOres) {
			ore.restore();
			ore = null;
		}
		selectedOres.clear();
		plugin.getPlayerDataList().remove(this);
	}
	
}
