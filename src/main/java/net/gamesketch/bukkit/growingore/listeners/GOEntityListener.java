package net.gamesketch.bukkit.growingore.listeners;

import net.gamesketch.bukkit.growingore.Core;
import net.gamesketch.bukkit.growingore.methods.RegisteredOre;
import net.gamesketch.bukkit.growingore.methods.TempOre;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class GOEntityListener extends EntityListener {
	
	private Core plugin;

	public GOEntityListener(Core plugin) {
		super();
		this.plugin = plugin;
	}

	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled()) { return; }
		if (event.blockList().size() < 1) { return; }
		
		for (Block b : event.blockList()) {
			RegisteredOre reg = plugin.getRegisteredOre(b);
			TempOre temp = plugin.getTemporaryOre(b);
			if (reg != null) {
				reg.activateTimers();
			}
			if (temp != null) {
				temp.startTimers();
			}
		}
	}
	
}
