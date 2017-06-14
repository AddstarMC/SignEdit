package net.timroden.signedit;

import org.bukkit.configuration.Configuration;
import org.bukkit.event.block.Action;

public class Config {
	private final SignEdit plugin;
	private Configuration config;
	private boolean ignoreCreative;
	private boolean invertMouse;
	private boolean commandsLogConsole;
	private boolean commandsLogFile;
	private boolean colorsOnPlace;
	private boolean useCOPPerm;
	private Action clickAction;
	private String logName;
	private String locale;
	private boolean fireBlockBreakPlace;

	public Config(SignEdit plugin) {
		this.plugin = plugin;
		config = plugin.getConfig().options().configuration();
		config.options().copyDefaults(true);
		plugin.saveConfig();

		getOpts();
	}

	public void reload() {
		this.plugin.reloadConfig();
		config = this.plugin.getConfig().options().configuration();
		config.options().copyDefaults(true);
		this.plugin.saveConfig();

		getOpts();
	}

	private void getOpts() {
		ignoreCreative = config.getBoolean("signedit.ignorecreative");
		logName = config.getString("signedit.log.filename");
		invertMouse = config.getBoolean("signedit.invertmouse");
		commandsLogConsole = config.getBoolean("signedit.commands.logtoconsole");
		commandsLogFile = config.getBoolean("signedit.commands.logtofile");
		colorsOnPlace = config.getBoolean("signedit.colorsonplace.enabled");
		useCOPPerm = config.getBoolean("signedit.colorsonplace.usepermission");
		locale = config.getString("signedit.locale");
		fireBlockBreakPlace = config.getBoolean("signedit.fireBlockBreakPlace");

		if (invertMouse) {
			clickAction = Action.RIGHT_CLICK_BLOCK;
                }
                else {
			clickAction = Action.LEFT_CLICK_BLOCK;
                }
	}

	public boolean fireBlockBreakPlace() {
		return fireBlockBreakPlace;
	}

	public boolean ignoreCreative() {
		return ignoreCreative;
	}

	public Action clickAction() {
		return clickAction;
	}

	public String logName() {
		return logName;
	}

	public boolean commandsLogConsole() {
		return commandsLogConsole;
	}

	public boolean commandsLogFile() {
		return commandsLogFile;
	}

	public boolean colorsOnPlace() {
		return colorsOnPlace;
	}

	public boolean useCOPPermission() {
		return useCOPPerm;
	}

	public boolean invertMouse() {
		return invertMouse;
	}

	public String getLocale() {
		return locale;
	}

	public String clickActionStr() {
		return invertMouse ? this.plugin.localization.get("clickRight") : this.plugin.localization.get("clickLeft");
	}
}