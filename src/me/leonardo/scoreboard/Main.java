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
		
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Version: "+getversion()));
		
		
		new BukkitRunnable() {
			
		    @Override
		    public void run() {
		    	
		    	if(Bukkit.getOnlinePlayers().size() > 0){
		    		for(Player people : Bukkit.getOnlinePlayers()){
			    		
			    		String path = "Scoreboards";
			    		for(String world : getConfig().getConfigurationSection(path).getKeys(false)){
			    			if(people.getWorld().getName().equalsIgnoreCase(world)){
			    				scoreboard(people, getConfig().getString(path+"."+world));
			    			}
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
	
	
	@SuppressWarnings("deprecation")
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
						.replace("%server-usedram%", String.valueOf(usedMemory))
						.replace("%server-maxram%", String.valueOf(maxMemory))
						.replace("%online-players%", String.valueOf(Bukkit.getOnlinePlayers().size())
						.replace("%rank%", cfg2.getString("Players."+p.getUniqueId().toString()))
						.replace("%rank-prefix%", cfg.getString("Groups."+cfg2.getString("Players."+p.getUniqueId().toString())+".prefix"))));
			}else{
				scorem = colored(Main.main.getConfig().getString(boardline)
						.replace("%player-name%", p.getName())
						.replace("%player-displayname%", p.getDisplayName())
						.replace("%player-health%", String.valueOf(p.getHealth()))
						.replace("%player-maxhealth%", String.valueOf(p.getMaxHealth()))
						.replace("%server-usedram%", String.valueOf(usedMemory))
						.replace("%server-maxram%", String.valueOf(maxMemory))
						.replace("%online-players%", String.valueOf(Bukkit.getOnlinePlayers().size())));
			}
			if(Main.main.getConfig().getString(boardline).contains("%player-latency%")){
				scorem = colored(scorem.replace("%player-latency%", getlatency(p)));
			}
			Score score = o.getScore(scorem);
			score.setScore(Integer.parseInt(number));
		}
	}
	
	
	public String getlatency(Player p){
		String[] split = Bukkit.getBukkitVersion().split("-");
		if(split.length < 1){
			return "...";
		}
		String version = split[0];
		switch(version)
		{
		    case "1.8.0":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.8.1":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.8.2":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.8.3":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer) p).getHandle().ping);
		    case "1.8.4":
		    	return String.valueOf(((CraftPlayer) p).getHandle().ping);
		    case "1.8.5":
		    	return String.valueOf(((CraftPlayer) p).getHandle().ping);
		    case "1.8.6":
		    	return String.valueOf(((CraftPlayer) p).getHandle().ping);
		    case "1.8.7":
		    	return String.valueOf(((CraftPlayer) p).getHandle().ping);
		    case "1.8.8":
		    	return String.valueOf(((CraftPlayer) p).getHandle().ping);
		    case "1.8":
		    	return String.valueOf(((CraftPlayer) p).getHandle().ping);
		    	
		    	
		    	
		    case "1.9.0":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.9.1":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.9.2":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.9.3":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.9.4":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer) p).getHandle().ping);
		    case "1.9":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer) p).getHandle().ping);
		    	
		    	
		    case "1.10.0":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.10.1":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.10.2":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.10":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer) p).getHandle().ping);
		    
		    	
		    case "1.11.0":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.11.1":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.11.2":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.11":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer) p).getHandle().ping);
		    
		    	
		    case "1.12.0":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.12.1":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.12.2":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) p).getHandle().ping);
		    case "1.12":
		    	return String.valueOf(((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) p).getHandle().ping);
		    
		    	
		    default:
		        return "...";
		}
	}
	
	public String getversion(){
		String[] split = Bukkit.getBukkitVersion().split("-");
		String version = split[0];
		switch(version)
		{
		    case "1.8.0":
		    	return "1.8.0";
		    case "1.8.1":
		    	return "1.8.1";
		    case "1.8.2":
		    	return "1.8.2";
		    case "1.8.3":
		    	return "1.8.3";
		    case "1.8.4":
		    	return "1.8.4";
		    case "1.8.5":
		    	return "1.8.5";
		    case "1.8.6":
		    	return "1.8.6";
		    case "1.8.7":
		    	return "1.8.7";
		    case "1.8.8":
		    	return "1.8.8";
		    case "1.8":
		    	return "1.8.0";
		    	
		    	
		    	
		    case "1.9.0":
		    	return "1.9.0";
		    case "1.9.1":
		    	return "1.9.1";
		    case "1.9.2":
		    	return "1.9.2";
		    case "1.9.3":
		    	return "1.9.3";
		    case "1.9.4":
		    	return "1.9.4";
		    case "1.9":
		    	return "1.9.0";
		    	
		    	
		    case "1.10.0":
		    	return "1.10.0";
		    case "1.10.1":
		    	return "1.10.1";
		    case "1.10.2":
		    	return "1.10.2";
		    case "1.10":
		    	return "1.10.0";
		    
		    	
		    case "1.11.0":
		    	return "1.11.0";
		    case "1.11.1":
		    	return "1.11.1";
		    case "1.11.2":
		    	return "1.11.2";
		    case "1.11":
		    	return "1.11.0";
		    
		    	
		    case "1.12.0":
		    	return "1.12.0";
		    case "1.12.1":
		    	return "1.12.1";
		    case "1.12.2":
		    	return "1.12.2";
		    case "1.12":
		    	return "1.12.0";
		    	
		    	
		    default:
		        return "...";
		}
	}
	
	
	public boolean vc(String v, String c){
		if(v.contains(c)){
			return true;
		}else{
			return false;
		}
	}
	
	
	public String colored(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}
