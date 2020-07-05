package toolkit.instrumentation.asm.calltraces;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

import static io.vertx.core.json.JsonObject.mapFrom;

public class AmplifixStatic {

    public static EventBus eventBus = Vertx.vertx().eventBus();
    private static String DEFAULT_ADDRESS = "amplifix.events";
    private static MessageConsumer consumer = eventBus.consumer(DEFAULT_ADDRESS, AmplifixStatic::handle);

    public static void send(Object object) {
        eventBus.send(DEFAULT_ADDRESS, mapFrom(object));
    }

    public static void handle(Message message) {
        System.out.println("do something");
    }

}
