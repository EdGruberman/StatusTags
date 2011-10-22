package edgruberman.bukkit.statustags;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

final class BedMonitor extends PlayerListener {
    
    private Bed bed;
    
    BedMonitor(final Plugin plugin, final Bed bed) {
        this.bed = bed;
        
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_BED_ENTER, this, Event.Priority.Monitor, plugin);
        pm.registerEvent(Event.Type.PLAYER_BED_LEAVE, this, Event.Priority.Monitor, plugin);
    }
    
    @Override
    public void onPlayerBedEnter(final PlayerBedEnterEvent event) {
        if (event.isCancelled()) return;
        
        this.bed.enter(event);
    }
    
    @Override
    public void onPlayerBedLeave(final PlayerBedLeaveEvent event) {
        this.bed.leave(event);
    }
}
