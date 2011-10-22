package edgruberman.bukkit.statustags;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import edgruberman.bukkit.activity.Activity;

final class IdleMonitor extends CustomEventListener {
    
    static Map<Player, Integer> updateTasks = new HashMap<Player, Integer>();
    private Idle idle;
    private Plugin plugin;

    IdleMonitor(final Plugin plugin, final Idle idle) {
        this.idle = idle;
        this.plugin = plugin;
        
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvent(Event.Type.CUSTOM_EVENT, this, Event.Priority.Monitor, plugin);
    }
    
    @Override
    public void onCustomEvent(final Event event) {
        if (!event.getEventName().equals(Activity.NAME)) return;
        
        Activity activity = (Activity) event;
        Integer updateTask = IdleMonitor.updateTasks.get(activity.player);
        
        if (updateTask == null) {
            this.idle.update(activity.player);
        } else {
            Bukkit.getServer().getScheduler().cancelTask(updateTask);
        }
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new IdleUpdate(activity.player, this.idle), (this.idle.threshold + 1) * 20);
    }
}
