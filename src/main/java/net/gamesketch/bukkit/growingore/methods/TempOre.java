package net.gamesketch.bukkit.growingore.methods;

import java.util.Timer;
import java.util.TimerTask;

import net.gamesketch.bukkit.growingore.Core;

import org.bukkit.block.Block;

public class TempOre {
	Block block;
	
	Timer cobbleTimer = new Timer();
	Timer stoneTimer = new Timer();
	
	TempOre thisOre = this;
	
	TimerTask cobbleTask = new TimerTask() { public void run() {
		block.setTypeId(4);
		this.cancel();
	}};
	TimerTask stoneTask = new TimerTask() { public void run() {
		block.setTypeId(1);
		Core.TEMPORES.remove(thisOre);
		this.cancel();
	}};
	
	public TempOre(Block b) {
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
