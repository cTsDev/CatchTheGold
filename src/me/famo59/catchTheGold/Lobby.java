package me.famo59.catchTheGold;


import org.bukkit.command.CommandExecutor;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class Lobby implements CommandExecutor{
	
	public static File file = new File("plugins/CTG", "warps.yml");
	public FileConfiguration cfg = YamlConfiguration.loadConfiguration(Lobby.file);
	
	private String err_need_player = "Du musst ein Spieler sein";
	

	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args){
		
		
		Player p = null;
		if(cs instanceof Player){
			
			p = (Player) cs;
		}

		if(label.equalsIgnoreCase("setctglobby")){
			if(p != null){
				if(cs.isOp()){
				Location loc = p.getLocation();
				String str = "lobby.";
				this.cfg.set(str +"lobby" +"world", loc.getWorld().getName());
				this.cfg.set(str +"lobby" + ".x", loc.getX());
				this.cfg.set(str +"lobby" + ".y", loc.getY());
				this.cfg.set(str +"lobby" + ".z", loc.getZ());
				this.cfg.set(str +"lobby" + ".yaw", loc.getYaw());
				this.cfg.set(str +"lobby" + ".pitch", loc.getPitch());
				try{
					this.cfg.save(Lobby.file);
					cs.sendMessage(ChatColor.RED + "Lobby gesetzt");
				
					return true;
				} catch(IOException ee){
					
					
				}
				}
			} else {
				cs.sendMessage(this.err_need_player);
			}
			return true;
			
			
		}
		if(label.equalsIgnoreCase("setctgspawn")){
			if(args[0] != null){
			if(p != null){
				if(cs.isOp()){
				Location loc = p.getLocation();
				String str = "spawn.";
				this.cfg.set(str + args[0] +  "world", loc.getWorld().getName());
				this.cfg.set(str + args[0] + ".x", loc.getX());
				this.cfg.set(str + args[0] + ".y", loc.getY());
				this.cfg.set(str + args[0] + ".z", loc.getZ());
				this.cfg.set(str + args[0] + ".yaw", loc.getYaw());
				this.cfg.set(str + args[0] + ".pitch", loc.getPitch());
				try{
					this.cfg.save(Lobby.file);
					cs.sendMessage(ChatColor.RED + "Spawn gesetzt");
				
					return true;
				} catch(IOException ee){
					
					
				}
				}
			} else {
				cs.sendMessage(this.err_need_player);
			}
			}
			return true;
			
			
		}
		
		if(label.equalsIgnoreCase("setctgleave")){
			if(p != null){
				if(cs.isOp()){
				Location loc = p.getLocation();
				String str = "leave.";
				this.cfg.set(str +"leave" + "world", loc.getWorld().getName());
				this.cfg.set(str +"leave" + ".x", loc.getX());
				this.cfg.set(str +"leave" + ".y", loc.getY());
				this.cfg.set(str +"leave" + ".z", loc.getZ());
				this.cfg.set(str +"leave" + ".yaw", loc.getYaw());
				this.cfg.set(str +"leave" + ".pitch", loc.getPitch());
				try{
					this.cfg.save(Lobby.file);
					cs.sendMessage(ChatColor.RED + "Leave gesetzt");
				
					return true;
				} catch(IOException ee){
					
					
				}
				}
			} else {
				cs.sendMessage(this.err_need_player);
			}
			return true;
			
			
		}
		if(label.equalsIgnoreCase("setctgbat")){
			if(p != null){
				if(cs.isOp()){
				Location loc = p.getLocation();
				String str = "bat.";
				this.cfg.set(str +"bat" + "world", loc.getWorld().getName());
				this.cfg.set(str +"bat" + ".x", loc.getX());
				this.cfg.set(str +"bat" + ".y", loc.getY());
				this.cfg.set(str +"bat" + ".z", loc.getZ());
				this.cfg.set(str +"bat" + ".yaw", loc.getYaw());
				this.cfg.set(str +"bat" + ".pitch", loc.getPitch());
				try{
					this.cfg.save(Lobby.file);
					cs.sendMessage(ChatColor.RED + "Bat-Spawn gesetzt");
				
					return true;
				} catch(IOException ee){
					
					
				}
				}
			} else {
				cs.sendMessage(this.err_need_player);
			}
			return true;
			
			
		}
		return false;
		
	}
}
