package toolkit;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.gaussian.amplifix.toolkit.Amplifix;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import toolkit.factory.domain.Session;
import toolkit.factory.domain.Status;

import java.time.Instant;

import static java.time.Instant.now;
import static toolkit.factory.domain.Status.APPROVED;
import static toolkit.factory.domain.Status.PENDING;

@RunWith(VertxUnitRunner.class)
public class AmplifixTest {

    private JsonObject message;

    @Before
    public void setup() {
        message = new JsonObject().put("startup", "test");
    }

    @Test
    public void sanityAsyncStartup(TestContext context) {
        Amplifix.runAsync()
                .setHandler(context.asyncAssertSuccess(amplifix -> amplifix.send(message)));
    }

    @Test
    public void sanitySyncStartup() {
        Amplifix amplifix = Amplifix.runSync();
        amplifix.send(message);
    }

    @Test
    public void proxyObject() {
        Amplifix amplifix = Amplifix.runSync();
        Session session = (Session) amplifix.create(Session.class, "mock.id", now(), APPROVED);
        session.setStatus(PENDING);
        session.doNotMutate(10);
    }

}
