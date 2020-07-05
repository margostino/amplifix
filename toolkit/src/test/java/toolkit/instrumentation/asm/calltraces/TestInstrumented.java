package toolkit.instrumentation.asm.calltraces;

import io.vertx.core.Vertx;
import org.gaussian.amplifix.toolkit.json.JsonCodec;
import toolkit.factory.domain.Status;

import java.time.Instant;

import static io.vertx.core.json.JsonObject.mapFrom;
import static java.lang.String.format;

/**
 * Translate TestInstrumented into ASM API calls
 * In /Users/martin.dagostino/workspace/amplifix/toolkit/build/classes/java/test/toolkit/instrumentation/asm/calltraces/
 * java -cp .:/Users/martin.dagostino/workspace/amplifix/toolkit/bin/asm-all-5.2.jar jdk.internal.org.objectweb.asm.util.ASMifier TestInstrumented
 */


public class TestInstrumented  {
    public String sessionId;
    public Instant createdAt;
    public Status status;

    public TestInstrumented(String sessionId, Instant createdAt, Status status) {
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.status = status;
    }

    //@LogEntry(logger="global")
    public void mutate(Status status) {
        this.status = status;
        AmplifixStatic.send(this);
    }

    public void notMutate(Integer value) {
        System.out.println(format("The object does not mutate. Value: %s", value));
    }
}