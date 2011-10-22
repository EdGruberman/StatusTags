package edgruberman.bukkit.statustags;

abstract class Status {
    
    static enum Type { BED, IDLE, AWAY }
    
    Type type;
    boolean enabled;
    Tag tag;
    
    Status(final Type type, final boolean enabled, final Tag tag) {
        this.type = type;
        this.enabled = enabled;
        this.tag = tag;
    }
    
    abstract void enable();
    
    abstract void disable();
}
