package toolkit;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.gaussian.amplifix.toolkit.Amplifix;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
}
