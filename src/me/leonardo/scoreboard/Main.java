package me.leonardo.scoreboard;

import java.io.File;

import me.leonardo.scoreboard.commands.*;
import me.leonardo.scoreboard.events.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Main extends JavaPlugin{

	public static Main main;
	
	@Override
	public void onEnable(){
		main = Main.this;
		
		
		saveDefaultConfig();
		
		
		new ScoreboardCommand(this);
		
		
		getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new PlayerChangeWorld(), this);
		
		
		new BukkitRunnable() {
			
		    @Override
		    public void run() {
		    	
		    	for(Player people : Bukkit.getOnlinePlayers()){
		    		
		    		String path = "Scoreboards";
		    		for(String world : getConfig().getConfigurationSection(path).getKeys(false)){
		    			if(people.getWorld().getName().equalsIgnoreCase(world)){
		    				scoreboard(people, getConfig().getString(path+"."+world));
		    			}
		    		}
		    	}
		    	
		    }
		}.runTaskTimer(Main.main, 0, getConfig().getInt("Delay"));
	}
	
	@Override
	public void onDisable(){
		saveDefaultConfig();
	}
	
	
	public void scoreboard(Player p, String board){
		//Manager and board
		ScoreboardManager m = Bukkit.getScoreboardManager();
		Scoreboard b = m.getNewScoreboard();
		
		//Objectives
		Objective o = b.registerNewObjective(board, "");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		if(Main.main.getConfig().contains(board+".title")){
			String title = Main.main.getConfig().getString(board+".title");
			o.setDisplayName(colored(title));
		}else{
			return;
		}
		
		//Scores
		checkscore(p, "1", o, board);
		checkscore(p, "2", o, board);
		checkscore(p, "3", o, board);
		checkscore(p, "4", o, board);
		checkscore(p, "5", o, board);
		checkscore(p, "6", o, board);
		checkscore(p, "7", o, board);
		checkscore(p, "8", o, board);
		checkscore(p, "9", o, board);
		checkscore(p, "10", o, board);
		checkscore(p, "11", o, board);
		checkscore(p, "12", o, board);
		checkscore(p, "13", o, board);
		checkscore(p, "14", o, board);
		checkscore(p, "15", o, board);
		
		
		p.setScoreboard(b);
	}
	
	
	public void checkscore(Player p, String number, Objective o, String board){
		String boardline = board+".lines."+number+".line";
		int bytes = 1048576;
		float maxMemory = Runtime.getRuntime().maxMemory() / bytes;
		float usedMemory = maxMemory - Runtime.getRuntime().freeMemory() / bytes;
		if(Main.main.getConfig().isSet(boardline)){
			String scorem = "";
			if(getServer().getPluginManager().getPlugin("SimpleGroups") != null){
				File f = new File("plugins/SimpleGroups/Groups.yml");
				File f2 = new File("plugins/SimpleGroups/PlayersGroups.yml");
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
				FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(f2);
				scorem = colored(Main.main.getConfig().getString(boardline)
						.replace("%player-name%", p.getName())
						.replace("%player-displayname%", p.getDisplayName())
						.replace("%player-health%", String.valueOf(p.getHealth()))
						.replace("%player-maxhealth%", String.valueOf(p.getMaxHealth()))
						.replace("%player-latency%", String.valueOf(((CraftPlayer) p).getHandle().ping))
						.replace("%server-usedram%", String.valueOf(usedMemory))
						.replace("%server-maxram%", String.valueOf(maxMemory))
						.replace("%online-players%", String.valueOf(Bukkit.getOnlinePlayers().size()))
						.replace("%rank%", cfg2.getString("Players."+p.getUniqueId().toString()))
						.replace("%rank-prefix%", cfg.getString("Groups."+cfg2.getString("Players."+p.getUniqueId().toString())+".prefix")));
			}else{
				scorem = colored(Main.main.getConfig().getString(boardline)
						.replace("%player-name%", p.getName())
						.replace("%player-displayname%", p.getDisplayName())
						.replace("%player-health%", String.valueOf(p.getHealth()))
						.replace("%player-maxhealth%", String.valueOf(p.getMaxHealth()))
						.replace("%player-latency%", String.valueOf(((CraftPlayer) p).getHandle().ping))
						.replace("%server-usedram%", String.valueOf(usedMemory))
						.replace("%server-maxram%", String.valueOf(maxMemory))
						.replace("%online-players%", String.valueOf(Bukkit.getOnlinePlayers().size())));
			}
			Score score = o.getScore(scorem);
			score.setScore(Integer.parseInt(number));
		}
	}
	
	
	public String colored(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}
