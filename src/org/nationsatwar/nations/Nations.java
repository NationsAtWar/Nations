package org.nationsatwar.nations;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Nations extends JavaPlugin {
	
	public String getVersion() {
		return "0.1";
	}
	
	public void onEnable() {
		
		this.getConfig().options().copyDefaults(true);
		
		this.getLogger().info(this.getVersion()+ " Loaded");
		
	}
	
	public void onDisable() {
		this.saveConfig();
		this.getLogger().info(this.getVersion()+ " Unloaded");
	}
	
	public void reload(CommandSender sender) {

	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return false; //command.execute(sender, cmd, commandLabel, args);
	}
	
	public void messageAll(String message) {
		this.getServer().broadcastMessage(ChatColor.DARK_RED + "["+this.getName()+"]: " + message);
	}
}