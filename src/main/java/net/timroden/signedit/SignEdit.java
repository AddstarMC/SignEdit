package net.timroden.signedit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.timroden.signedit.commands.CommandSignEdit;
import net.timroden.signedit.data.SignEditDataPackage;
import net.timroden.signedit.localization.SignEditLocalization;
import net.timroden.signedit.utils.SignEditLogger;
import net.timroden.signedit.utils.SignEditUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SignEdit extends JavaPlugin {
	public final String chatPrefix = ChatColor.RESET + "[" + ChatColor.AQUA + "SignEdit" + ChatColor.WHITE + "] " + ChatColor.RESET;
	public PluginManager pluginMan;
	public final Map<UUID, SignEditDataPackage> playerData = new HashMap<>();
	public final Map<UUID, Integer> pasteAmounts = new HashMap<>();
	public SignEditLogger log;
	public SignEditUtils utils;
	public SignEditLocalization localization;
	public Config config;
	public YML yml;

        @Override
	public void onEnable() {
		this.config = new Config(this);
		this.yml = new YML(this);
		this.localization = new SignEditLocalization(this);

		this.utils = new SignEditUtils(this);
		this.log = new SignEditLogger(this);

			SignEditPlayerListener listener = new SignEditPlayerListener(this);

		this.pluginMan = getServer().getPluginManager();

		this.pluginMan.registerEvents(listener, this);

		getCommand("signedit").setExecutor(new CommandSignEdit(this));
	}

        @Override
	public void onDisable() {}
}