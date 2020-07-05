package demo.repository;

import org.gaussian.amplifix.toolkit.Amplifix;

import static demo.model.Status.APPROVED;
import static java.time.Instant.now;

public class RepositoryService {

    private final Amplifix amplifix;

    public RepositoryService(Amplifix amplifix) {
        this.amplifix = amplifix;
    }

    public Session getSession(String sessionId) {
        return (Session) amplifix.create(Session.class, "mock.id", "amplifix demo session", "US", now(), APPROVED);
    }
}
