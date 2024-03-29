package edgruberman.bukkit.activity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

final class EntityActivity extends EntityListener implements ActivityMonitor {
    
    /**
     * Events this listener recognizes and can monitor player activity for.
     */
    private static final Set<Event.Type> SUPPORTS = new HashSet<Event.Type>(Arrays.asList(
              Event.Type.ENTITY_DAMAGE
            , Event.Type.ENTITY_REGAIN_HEALTH
            , Event.Type.PAINTING_PLACE
            , Event.Type.PAINTING_BREAK
            , Event.Type.PROJECTILE_HIT
    ));
    
    @Override
    public Set<Type> supports() {
        return EntityActivity.SUPPORTS;
    }
    
    @Override
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.isCancelled()) return;
        
        Player player = null;
        if (event.getEntity() instanceof Player) {
            player = (Player) event.getEntity();
            
        } else if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
            if (!(edbee.getEntity() instanceof Player)) return;
            
            player = (Player) event.getEntity();
        } else {
            return;
        }
        
        ActivityManager.updateActivity(player, event);
    }
    
    @Override
    public void onEntityRegainHealth(final EntityRegainHealthEvent event) {
        if (event.isCancelled()) return;
        
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        ActivityManager.updateActivity(player, event);
    }
    
    @Override
    public void onPaintingPlace(final PaintingPlaceEvent event) {
        if (event.isCancelled()) return;
        
        ActivityManager.updateActivity(event.getPlayer(), event);
    }
    
    @Override
    public void onPaintingBreak(final PaintingBreakEvent event) {
        if (event.isCancelled()) return;
        
        if (!(event instanceof PaintingBreakByEntityEvent)) return;
        
        PaintingBreakByEntityEvent pbbee = (PaintingBreakByEntityEvent) event;
        if (!(pbbee.getRemover() instanceof Player)) return;
        
        Player player = (Player) pbbee.getRemover();
        ActivityManager.updateActivity(player, event);
    }
    
    @Override
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        ActivityManager.updateActivity(player, event);
    }
}