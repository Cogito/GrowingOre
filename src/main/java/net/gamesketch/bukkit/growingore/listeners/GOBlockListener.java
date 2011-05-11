package net.gamesketch.bukkit.growingore.listeners;

import net.gamesketch.bukkit.growingore.Core;
import net.gamesketch.bukkit.growingore.methods.RegisteredOre;
import net.gamesketch.bukkit.growingore.methods.TempOre;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class GOBlockListener extends BlockListener {
	public void onBlockBreak(BlockBreakEvent event) {
		if (Core.getRegisteredOre(event.getBlock()) != null) {
			RegisteredOre ore = Core.getRegisteredOre(event.getBlock());
			ore.activateTimers();
		}
		if (Core.getTemporaryOre(event.getBlock()) != null) {
			TempOre ore = Core.getTemporaryOre(event.getBlock());
			ore.startTimers();
		}
	}
}
