package edgruberman.bukkit.statustags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

final class Bed extends Status {
    
    static final boolean DEFAULT_ENABLED = false;
    static final String DEFAULT_FORMAT = "#Bed";
    static final int DEFAULT_PRIORITY = -1;
    
    Bed(final Tag tag) {
        super(Type.BED, true, tag);
        
        new BedMonitor(Main.plugin, this);
    }
    
    void enter(final PlayerBedEnterEvent event) {
        TagManager.addTag(event.getPlayer(), this.tag);
    }
    
    void leave(final PlayerBedLeaveEvent event) {
        TagManager.removeTag(event.getPlayer(), this.tag);
    }
    
    @Override
    void enable() {
        this.enabled = true;
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            if (player.isSleeping()) TagManager.addTag(player, this.tag);
    }
    
    @Override
    void disable() {
        this.enabled = false;
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            TagManager.removeTag(player, this.tag);
    }
}