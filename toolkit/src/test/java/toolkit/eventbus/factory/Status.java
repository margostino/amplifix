package toolkit.eventbus.factory;

public enum Status {

    APPROVED, REJECTED, PENDING;

    public String toString() {
        return this.name();
    }
}
