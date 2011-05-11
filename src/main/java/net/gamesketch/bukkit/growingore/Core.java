package net.gamesketch.bukkit.growingore;

import java.util.ArrayList;
import java.util.List;

import net.gamesketch.bukkit.growingore.data.RegisteredData;
import net.gamesketch.bukkit.growingore.listeners.GOBlockListener;
import net.gamesketch.bukkit.growingore.listeners.GOEntityListener;
import net.gamesketch.bukkit.growingore.listeners.GOPlayerListener;
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

public class Core extends JavaPlugin {

	private GOPlayerListener playerListener;
	private GOBlockListener blockListener;
	private GOEntityListener entityListener;

	//Lists of data
	private List<PlayerData> playerData;
	private List<RegisteredOre> registeredOres;
	private List<TempOre> tempOres;
	
	//Reference for server();
	public static Server server;
	
	public void onDisable() {
		//Save all data
		RegisteredData.Save();
		
		//Clear all timers
		for (RegisteredOre ore : registeredOres) {
			ore.deactivateTimers();
		}
		System.out.println("[GrowingOre] Saved " + registeredOres.size() + " ores to regrow.");
		
		//remove all temp ores
		for (TempOre ore : tempOres) {
			ore.getBlock().setTypeId(1);
		}
		System.out.println("[GrowingOre] Restored " + tempOres.size() + " temp ores to stone.");
	}

	public void onEnable() {
		//Reference for server();
		server = getServer();
		//Initalize the lists of data
		playerData = new ArrayList<PlayerData>();
		registeredOres = new ArrayList<RegisteredOre>();
		tempOres = new ArrayList<TempOre>();
		
		//Load data
		RegisteredData.Load();
		System.out.println("[GrowingOre] Loaded " + registeredOres.size() + " ores to regrow.");
		
		//Register listeners and events
		playerListener = new GOPlayerListener(this);
		blockListener = new GOBlockListener(this);
		entityListener = new GOEntityListener(this);

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

	public List<PlayerData> getPlayerDataList() {
		return playerData;
	}

	public PlayerData getPlayerData(Player p) {
		for (PlayerData dat : playerData) {
			if (dat.getPlayer().getName().equals(p.getName())) {
				return dat;
			}
		}
		PlayerData pd = new PlayerData(p);
		playerData.add(pd);
		return pd;
	}

	public boolean isOreSelected(Block b) {
		for (PlayerData dat : playerData) {
			if (dat.isBlockSelected(b)) { return true; }
		}
		return false;
	}

	public  List<RegisteredOre> getRegisteredOresList() {
		return registeredOres;
	}

	public RegisteredOre getRegisteredOre(Block b) {
		for (RegisteredOre rore : registeredOres) {
			Block ore = rore.getBlock();
			if (ore.getX() == b.getX() && ore.getY() == b.getY() && ore.getZ() == b.getZ() && ore.getWorld().getName() == b.getWorld().getName()) {
				return rore;
			}
		}
		return null;
	}

	public List<TempOre> getTempOres() {
		return tempOres;
	}

	public TempOre getTemporaryOre(Block b) {
		for (TempOre tore : tempOres) {
			Block ore = tore.getBlock();
			if (ore.getX() == b.getX() && ore.getY() == b.getY() && ore.getZ() == b.getZ() && ore.getWorld().getName() == b.getWorld().getName()) {
				return tore;
			}
		}
		return null;
	}

	public void growNear(Block bl, int blockID, int percent) {
		int a = -2;
		int b = -2;
		int c = -2;
		while (c <= 2) {
			Block cur = bl.getWorld().getBlockAt(bl.getX() + a, bl.getY() + b, bl.getZ() + c);
			if (getRegisteredOre(cur) == null) { 
				if (cur.getTypeId() == 1) {
					if ((Math.random() * 1000) <= percent) {
						cur.setTypeId(blockID); 
						tempOres.add(new TempOre(cur));
					}
				}
			}
			a += 1;
			if (a > 2) { a = -2; b += 1; }
			if (b > 2) { b = -2; c += 1; }
		}
	}
}
