package net.timroden.signedit.utils;

import net.timroden.signedit.Config;
import net.timroden.signedit.SignEdit;
import net.timroden.signedit.data.LogType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignEditLogger {
    private final SignEdit plugin;
    private final Logger log;
    private final Config config;
    private final File logFile;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    public SignEditLogger(SignEdit plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
        this.config = plugin.config;
        
        this.logFile = new File(this.plugin.getDataFolder(), config.logName());
        if (!this.logFile.exists()) {
            try {
                if (this.logFile.createNewFile()) info("New Log file created: " + logFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void logAll(String thePlayer, String theCommand, LogType theType, Level level) {
        String theMessage = thePlayer + ": /signedit " + theCommand;
        
        if (theType.equals(LogType.PLAYERCOMMAND)) {
            theMessage = this.plugin.localization.get("playerCommand") + " " + thePlayer + ": /signedit " + theCommand;
        } else if (theType.equals(LogType.SIGNCHANGE)) {
            theMessage = this.plugin.localization.get("signChange") + " " + thePlayer + theCommand;
        }
        if (config.commandsLogFile()) {
            logFile("[" + this.dateFormat.format(new Date()) + "] " + theMessage);
        }
        if (config.commandsLogConsole()) {
            log(level, theMessage);
        }
    }
    
    private void logFile(String data) {
        try (BufferedWriter fileOut = new BufferedWriter(new FileWriter(logFile, true))) {
            fileOut.write(data);
            fileOut.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void info(String msg) {
        this.log.info(msg);
    }
    
    private void log(Level level, String msg) {
        this.log.log(level, msg);
    }
}