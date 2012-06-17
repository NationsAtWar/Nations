package org.nationsatwar.nations.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Invite;
import org.nationsatwar.nations.objects.User;

public class Accept extends NationsCommand {

	protected Accept(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	}

	@Override
	public void run() {
		// --accept || -accept help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "e.g. '/accept [list|player|town|nation]'", "Accepts and lists any invitations you may have.");
			return;
		}
		
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		if(command[0].equalsIgnoreCase("list")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/accept list", "Lists any invites you may have that you can accept.");
				return;
			}
			
			User user = nations.userManager.getUserByName(commandSender.getName());
			ArrayList<Invite> invites = null;
			if(user != null) {
				invites = nations.inviteManager.getInvites(user);
			}
			if(invites == null || invites.isEmpty()) {
				this.successText(commandSender, null, "No invites present.");
			}
			for(Invite inv : invites) {
				User inviter = (User) inv.getInviter();
				this.successText(commandSender, inv.getNiceType()+" Invitation From: "+inviter.getName()+" ("+nations.nationManager.getNationByUsername(inviter.getName())+")", null);
			}
			return;
		}
		
		if(command[0].equalsIgnoreCase("player")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/accept player [player name]", "accepts a nation invite from a player.");
				return;
			}
			
			User user = nations.userManager.getUserByName(commandSender.getName());
			ArrayList<Invite> invites = null;
			if(user != null) {
				invites = nations.inviteManager.getInvites(user);
			}
			if(invites == null || invites.isEmpty()) {
				this.successText(commandSender, null, "No invites present.");
			}
			for(Invite inv : invites) {
				User inviter = (User) inv.getInviter();
				if(inviter.getName().equalsIgnoreCase(command[1])) {
					if(nations.nationManager.getNationByUsername(user.getName()).removeMember(user) && 
							nations.nationManager.getNationByUsername(inviter.getName()).addMember(user)) {
						this.successText(commandSender, "You've accepted your invitation into "+nations.nationManager.getNationByUsername(inviter.getName()).getName()+".", null);					
					}
					return;
				}
			}
			this.errorText(commandSender, "That was not a valid invitation", null);			
			
			return;
		}
		
		if(command[0].equalsIgnoreCase("town")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/accept town [town name]", "accepts your town into your nation.");
				return;
			}
		}
		
		if(command[0].equalsIgnoreCase("nation")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/accept nation [nation name]", "accepts your nation into the alliance.");
				return;
			}
		}
		
	}

}
