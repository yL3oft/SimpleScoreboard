package me.leonardo.scoreboard.commands;

import me.leonardo.scoreboard.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreboardCommand implements CommandExecutor{

	@SuppressWarnings("unused")
	private Main plugin;
	
	public ScoreboardCommand(Main plugin){
		this.plugin = plugin;
		plugin.getCommand("scoreboard").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(s instanceof Player){
			String perm = Main.main.getConfig().getString("General.perm");
			Player p = (Player) s;
			if(p.hasPermission(perm)){
				if(args.length >= 1){
					String subcmd = args[0];
					if(subcmd.equalsIgnoreCase("setline") || subcmd.equalsIgnoreCase("line")){
						if(args.length >= 4){
							String line = args[2];
							String board = args[1];
							if(isNumeric(line)){
								String msg = "";
								for(int i = 3; i< args.length;i++){
									msg = msg + args[i] + " ";
								}
								if(Main.main.getConfig().contains(board+".lines."+line+".line")){
									p.sendMessage(setlinesucess(line, colored(msg)));
									Main.main.getConfig().set(board+".lines."+line+".line", msg);
									Main.main.saveConfig();
								}else if(Integer.parseInt(line) <= 15){
									p.sendMessage(setlinesucess(line, colored(msg)));
									Main.main.getConfig().set(board+".lines."+line+".line", msg);
									Main.main.saveConfig();
								}
							}else{
								p.sendMessage(colored(notint(line)));
							}
						}else{
							usages(p, true, false, false);
						}
					}else if(subcmd.equalsIgnoreCase("settitle") || subcmd.equalsIgnoreCase("title")){
						if(args.length >= 3){
							String board = args[1];
							String msg = "";
							for(int i = 2; i< args.length;i++){
								msg = msg + args[i] + " ";
							}
							p.sendMessage(settitlesucess(msg));
							Main.main.getConfig().set(board+".title", msg);
							Main.main.saveConfig();
						}else{
							usages(p, false, true, false);
						}
					}else if(subcmd.equalsIgnoreCase("reload") || subcmd.equalsIgnoreCase("rl")){
						p.sendMessage(reloadsucess());
						Main.main.saveDefaultConfig();
					}else{
						usages(p, true, true, true);
					}
				}else{
					usages(p, true, true, true);
				}
			}else{
				p.sendMessage(noperm());
			}
		}
	return false;
	}
	
	
	public void usages(Player p, boolean setline, boolean settitle, boolean reload){
		String setlinem = Main.main.getConfig().getString("Lang.usages.scoreboard-setline");
		String settitlem = Main.main.getConfig().getString("Lang.usages.scoreboard-settitle");
		String reloadm = Main.main.getConfig().getString("Lang.usages.scoreboard-reload");
		if(setline == true){
			p.sendMessage(colored(setlinem));
		}
		if(settitle == true){
			p.sendMessage(colored(settitlem));
		}
		if(reload == true){
			p.sendMessage(colored(reloadm));
		}
	}
	
	
	public String notint(String supposednumber){
		return Main.main.getConfig().getString("Lang.messages.scoreboard-setline-notnumber")
				.replace("{supposed-number}", supposednumber);
	}
	
	
	public String setlinesucess(String line, String linemsg){
		return colored(Main.main.getConfig().getString("Lang.messages.scoreboard-setline-sucess")
				.replace("{line}", line)
				.replace("{line-msg}", linemsg));
	}
	
	
	public String settitlesucess(String title){
		return colored(Main.main.getConfig().getString("Lang.messages.scoreboard-settitle-sucess")
				.replace("{title}", title));
	}
	
	
	public String reloadsucess(){
		return colored(Main.main.getConfig().getString("Lang.messages.scoreboard-reload-sucess"));
	}
	
	
	public String noperm(){
		return colored(Main.main.getConfig().getString("Lang.noperm"));
	}
	
	
	public String colored(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	
	public boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        @SuppressWarnings("unused")
			int i = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}
