package net.gamesketch.bukkit.growingore.listeners;

import net.gamesketch.bukkit.growingore.core;
import net.gamesketch.bukkit.growingore.methods.PlayerData;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class player extends PlayerListener {

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled()) { return; } //check if event is cancelled
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }  //check if the interact is a right-click-block
		if (!event.getPlayer().isOp()) { return; } //check if player is an OP
		if (event.getClickedBlock().getType().equals(Material.SOIL)) { return; } //check if clicked block = farmland (prevent farming)
		if (!event.getPlayer().getItemInHand().getType().equals(Material.SEEDS)) { return; } //check if the item in hand are seeds
		

		if (core.getRegisteredOre(event.getClickedBlock()) != null) {
			event.getPlayer().sendMessage("This ore is already registered"); 
			return;
		}
		
		if (!core.isOreSelected(event.getClickedBlock())) {
			PlayerData playerData = core.getPlayerData(event.getPlayer());
			playerData.addSelectedBlock(event.getClickedBlock());
			event.getPlayer().sendMessage("Added block (" + event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + ") to your selection (" + playerData.getSelectedBlocks().size() + " total)");
		} else {
			event.getPlayer().sendMessage("This ore is already selected by someone");
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) { //remove playerdata if player leaves
		core.getPlayerData(event.getPlayer()).remove();
	}

	
		
}
