package net.gamesketch.bukkit.growingore.listeners;

import net.gamesketch.bukkit.growingore.core;
import net.gamesketch.bukkit.growingore.methods.RegisteredOre;
import net.gamesketch.bukkit.growingore.methods.TempOre;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class block extends BlockListener {
	public void onBlockBreak(BlockBreakEvent event) {
		if (core.getRegisteredOre(event.getBlock()) != null) {
			RegisteredOre ore = core.getRegisteredOre(event.getBlock());
			ore.activateTimers();
		}
		if (core.getTemporaryOre(event.getBlock()) != null) {
			TempOre ore = core.getTemporaryOre(event.getBlock());
			ore.startTimers();
		}
	}
}
