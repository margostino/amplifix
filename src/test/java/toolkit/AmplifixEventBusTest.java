package toolkit;

import demo.model.AuthorizeResponse;
import demo.model.SessionResponse;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import toolkit.datagrid.DropRegistry;
import toolkit.eventbus.AmplifixEventBus;
import toolkit.eventbus.ConversionEvent;
import toolkit.eventbus.EventConsumer;
import toolkit.metadatareader.MetadataReader;
import toolkit.metric.TagSerializable;

import java.time.Instant;
import java.util.UUID;

import static demo.model.Status.APPROVED;
import static io.vertx.core.json.JsonObject.mapFrom;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.MockitoAnnotations.initMocks;
import static toolkit.MessageValidationHelpers.assertValidConversionMessage;
import static toolkit.MessageValidationHelpers.assertValidDropMessage;
import static toolkit.MessageValidationHelpers.assertValidSessionResponseMessage;

@RunWith(VertxUnitRunner.class)
public class AmplifixEventBusTest {

    @Rule
    public final RunTestOnContext context = new RunTestOnContext();
    private AmplifixEventBus amplifixEventBus;
    @Mock
    private EventConsumer eventConsumer;

    @Before
    public void setup() {
        clearInvocations();
        initMocks(this);
    }

    @Test
    public void createSession(TestContext testContext) {
        EventBus eventBus = context.vertx().eventBus();
        eventBus.localConsumer("amplifix.events", assertValidSessionResponseMessage(testContext));
        SessionResponse sessionResponse = new SessionResponse("test session response", "SE", now());
        amplifixEventBus = new AmplifixEventBus(eventBus, new MetadataReader(), eventConsumer);
        amplifixEventBus.send(sessionResponse);
    }

    @Test
    public void conversion(TestContext testContext) {
        EventBus eventBus = context.vertx().eventBus();
        eventBus.localConsumer("amplifix.events", assertValidConversionMessage(testContext));
        AuthorizeResponse authorizeResponse = new AuthorizeResponse("invoice", APPROVED);
        ConversionEvent conversionEvent = ConversionEvent.of("mock.session.id", mapFrom(authorizeResponse), authorizeResponse.getClass().getFields());
        amplifixEventBus = new AmplifixEventBus(eventBus, new MetadataReader(), eventConsumer);
        amplifixEventBus.send(conversionEvent);
    }

    @Test
    public void drop(TestContext testContext) {
        EventBus eventBus = context.vertx().eventBus();
        eventBus.localConsumer("amplifix.events", assertValidDropMessage(testContext));
        TagSerializable tags = new TagSerializable("country", "BR");
        DropRegistry dropRegistry = new DropRegistry("session_drop", "session_id", UUID.randomUUID().toString(), asList(tags), "AuthorizeResponse", Instant.now());
        amplifixEventBus = new AmplifixEventBus(eventBus, new MetadataReader(), eventConsumer);
        amplifixEventBus.send(dropRegistry);
    }

}
