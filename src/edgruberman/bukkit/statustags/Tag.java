package edgruberman.bukkit.statustags;

final class Tag implements Comparable<Tag> {
    
    String format;
    Integer priority;
    
    Tag(final String format, final Integer priority) {
        this.format = format;
        this.priority = priority;
    }
    
    String getFormat() {
        return this.format;
    }
    
    void setFormat(final String format) {
        this.format = format;
    }
    
    Integer getPriority() {
        return this.priority;
    }
    
    void setPriority(final Integer priority) {
        this.priority = priority;
    }
    
    @Override
    public String toString() {
        return "Format: " + this.format + "; Priority: " + this.priority;
    }

    @Override 
    public int compareTo(final Tag other) {
        if (other == null) return 1;
        if (this.priority == other.priority) return 0;
        if (other.priority == null) return 1;
        if (this.priority == null) return -1;
        return (this.priority > other.priority ? 1 : -1);
    } 
}
