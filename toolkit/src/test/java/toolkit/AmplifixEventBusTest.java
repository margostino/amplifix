package toolkit;

import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.gaussian.amplifix.toolkit.eventbus.AmplifixEventBus;
import org.gaussian.amplifix.toolkit.eventbus.EventConsumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.MockitoAnnotations.initMocks;

//@RunWith(VertxUnitRunner.class)
public class AmplifixEventBusTest {

//    @Rule
//    public final RunTestOnContext context = new RunTestOnContext();
//    private AmplifixEventBus amplifixEventBus;
//    @Mock
//    private EventConsumer eventConsumer;
//
//    @Before
//    public void setup() {
//        clearInvocations();
//        initMocks(this);
//    }

//    @Test
//    public void createSession(TestContext testContext) {
//        EventBus eventBus = context.vertx().eventBus();
//        eventBus.localConsumer("amplifix.events", assertValidSessionResponseMessage(testContext));
//        SessionResponse sessionResponse = new SessionResponse("test session response", "SE", now());
//        amplifixEventBus = new AmplifixEventBus(eventBus, new MetadataReader(), eventConsumer);
//        amplifixEventBus.send(sessionResponse);
//    }
//
//    @Test
//    public void conversion(TestContext testContext) {
//        EventBus eventBus = context.vertx().eventBus();
//        eventBus.localConsumer("amplifix.events", assertValidConversionMessage(testContext));
//        AuthorizeResponse authorizeResponse = new AuthorizeResponse("invoice", APPROVED);
//        ConversionEvent conversionEvent = ConversionEvent.of("mock.session.id", mapFrom(authorizeResponse), authorizeResponse.getClass().getFields());
//        amplifixEventBus = new AmplifixEventBus(eventBus, new MetadataReader(), eventConsumer);
//        amplifixEventBus.send(conversionEvent);
//    }
//
//    @Test
//    public void drop(TestContext testContext) {
//        EventBus eventBus = context.vertx().eventBus();
//        eventBus.localConsumer("amplifix.events", assertValidDropMessage(testContext));
//        TagSerializable tags = new TagSerializable("country", "BR");
//        DropRegistry dropRegistry = new DropRegistry("session_drop", "session_id", UUID.randomUUID().toString(), asList(tags), "AuthorizeResponse", Instant.now());
//        amplifixEventBus = new AmplifixEventBus(eventBus, new MetadataReader(), eventConsumer);
//        amplifixEventBus.send(dropRegistry);
//    }

}
