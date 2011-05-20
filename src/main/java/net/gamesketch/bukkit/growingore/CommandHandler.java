package net.gamesketch.bukkit.growingore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.gamesketch.bukkit.growingore.methods.PlayerData;
import net.gamesketch.bukkit.growingore.methods.RegisteredOre;
import net.gamesketch.bukkit.growingore.methods.SelectedOre;
import net.gamesketch.bukkit.growingore.selections.Area;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin; 

public class CommandHandler {
	
	public static String handle(Core plugin, Player p, String command, String[] args) {
		if (command.equals("ore")) {
			if (args.length < 1) { return "False arguments"; }
			if (args[0].equals("add")) {
				if (!p.isOp()) { return "You don't have the permissions to use this command"; }
				PlayerData pd = plugin.getPlayerData(p);
				int itemid;
				int minutes;
				
				if (args.length < 3) { return "Use the format `/ore add <itemid> <minutes> `"; }
				try { itemid = Integer.parseInt(args[1]); minutes = Integer.parseInt(args[2]); }
				catch (NumberFormatException e) { return "Number excepted, String given"; }
				
				if (pd.getSelectedBlocks().size() < 1) { return "Select some ores first with seeds as tool"; }
				for (SelectedOre ore : pd.getSelectedBlocks()) {
					ore.register(plugin, itemid, minutes);
				}
				p.sendMessage(ChatColor.GREEN + "Added " + pd.getSelectedBlocks().size() + " ores to regrow");
				pd.getSelectedBlocks().clear();
				return "";
			}
			if (args[0].equals("remove")) {
				if (!p.isOp()) { return "You don't have the permissions to use this command"; }
				
				int radius;
				int startSize = plugin.getRegisteredOresList().size();
				
				if (args.length < 2) { return "Use the format `/ore remove <radius>"; }
				try { radius = Integer.parseInt(args[1]); }
				catch (NumberFormatException e) { return "Number excepted, String given"; }
				
				List<RegisteredOre> removes = new ArrayList<RegisteredOre>();
				for (RegisteredOre ore : plugin.getRegisteredOresList()) {
					if (ore.getBlock().getLocation().toVector().distance(p.getLocation().toVector()) <= radius) {
						ore.deactivateTimers();
						removes.add(ore);
						ore.getBlock().setTypeId(1);
					}
				}
				for (RegisteredOre ore : removes) {
					plugin.getRegisteredOresList().remove(ore);
				}
				p.sendMessage(ChatColor.GREEN + "Removed " + (startSize - plugin.getRegisteredOresList().size()) + " ores in " + radius + " radius.");
				return "";
			}
			if (args[0].equals("count")) {
				if (!p.isOp()) { return "You don't have the permissions to use this command"; }
				
				int radius;
				int startSize = 0;
				
				if (args.length < 2) { return "Use the format `/ore count <radius>"; }
				try { radius = Integer.parseInt(args[1]); }
				catch (NumberFormatException e) { return "Number excepted, String given"; }
				
				for (RegisteredOre ore : plugin.getRegisteredOresList()) {
					if (ore.getBlock().getLocation().toVector().distance(p.getLocation().toVector()) <= radius) {
						startSize += 1;
					}
				}
				p.sendMessage(ChatColor.GREEN + "Counted " + startSize + " growing ores in a " + radius + " radius.");
				return "";
			}
			if (args[0].equals("select")) {
				if (args.length < 2) { return "Wrong arguments"; }
				if (args[1].equals("none")) {
					PlayerData pd = plugin.getPlayerData(p);
					if (pd.getSelectedBlocks().size() >= 1) { 
						for (SelectedOre ore : pd.getSelectedBlocks()) {
							ore.restore();
							ore = null;
						}
					}
					p.sendMessage(ChatColor.GREEN + "Unselected " + pd.getSelectedBlocks().size() + " blocks.");
					pd.getSelectedBlocks().clear();
					return "";
				}
				if (args[1].equals("radius")) {
					int radius;
					if (args[2] == null) { return "Nothing given, Number expected"; }
					try { radius = Integer.parseInt(args[2]); }
					catch (NumberFormatException e) { return "String given, Number expected"; }
					if (radius >= 200) { radius = 200; }
					if (radius <= 0) { radius = 5; }
					if (radius >= 75) { p.sendMessage(ChatColor.RED + "Scanning a large area for ores, this can give some lag"); }
					
					List<Material> oreIDs = new LinkedList<Material>();
					oreIDs.add(Material.COAL_ORE); oreIDs.add(Material.IRON_ORE);
					oreIDs.add(Material.GOLD_ORE); oreIDs.add(Material.DIAMOND_ORE);
					oreIDs.add(Material.REDSTONE_ORE); oreIDs.add(Material.GLOWING_REDSTONE_ORE);
					oreIDs.add(Material.LAPIS_ORE);

					List<Block> blocks = Area.getBlocks(p.getWorld().getBlockAt(p.getLocation()), radius, oreIDs);
					PlayerData pd = plugin.getPlayerData(p);
					for (Block b : blocks) {
						if (plugin.getRegisteredOre(b) != null) { continue; }
						pd.addSelectedBlock(b);
					}
					p.sendMessage(ChatColor.GREEN + "Selected " + blocks.size() + " new blocks in " + radius + " radius. (" + pd.getSelectedBlocks().size() + " total)");
					return "";
				}
			}
			return "Wrong parameters";
		}
		return "";
	}
	
}
