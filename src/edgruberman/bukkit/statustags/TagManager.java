package edgruberman.bukkit.statustags;

import java.util.HashMap;
import java.util.TreeSet;

import org.bukkit.entity.Player;

final class TagManager {
    
    static HashMap<Player, TreeSet<Tag>> tags = new HashMap<Player, TreeSet<Tag>>();
    
    static boolean addTag(final Player player, final Tag tag) {
        if (!TagManager.tags.get(player).add(tag)) return false;
        
        // No reason to refresh name if tag isn't the highest priority
        if (!TagManager.tags.get(player).last().equals(tag)) return true;
        
        TagManager.refresh(player);
        return true;
    }
    
    static boolean removeTag(final Player player, final Tag tag) {
        if (!TagManager.tags.get(player).remove(tag)) return false;
        
        // No reason to refresh name if current highest tag was higher than removed tag
        if (TagManager.tags.get(player).size() > 0 && TagManager.tags.get(player).last().compareTo(tag) == 1) return true;
        
        TagManager.refresh(player);
        return true;
    }
    
    private static void refresh(final Player player) {
        if (TagManager.tags.get(player).size() == 0) {
            player.setPlayerListName(player.getName());
            return;
        }

        Tag highest = TagManager.tags.get(player).last();
        String name = player.getName().substring(0, Math.min(16 - highest.getFormat().length(), player.getName().length()));
        String nameTag = highest.getFormat().substring(0, Math.min(16 - name.length(), highest.getFormat().length()));
        
        String space = TagManager.repeat(" ", 16 - name.length() - nameTag.length());
        
        player.setPlayerListName(name + space + nameTag);
    }
    
    /**
     * Generate a repeating string of s, n times.
     * 
     * @param s string to repeat
     * @param n number of times to repeat s
     * @return string of s repeated n times
     */
    private static String repeat(final String s, int n) {
        return new String(new char[n]).replace("\0", s);
    }
}