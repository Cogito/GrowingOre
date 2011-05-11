package net.gamesketch.bukkit.growingore.methods;

import java.util.Timer;
import java.util.TimerTask;

import net.gamesketch.bukkit.growingore.Core;

import org.bukkit.block.Block;

public class RegisteredOre {
	Block b;
	int itemid;
	int minutes;
	
	Timer cobbleTimer;
	Timer stoneTimer;
	Timer oreTimer;
	
	TimerTask cobbleTask;
	TimerTask stoneTask;
	TimerTask oreTask;
	
	boolean firstTime = true;
	
	public RegisteredOre(Block b, int id, int min) {
		this.b = b;
		this.itemid = id;
		this.minutes = min;
		
		b.setTypeId(itemid);
	}
	public RegisteredOre(Block bl, int id, int min, long cobble, long stone, long ore) {
		this.b = bl;
		this.itemid = id;
		this.minutes = min;
		//Add timers (IF NECESERRY ,TO PREVENT RAM LEAK)
		if (cobble > 0) {
			cobbleTimer = new Timer();
			cobbleTask = new TimerTask() { public void run() {
				b.setTypeId(4);
				cobbleTask.cancel(); cobbleTimer.cancel();
				cobbleTask = null; cobbleTimer = null;
			}};
			cobbleTimer.schedule(cobbleTask, cobble); 
		}
		if (stone > 0) {
			stoneTimer = new Timer();
			stoneTask = new TimerTask() { public void run() {
				b.setTypeId(1);
				stoneTask.cancel(); stoneTimer.cancel();
				stoneTask = null; stoneTimer = null;
			}};
			stoneTimer.schedule(stoneTask, stone); 
		}
		if (ore > 0) {
			oreTimer = new Timer();
			oreTask = new TimerTask() { public void run() {
				b.setTypeId(itemid);
				Core.growNear(b, itemid, 1);
				oreTask.cancel(); oreTimer.cancel();
				oreTask = null; oreTimer = null;
			}};
			oreTimer.schedule(oreTask, ore); 
		}
	}
	
	public Block getBlock() {
		return b;
	}
	
	public void activateTimers() {
		//Cancel old timers & tasks
		if (cobbleTimer != null) { cobbleTimer.cancel(); cobbleTask.cancel(); }
		if (stoneTimer != null) { stoneTimer.cancel(); stoneTask.cancel(); }
		if (oreTimer != null) { oreTimer.cancel(); oreTask.cancel(); }
		//initalize new timers
		cobbleTimer = new Timer();
		stoneTimer = new Timer();
		oreTimer = new Timer();
		//initalize new tasks
		cobbleTask = new TimerTask() { public void run() {
			b.setTypeId(4);
			cobbleTask.cancel(); cobbleTimer.cancel();
			cobbleTask = null; cobbleTimer = null;
		}};
		
		stoneTask = new TimerTask() { public void run() {
			b.setTypeId(1);
			stoneTask.cancel(); stoneTimer.cancel();
			stoneTask = null; stoneTimer = null;
		}};
		
		oreTask = new TimerTask() { public void run() {
			b.setTypeId(itemid);
			Core.growNear(b, itemid, 15);
			oreTask.cancel(); oreTimer.cancel();
			oreTask = null; oreTimer = null;
		}};
		
		//activate new timers
		cobbleTimer.schedule(cobbleTask, 5000);
		stoneTimer.schedule(stoneTask, minutes*10000);
		oreTimer.schedule(oreTask, minutes*60000);
	}
	public void deactivateTimers() {
		//Cancel old timers & tasks
		if (cobbleTimer != null) { cobbleTimer.cancel(); cobbleTask.cancel(); cobbleTimer = null; cobbleTask = null; }
		if (stoneTimer != null) { stoneTimer.cancel(); stoneTask.cancel(); stoneTimer = null; stoneTask = null; }
		if (oreTimer != null) { oreTimer.cancel(); oreTask.cancel(); oreTimer = null; oreTask = null; }

	}
	public long getOreTimeLeft() {
		if (oreTask == null) { return 0; }
		long time = oreTask.scheduledExecutionTime() - System.currentTimeMillis();
		if (time <= 0) { time = 0; }
		if (oreTask.scheduledExecutionTime() == 0) { time = 0; }
		return time;
	}
	public long getStoneTimeLeft() {
		if (stoneTask == null) { return 0; }
		long time = stoneTask.scheduledExecutionTime() - System.currentTimeMillis();
		if (time <= 0) { time = 0; }
		if (stoneTask.scheduledExecutionTime() == 0) { time = 0; }
		return time;
	}
	public long getCobbleTimeLeft() {
		if (cobbleTask == null) { return 0; }
		long time = cobbleTask.scheduledExecutionTime() - System.currentTimeMillis();
		if (time <= 0) { time = 0; }
		if (cobbleTask.scheduledExecutionTime() == 0) { time = 0; }
		return time;
	}
	public int getOreId() {
		return itemid;
	}
	public int getMinutes() {
		return minutes;
	}
}
