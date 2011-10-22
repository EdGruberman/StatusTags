package edgruberman.bukkit.statustags;

import org.bukkit.entity.Player;

public class IdleUpdate implements Runnable {
    
    private Player player;
    private Idle idle;
    
    IdleUpdate(final Player player, final Idle idle) {
        this.player = player;
        this.idle = idle;
    }
    
    @Override
    public void run() {
        if (!this.player.isOnline()) return;
        
        IdleMonitor.updateTasks.put(this.player, null);
        this.idle.update(player);
    }
}