package toolkit.factory.domain;

import org.gaussian.amplifix.toolkit.annotation.Trace;

import java.time.Instant;

import static java.lang.String.format;

@Trace
public class Session {

    public String sessionId;
    public Instant createdAt;
    public Status status;

    public Session(String sessionId, Instant createdAt, Status status) {
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.status = status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void doNotMutate(Integer value) {
        System.out.println(format("The object does not mutate. Value: %s", value));
    }

}
