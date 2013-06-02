package org.nationsatwar.nations.listeners;

import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.Town;
import org.nationsatwar.nations.objects.User;

public class NationsUserListener implements Listener {
	
	private Nations plugin;

	public NationsUserListener(Nations instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public synchronized void onPlayerJoin(PlayerJoinEvent event) {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		Player player = event.getPlayer();
		if(!nations.userManager.getUserList().contains(player.getName())) {
			if(player.hasPermission("nationsatwar.nations.player")) {
				User user = nations.userManager.getUserByPlayer(player);
				if(user == null) {
					if(nations.userManager.createUser(player.getName())) {
						nations.getLogger().log(Level.FINE, "Added user: " + player.getName());
					}
				}
			}
		}
	}
	
	@EventHandler
	public synchronized void onPlayerMove(PlayerMoveEvent event) {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		Player player = event.getPlayer();
		if(player == null) {
			return;
		}
		
		if(nations.userManager.getUserList().contains(player.getName())) {
			User user = nations.userManager.getUserByPlayer(player);
			if(user != null) {
				nations.userManager.updateLocation(user);
			}
		}
	}
	
	@EventHandler
	public synchronized void onPlayerTeleport(PlayerTeleportEvent event) {
	//Blocks enderpearl teleporting in nations that are not your own.
		if(plugin.getConfig().getBoolean("blockpearls", true)) {
			User user = plugin.userManager.getUserByPlayer(event.getPlayer());
			Plot plot = plugin.plotManager.getPlotByLocation(event.getTo().getBlock().getLocation());
			
			if(event.getCause()==TeleportCause.ENDER_PEARL) {
				if(plot != null && user != null) {
					Town plotTown = plugin.townManager.getTownByID(plot.getTownID());
					if(plotTown == null) {
						return;
					}
					Nation plotNation = plugin.nationManager.getNationByID(plotTown.getNationID());
					if(plotNation == null) {
						return;
					}
					Nation userNation = plugin.nationManager.getNationByUserID(user.getID());
					if(userNation == null) {
						event.setCancelled(true);
						event.setTo(event.getFrom());
						return;
					}
					if(plotNation.getID() != userNation.getID()) {
						event.setCancelled(true);
						event.setTo(event.getFrom());
						return;
					}
				}
			}
		}
	}

}
