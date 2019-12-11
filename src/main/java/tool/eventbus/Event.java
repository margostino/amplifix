package tool.eventbus;

public class Event {

    public Metadata metadata;
    public String raw;

    public Event(Metadata metadata, String raw) {
        this.metadata = metadata;
        this.raw = raw;
    }

}
