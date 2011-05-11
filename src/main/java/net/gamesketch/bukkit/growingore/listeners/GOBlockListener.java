package net.gamesketch.bukkit.growingore.listeners;

import net.gamesketch.bukkit.growingore.Core;
import net.gamesketch.bukkit.growingore.methods.RegisteredOre;
import net.gamesketch.bukkit.growingore.methods.TempOre;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class GOBlockListener extends BlockListener {
	private Core plugin;

	public GOBlockListener(Core plugin) {
		super();
		this.plugin = plugin;
	}

	public void onBlockBreak(BlockBreakEvent event) {
		if (plugin.getRegisteredOre(event.getBlock()) != null) {
			RegisteredOre ore = plugin.getRegisteredOre(event.getBlock());
			ore.activateTimers();
		}
		if (plugin.getTemporaryOre(event.getBlock()) != null) {
			TempOre ore = plugin.getTemporaryOre(event.getBlock());
			ore.startTimers();
		}
	}
}
