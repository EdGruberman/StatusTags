package edgruberman.bukkit.statustags;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import edgruberman.bukkit.activity.ActivityManager;
import edgruberman.bukkit.messagemanager.MessageLevel;

public final class Idle extends Status {
    
    static final boolean DEFAULT_ENABLED = false;
    static final String DEFAULT_FORMAT = "#Idle";
    static final int DEFAULT_PRIORITY = -1;
    
    static final int DEFAULT_THRESHOLD = 60; 
    
    int threshold;
    private Set<Event.Type> activity;
    private Set<String> custom;
    
    Idle(final Tag tag, final int threshold, final Set<Event.Type> activity, final Set<String> custom) {
        super(Status.Type.IDLE, true, tag);
        this.threshold = threshold;
        this.activity = activity;
        this.custom = custom;
        this.enable();
    }
    
    void update(final Player player) {
        Long last = ActivityManager.last.get(player);
        if (last == null) return;
        
        long seconds = (System.currentTimeMillis() - last) / 1000;
        if (seconds >= this.threshold) {
            TagManager.addTag(player, this.tag);
        } else {
            TagManager.removeTag(player, this.tag);
        }
    }
    
    @Override
    void enable() {
        if (this.activity == null || this.activity.size() == 0) {
            Main.messageManager.log("Unable to register idle monitor; No events specified to monitor for player activity.", MessageLevel.WARNING);
            return;
        }
        
        this.enabled = true;
        
        // Start monitor for registered player activity.
        new IdleMonitor(Main.plugin, this);
        
        // Start monitoring for activities listed in configuration.
        ActivityManager.registerEvents(this.activity, this.custom);
    }
    
    @Override
    void disable() {
        this.enabled = false;
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            TagManager.removeTag(player, this.tag);
    }
}