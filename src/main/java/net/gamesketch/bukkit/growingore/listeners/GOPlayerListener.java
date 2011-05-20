package net.gamesketch.bukkit.growingore.listeners;

import net.gamesketch.bukkit.growingore.Core;
import net.gamesketch.bukkit.growingore.methods.PlayerData;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GOPlayerListener extends PlayerListener {

	private Core plugin;

	public GOPlayerListener(Core plugin) {
		super();
		this.plugin = plugin;
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled()) { return; } //check if event is cancelled
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }  //check if the interact is a right-click-block
		if (!event.getPlayer().isOp()) { return; } //check if player is an OP
		if (event.getClickedBlock().getType().equals(Material.SOIL)) { return; } //check if clicked block = farmland (prevent farming)
		if (!event.getPlayer().getItemInHand().getType().equals(Material.SEEDS)) { return; } //check if the item in hand are seeds
		

		if (plugin.getRegisteredOre(event.getClickedBlock()) != null) {
			event.getPlayer().sendMessage("This ore is already registered"); 
			return;
		}
		
		if (!plugin.isOreSelected(event.getClickedBlock())) {
			PlayerData playerData = plugin.getPlayerData(event.getPlayer());
			playerData.addSelectedBlock(event.getClickedBlock());
			event.getPlayer().sendMessage("Added block (" + event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + ") to your selection (" + playerData.getSelectedBlocks().size() + " total)");
		} else {
			event.getPlayer().sendMessage("This ore is already selected by someone");
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) { //remove playerdata if player leaves
		plugin.getPlayerData(event.getPlayer()).remove(plugin);
	}
}
