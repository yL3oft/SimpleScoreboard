package me.leonardo.scoreboard.events;

import me.leonardo.scoreboard.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener{

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		
		
		String path = "Scoreboards";
		for(String world : Main.main.getConfig().getConfigurationSection(path).getKeys(false)){
			if(p.getWorld().getName().equalsIgnoreCase(world)){
				Main.main.scoreboard(p, Main.main.getConfig().getString(path+"."+world));
			}
		}
	}
}
