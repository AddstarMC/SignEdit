package net.timroden.signedit.localization;

import net.timroden.signedit.SignEdit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class SignEditLocalization {
    private static final Map<String, String> strings = new HashMap<>();
    private final SignEdit plugin;
    
    public SignEditLocalization(SignEdit plugin) {
        this.plugin = plugin;
        loadLocales();
    }
    
    public final void loadLocales() {
        strings.clear();
        
        FileConfiguration locales = plugin.yml.getYMLConfig(plugin.config.getLocale(), true);
        
        if (locales != null) {
            String value;
            for (String key : locales.getKeys(false)) {
                value = locales.getString(key);
                strings.put(key, ChatColor.translateAlternateColorCodes('&', value));
            }
        }
        
        locales = plugin.yml.getYMLConfig("enUS.yml", true);
        
        if (locales != null) {
            String value;
            for (String key : locales.getKeys(false)) {
                if (!strings.containsKey(key)) {
                    value = locales.getString(key);
                    strings.put(key, ChatColor.translateAlternateColorCodes('&', value));
                }
            }
        }
    }
    
    public String get(String key) {
        return strings.get(key);
    }
    
    public String get(String key, Object... args) {
        String value = strings.get(key);
        try {
            if (value != null || args != null) value = String.format(value, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}