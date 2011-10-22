package edgruberman.bukkit.statustags;

import java.util.TreeSet;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

final class JoinQuitMonitor extends PlayerListener {

    JoinQuitMonitor(final Plugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvent(Event.Type.PLAYER_JOIN, this, Event.Priority.Monitor, plugin);
        pluginManager.registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Monitor, plugin);
    }
    
    @Override
    public void onPlayerJoin(final PlayerJoinEvent event) {
        TagManager.tags.put(event.getPlayer(), new TreeSet<Tag>());
    }
    
    @Override
    public void onPlayerQuit(final PlayerQuitEvent event) {
        TagManager.tags.remove(event.getPlayer());
    }
}
