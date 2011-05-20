package net.gamesketch.bukkit.growingore.methods;

import java.util.Timer;
import java.util.TimerTask;

import net.gamesketch.bukkit.growingore.Core;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class TempOre {
	Block block;

	Timer cobbleTimer = new Timer();
	Timer stoneTimer = new Timer();

	TempOre thisOre = this;

	protected Core plugin;

	TimerTask cobbleTask = new TimerTask() { public void run() {
		block.setType(Material.COBBLESTONE);//.setTypeId(4);
		this.cancel();
	}};

	TimerTask stoneTask = new TimerTask() { public void run() {
		block.setType(Material.STONE);//.setTypeId(1);
		plugin.getTempOres().remove(thisOre);
		this.cancel();
	}};
	
	public TempOre(Core plugin, Block b) {
		this.block = b;
	}
	public Block getBlock() {
		return block;
	}
	public void startTimers() {
		if (cobbleTask.scheduledExecutionTime() >= System.currentTimeMillis()) { return; }
		if (stoneTask.scheduledExecutionTime() >= System.currentTimeMillis()) { return; }
		cobbleTimer.schedule(cobbleTask, 5000);
		stoneTimer.schedule(stoneTask, 10000);
	}
	
}
