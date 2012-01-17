package edgruberman.bukkit.statustags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.activity.ActivityManager;
import edgruberman.bukkit.messagemanager.MessageLevel;
import edgruberman.bukkit.messagemanager.MessageManager;

public final class Main extends JavaPlugin {
    
    public static MessageManager messageManager;
    
    static ConfigurationFile configurationFile;
    static Plugin plugin;
    
    @Override
    public void onLoad() {
        Main.messageManager = new MessageManager(this);
        Main.messageManager.log("Version " + this.getDescription().getVersion());
        
        Main.configurationFile = new ConfigurationFile(this);
        
        // Prepare list of supported activity monitors.
        new ActivityManager(this);
        
        Main.plugin = this;
    }
	
    @Override
    public void onEnable() {
        new JoinQuitMonitor(this);
        
        Main.loadConfiguration();
        
        Main.messageManager.log("Plugin Enabled");
    }
    
    @Override
    public void onDisable() {
        Main.messageManager.log("Plugin Disabled");
    }
    
    public static void loadConfiguration() {
        Main.configurationFile.load();
        
        Main.loadBed();
        Main.loadIdle();
    }
    
    private static void loadBed() {
        FileConfiguration cfg = Main.configurationFile.getConfig();
        if (!cfg.getBoolean("status.BED.enabled", Bed.DEFAULT_ENABLED)) return;
        
        Tag tag = new Tag(cfg.getString("status.BED.format", Bed.DEFAULT_FORMAT), cfg.getInt("status.BED.priority", Bed.DEFAULT_PRIORITY));
        Main.messageManager.log("BED Tag: " + tag.toString(), MessageLevel.CONFIG);
        
        new Bed(tag);
    }
    
    private static void loadIdle() {
        FileConfiguration cfg = Main.configurationFile.getConfig();
        if (!cfg.getBoolean("status.IDLE.enabled", Idle.DEFAULT_ENABLED)) return;
        
        Set<Event.Type> activity = new HashSet<Event.Type>();
        Set<String> custom = new HashSet<String>();
        
        for (String event : (List<String>) cfg.getStringList("status.IDLE.activity")) {
            String type = event;
            String name = null;
            
            if (event.contains(":")) {
                type = "CUSTOM_EVENT";
                name = event.substring(event.indexOf(":") + 1);
                
                if (!ActivityManager.isSupportedCustom(name)) {
                    Main.messageManager.log("Custom event not supported for monitoring activity: " + name, MessageLevel.WARNING);
                    continue;
                }
                
                custom.add(name);
            }
            
            if (ActivityManager.isSupported(Event.Type.valueOf(type))) {
                activity.add(Event.Type.valueOf(type));
            } else {
                Main.messageManager.log("Event type not supported for monitoring activity: " + type, MessageLevel.WARNING);
            }
        }
        Main.messageManager.log("IDLE Monitored Activity: " + activity.toString(), MessageLevel.CONFIG);
        if (custom.size() > 0) Main.messageManager.log("IDLE Monitored Custom Activity: " + custom.toString(), MessageLevel.CONFIG);
        
        int threshold = cfg.getInt("status.IDLE.threshold", Idle.DEFAULT_THRESHOLD);
        Main.messageManager.log("IDLE Threshold: " + threshold, MessageLevel.CONFIG);
        
        Tag tag = new Tag(cfg.getString("status.IDLE.format", Idle.DEFAULT_FORMAT), cfg.getInt("status.IDLE.priority", Idle.DEFAULT_PRIORITY));
        Main.messageManager.log("IDLE Tag: " + tag.toString(), MessageLevel.CONFIG);
        
        new Idle(tag, threshold, activity, custom);
    }
}