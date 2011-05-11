package net.gamesketch.bukkit.growingore;

import java.util.ArrayList;
import java.util.List;

import net.gamesketch.bukkit.growingore.data.RegisteredData;
import net.gamesketch.bukkit.growingore.listeners.block;
import net.gamesketch.bukkit.growingore.methods.PlayerData;
import net.gamesketch.bukkit.growingore.methods.RegisteredOre;
import net.gamesketch.bukkit.growingore.methods.TempOre;
import net.gamesketch.bukkit.growingore.listeners.*;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class core extends JavaPlugin {
	PlayerListener playerListener = new player();
	BlockListener blockListener = new block();
	EntityListener entityListener = new entity();
	
	//Lists of data
	public static List<PlayerData> PLAYERDATA;
	public static List<RegisteredOre> REGISTEREDORES;
	public static List<TempOre> TEMPORES;
	
	//Reference for server();
	public static Server server;
	
	public void onDisable() {
		//Save all data
		RegisteredData.Save();
		
		//Clear all timers
		for (RegisteredOre ore : REGISTEREDORES) {
			ore.deactivateTimers();
		}
		System.out.println("[GrowingOre] Saved " + REGISTEREDORES.size() + " ores to regrow.");
		
		//remove all temp ores
		for (TempOre ore : TEMPORES) {
			ore.getBlock().setTypeId(1);
		}
		System.out.println("[GrowingOre] Restored " + TEMPORES.size() + " temp ores to stone.");
	}

	public void onEnable() {
		//Reference for server();
		server = getServer();
		//Initalize the lists of data
		PLAYERDATA = new ArrayList<PlayerData>();
		REGISTEREDORES = new ArrayList<RegisteredOre>();
		TEMPORES = new ArrayList<TempOre>();
		
		//Load data
		RegisteredData.Load();
		System.out.println("[GrowingOre] Loaded " + REGISTEREDORES.size() + " ores to regrow.");
		
		//Register events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener,Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_EXPLODE, this.entityListener, Event.Priority.Normal, this);
		
		//Output the console that the server started
		PluginDescriptionFile pdfFile = getDescription();
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");

	}
	  
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
	    String commandName = command.getName().toLowerCase();
	    if (sender instanceof Player) { } else { return true; }
	    Player player = (Player)sender;
	    
	    String result = CommandHandler.handle(player, commandName, args);
	    if (result.length() > 1) {
	    	player.sendMessage(ChatColor.RED + "[ERROR: " + result + "]");
	    	return false; 
	    }
	    return true;
	    
	}
	
	public static PlayerData getPlayerData(Player p) {
		for (PlayerData dat : PLAYERDATA) {
			if (dat.getPlayer().getName().equals(p.getName())) {
				return dat;
			}
		}
		PlayerData pd = new PlayerData(p);
		PLAYERDATA.add(pd);
		return pd;
	}
	public static boolean isOreSelected(Block b) {
		for (PlayerData dat : PLAYERDATA) {
			if (dat.isBlockSelected(b)) { return true; }
		}
		return false;
	}
	public static RegisteredOre getRegisteredOre(Block b) {
		for (RegisteredOre rore : REGISTEREDORES) {
			Block ore = rore.getBlock();
			if (ore.getX() == b.getX() && ore.getY() == b.getY() && ore.getZ() == b.getZ() && ore.getWorld().getName() == b.getWorld().getName()) {
				return rore;
			}
		}
		return null;
	}
	public static TempOre getTemporaryOre(Block b) {
		for (TempOre tore : TEMPORES) {
			Block ore = tore.getBlock();
			if (ore.getX() == b.getX() && ore.getY() == b.getY() && ore.getZ() == b.getZ() && ore.getWorld().getName() == b.getWorld().getName()) {
				return tore;
			}
		}
		return null;
	}
	public static void growNear(Block bl, int blockID, int percent) {
		int a = -2;
		int b = -2;
		int c = -2;
		while (c <= 2) {
			Block cur = bl.getWorld().getBlockAt(bl.getX() + a, bl.getY() + b, bl.getZ() + c);
			if (getRegisteredOre(cur) == null) { 
				if (cur.getTypeId() == 1) {
					if ((Math.random() * 1000) <= percent) {
						cur.setTypeId(blockID); 
						core.TEMPORES.add(new TempOre(cur));
					}
				}
			}
			a += 1;
			if (a > 2) { a = -2; b += 1; }
			if (b > 2) { b = -2; c += 1; }
		}
	}
}
