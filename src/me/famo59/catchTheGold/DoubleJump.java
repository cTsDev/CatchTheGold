package me.famo59.catchTheGold;


import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJump implements Listener{
	
	public double height = 1.50;
	public double multiply = 1.50;
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
			if(Main.CTGList.contains(p.getName())){
				if(p.getGameMode() != GameMode.CREATIVE && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR){
					p.setAllowFlight(true);
				}
		}
	}
	
	@EventHandler
	public void onFly(PlayerToggleFlightEvent e){
		Player p = e.getPlayer();
		if(Main.CTGList.contains(p.getName())){
			if(p.getGameMode() != GameMode.CREATIVE){
				e.setCancelled(true);
				p.setAllowFlight(false);
				p.setFlying(false);
				p.setVelocity(p.getLocation().getDirection().multiply(1.0*multiply).setY(1.0* height));
				p.setFallDistance(0.0F);
				p.getLocation().getWorld().playSound(p.getLocation(), Sound.EXPLODE, 1.0F, -5.0F);
			}
		}
	}
	
}
