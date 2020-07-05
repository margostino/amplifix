package toolkit;

import com.google.monitoring.runtime.instrumentation.AllocationRecorder;
import com.google.monitoring.runtime.instrumentation.Sampler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.gaussian.amplifix.toolkit.Amplifix;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import toolkit.factory.domain.Session;

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

    @Test
    public void allocationInstrumenter() {
        AllocationRecorder.addSampler(new Sampler() {
            public void sampleAllocation(int count, String desc, Object newObj, long size) {
                System.out.println("I just allocated the object " + newObj + " of type " + desc + " whose size is " + size);
                if (count != -1) {
                    System.out.println("It's an array of size " + count);
                }
            }
        });
        Amplifix amplifix = Amplifix.runSync();
        Session session = new Session("mock.id", now(), APPROVED);
        session.setStatus(PENDING);
        amplifix.send(message);

    }
}
