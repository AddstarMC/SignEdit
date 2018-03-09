package net.timroden.signedit.commands;

import net.timroden.signedit.SignEdit;
import net.timroden.signedit.data.LogType;
import net.timroden.signedit.data.SignEditDataPackage;
import net.timroden.signedit.data.SignFunction;
import net.timroden.signedit.utils.SignEditUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CommandSignEdit implements CommandExecutor {
    private final SignEdit plugin;
    private final SignEditUtils utils;
    
    public CommandSignEdit(SignEdit plugin) {
        this.plugin = plugin;
        this.utils = plugin.utils;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("syntaxError"));
            help(sender);
            return true;
        }
        
        if (args[0].equalsIgnoreCase("help")) {
            help(sender);
            return true;
        }
        
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("signedit.admin")) {
                sender.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("noReloadPermission"));
                return true;
            }
            this.plugin.config.reload();
            this.plugin.localization.loadLocales();
            sender.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("reloaded"));
            return true;
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("noPlayer"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("signedit.edit")) {
            player.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("noSignEditPermission"));
            return true;
        }
        
        if (args[0].equalsIgnoreCase("cancel")) {
            this.plugin.log.logAll(player.getName(), "cancel", LogType.PLAYERCOMMAND, Level.INFO);
            if (!this.plugin.playerData.containsKey(player.getUniqueId())) {
                player.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("noRequests"));
                return true;
            }
            
            this.plugin.playerData.remove(player.getUniqueId());
            player.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("requestCancelled"));
            return true;
        }
        if (args[0].equalsIgnoreCase("copy")) {
            if (!this.plugin.pasteAmounts.containsKey(player.getUniqueId())) {
                this.plugin.pasteAmounts.put(player.getUniqueId(), 1);
            }
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("persist")) {
                    this.plugin.log.logAll(player.getName(), "copy persist", LogType.PLAYERCOMMAND, Level.INFO);
                    SignEditDataPackage tmp = new SignEditDataPackage(player.getName(), SignFunction.COPYPERSIST);
                    this.plugin.playerData.put(player.getUniqueId(), tmp);
                    player.sendMessage(this.plugin.chatPrefix
                            + this.plugin.localization.get("persistEnabled", this.utils.capitalize(this.plugin.config.clickActionStr())));
                    return true;
                }
                if (args[1].equalsIgnoreCase("default")) {
                    if (!this.utils.isInt(args[2])) {
                        player.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("invalidNumber", args[2]));
                        return true;
                    }
                    this.plugin.log.logAll(player.getName(), "default " + args[2], LogType.PLAYERCOMMAND, Level.INFO);
                    this.plugin.pasteAmounts.put(player.getUniqueId(), Integer.parseInt(args[2]));
                    player.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("defaultPastesChanged", args[2]));
                    return true;
                }
                this.plugin.log.logAll(player.getName(), "copy " + args[1], LogType.PLAYERCOMMAND, Level.INFO);
                if (!this.utils.isInt(args[1])) {
                    SignEditDataPackage tmp = new SignEditDataPackage(player.getName(), SignFunction.COPY, (this.plugin.pasteAmounts.get(player.getUniqueId())));
                    this.plugin.playerData.put(player.getUniqueId(), tmp);
                    player.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("invalidNumber", args[1]));
                    player.sendMessage(this.plugin.chatPrefix
                            + this.plugin.localization.get("defaultCopyPunch",
                            this.utils.capitalize(this.plugin.config.clickActionStr()),
                            this.plugin.pasteAmounts.get(player.getUniqueId()),
                            (this.plugin.pasteAmounts.get(player.getUniqueId())) == 1 ? this.plugin.localization.get("pasteCopyStr")
                                    : this.plugin.localization.get("pasteCopiesStr")));
                    return true;
                }
                SignEditDataPackage tmp = new SignEditDataPackage(player.getName(), SignFunction.COPY, Integer.parseInt(args[1]));
                this.plugin.playerData.put(player.getUniqueId(), tmp);
                player.sendMessage(this.plugin.chatPrefix
                        + this.plugin.localization.get("intCopyPunch",
                        this.utils.capitalize(this.plugin.config.clickActionStr()), args[1],
                        Integer.parseInt(args[1]) == 1 ? this.plugin.localization.get("pasteCopyStr") : this.plugin.localization.get("pasteCopiesStr")));
                return true;
            }
            
            this.plugin.log.logAll(player.getName(), "copy", LogType.PLAYERCOMMAND, Level.INFO);
            int amt = (this.plugin.pasteAmounts.get(player.getUniqueId()));
            SignEditDataPackage tmp = new SignEditDataPackage(player.getName(), SignFunction.COPY, amt);
            this.plugin.playerData.put(player.getUniqueId(), tmp);
            player.sendMessage(this.plugin.chatPrefix
                    + this.plugin.localization.get("defaultCopyPunch",
                    this.utils.capitalize(this.plugin.config.clickActionStr()),
                    this.plugin.pasteAmounts.get(player.getUniqueId()),
                    (this.plugin.pasteAmounts.get(player.getUniqueId())) == 1 ? this.plugin.localization.get("pasteCopyStr")
                            : this.plugin.localization.get("pasteCopiesStr")));
            return true;
        }
        
        if (args.length >= 1) {
            if (!this.utils.isInt(args[0])) {
                player.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("invalidNumber", args[0]));
                return true;
            }
            
            if ((Integer.parseInt(args[0]) > 4) || (Integer.parseInt(args[0]) < 1)) {
                player.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("invalidLine", args[0]));
                return true;
            }
            
            String line = this.utils.implode(args, " ", 1, args.length);
            
            if (line == null) {
                this.plugin.log.logAll(player.getName(), args[0], LogType.PLAYERCOMMAND, Level.INFO);
                SignEditDataPackage tmp = new SignEditDataPackage(player.getName(), SignFunction.EDIT, "", Integer.parseInt(args[0]) - 1);
                this.plugin.playerData.put(player.getUniqueId(), tmp);
                player.sendMessage(this.plugin.chatPrefix
                        + this.plugin.localization.get("punchToComplete", this.utils.capitalize(this.plugin.config.clickActionStr())));
                return true;
            }
            
            if (line.length() > 15) {
                player.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("truncating"));
                line = line.substring(0, 15);
            }
            this.plugin.log.logAll(player.getName(), this.utils.implode(args, " ", 0, args.length), LogType.PLAYERCOMMAND, Level.INFO);
            SignEditDataPackage tmp = new SignEditDataPackage(player.getName(), SignFunction.EDIT, line, Integer.parseInt(args[0]) - 1);
            this.plugin.playerData.put(player.getUniqueId(), tmp);
            player.sendMessage(this.plugin.chatPrefix
                    + this.plugin.localization.get("punchToComplete", this.utils.capitalize(this.plugin.config.clickActionStr())));
            return true;
        }
        sender.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("syntaxError"));
        help(sender);
        return true;
    }
    
    private void help(CommandSender sender) {
        sender.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("commandsAvailable"));
        if ((sender instanceof Player)) {
            sender.sendMessage(this.plugin.chatPrefix + this.plugin.localization.get("commandsHeader", this.plugin.config.clickActionStr()));
            sender.sendMessage(ChatColor.GRAY + " - " + this.plugin.localization.get("commandsDef"));
            sender.sendMessage(ChatColor.GRAY + " - " + this.plugin.localization.get("commandsCancel"));
            sender.sendMessage(ChatColor.GRAY + " - " + this.plugin.localization.get("commandsCopyAmount"));
            sender.sendMessage(ChatColor.GRAY + " - " + this.plugin.localization.get("commandsCopyPersist"));
            sender.sendMessage(ChatColor.GRAY + " - " + this.plugin.localization.get("commandsCopyDefault"));
        }
        if (sender.hasPermission("signedit.admin")) {
            sender.sendMessage(ChatColor.GRAY + " - " + this.plugin.localization.get("commandsReload"));
        }
        sender.sendMessage(ChatColor.GRAY + " - " + this.plugin.localization.get("commandsHelp"));
    }
}