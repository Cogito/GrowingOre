package net.gamesketch.bukkit.growingore.listeners;

import net.gamesketch.bukkit.growingore.Core;
import net.gamesketch.bukkit.growingore.methods.RegisteredOre;
import net.gamesketch.bukkit.growingore.methods.TempOre;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class GOBlockListener extends BlockListener {
	private Core plugin;

	public GOBlockListener(Core plugin) {
		super();
		this.plugin = plugin;
	}

	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (plugin.getRegisteredOre(block) != null) {
			RegisteredOre ore = plugin.getRegisteredOre(block);
			ore.activateTimers();
		}
		if (plugin.getTemporaryOre(block) != null) {
			TempOre ore = plugin.getTemporaryOre(block);
			ore.startTimers();
		}
	}
}
