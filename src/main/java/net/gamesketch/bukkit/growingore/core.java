package net.gamesketch.bukkit.growingore;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.player.PlayerListener;

public class core extends JavaPlugin {
	private final PlayerListener playerListener = new PlayerListener();
	
	  public void onDisable() {
	  }

	  public void onEnable() {
		    PluginManager pm = getServer().getPluginManager();
		    PluginDescriptionFile pdfFile = getDescription();
		    pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Priority.Normal, this);
		    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");

	  }
	
}
