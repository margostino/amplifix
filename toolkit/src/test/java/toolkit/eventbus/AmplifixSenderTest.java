package toolkit.eventbus;

import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.gaussian.amplifix.toolkit.datagrid.DropRegistry;
import org.gaussian.amplifix.toolkit.eventbus.AmplifixEventBus;
import org.gaussian.amplifix.toolkit.eventbus.AmplifixSender;
import org.gaussian.amplifix.toolkit.eventbus.EventConsumer;
import org.gaussian.amplifix.toolkit.model.TagSerializable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import toolkit.factory.domain.AuthorizeResponse;
import toolkit.factory.domain.OrderResponse;
import toolkit.factory.domain.SessionResponse;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.MockitoAnnotations.initMocks;
import static toolkit.eventbus.MessageValidationHelper.assertValidConversionMessage;
import static toolkit.eventbus.MessageValidationHelper.assertValidDropMessage;
import static toolkit.eventbus.MessageValidationHelper.assertValidOrderResponseMessage;
import static toolkit.eventbus.MessageValidationHelper.assertValidSessionResponseMessage;
import static toolkit.eventbus.MessageValidationHelper.assertValidTraceSessionResponseMessage;
import static toolkit.factory.domain.Status.APPROVED;

@RunWith(VertxUnitRunner.class)
public class AmplifixSenderTest {

    @Rule
    public final RunTestOnContext context = new RunTestOnContext();
    @Mock
    private EventConsumer consumer;

    @Before
    public void setup() {
        clearInvocations();
        initMocks(this);
    }

    public AmplifixSender createSender(EventBus eventBus, EventConsumer consumer) {
        AmplifixEventBus amplifixEventBus = new AmplifixEventBus(eventBus);
        amplifixEventBus.setConsumer(consumer);
        return new AmplifixSender(amplifixEventBus);
    }

    @Test
    public void createSession(TestContext testContext) {
        EventBus eventBus = context.vertx().eventBus();
        eventBus.localConsumer("amplifix.events", assertValidSessionResponseMessage(testContext));
        SessionResponse sessionResponse = new SessionResponse("test session response", "SE", now());
        createSender(eventBus, consumer).send(sessionResponse);
    }

    @Test
    public void conversion(TestContext testContext) {
        EventBus eventBus = context.vertx().eventBus();
        eventBus.localConsumer("amplifix.events", assertValidConversionMessage(testContext));
        AuthorizeResponse authorizeResponse = new AuthorizeResponse("invoice", APPROVED);
        createSender(eventBus, consumer).send(authorizeResponse, "mock.session.id");
    }

    @Test
    public void drop(TestContext testContext) {
        EventBus eventBus = context.vertx().eventBus();
        eventBus.localConsumer("amplifix.events", assertValidDropMessage(testContext));
        TagSerializable tags = new TagSerializable("country", "BR");
        DropRegistry dropRegistry = new DropRegistry("session_drop", "session_id", randomUUID().toString(), asList(tags), "AuthorizeResponse", now());
        createSender(eventBus, consumer).send(dropRegistry);
    }

    @Test
    public void createOrder(TestContext testContext) {
        EventBus eventBus = context.vertx().eventBus();
        eventBus.localConsumer("amplifix.events", assertValidOrderResponseMessage(testContext));
        OrderResponse orderResponse = new OrderResponse("invoice", APPROVED);
        createSender(eventBus, consumer).send(orderResponse);
    }

    @Test
    public void traceCreateSession(TestContext testContext) {
        EventBus eventBus = context.vertx().eventBus();
        eventBus.localConsumer("amplifix.events", assertValidTraceSessionResponseMessage(testContext));
        SessionResponse sessionResponse = new SessionResponse("test session response", "SE", now());
        createSender(eventBus, consumer).trace(sessionResponse);
    }

}
