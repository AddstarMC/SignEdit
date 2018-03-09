package net.timroden.signedit;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class YML {
    private final SignEdit plugin;
    
    private final Map<String, File> Files = new HashMap<>();
    private final Map<String, FileConfiguration> FileConfigs = new HashMap<>();
    
    public YML(SignEdit monarchy) {
        this.plugin = monarchy;
    }
    
    private static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean reloadYMLConfig(String file) {
        if (!FileConfigs.containsKey(file)) {
            FileConfigs.put(file, new YamlConfiguration());
        }
        
        try {
            Files.put(file, new File(plugin.getDataFolder(), file));
            
            if (!Files.get(file).exists()) {
                InputStream r = plugin.getResource(file);
                if (r == null)
                    return false;
                
                Files.get(file).getParentFile().mkdirs();
                copy(plugin.getResource(file), Files.get(file));
            }
            
            FileConfigs.get(file).load(Files.get(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public FileConfiguration getYMLConfig(String file, Boolean loadNewKeys) {
        if (!Files.containsKey(file)) {
            if (!reloadYMLConfig(file))
                return null;
        }
        
        if (loadNewKeys)
            copyNewKeysToDisk(file);
        
        return FileConfigs.get(file);
    }
    
    private void copyNewKeysToDisk(String fileName) {
        InputStream in = plugin.getResource(fileName);
        InputStreamReader reader = new InputStreamReader(in);
        
        YamlConfiguration locales = new YamlConfiguration();
        
        try {
            
            locales.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        
        FileConfiguration targetConfig = FileConfigs.get(fileName);
        Boolean save = false;
        String value;
        for (String key : locales.getKeys(false)) {
            value = locales.getString(key);
            
            if (targetConfig.getString(key) == null) {
                plugin.log.info("Copying new locale key [" + key + "]=[" + value + "] to " + fileName);
                
                targetConfig.set(key, value);
                save = true;
            }
            
        }
        
        if (save) {
            try {
                targetConfig.save(Files.get(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
