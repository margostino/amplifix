package toolkit;

import io.vertx.core.Future;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import toolkit.eventbus.AmplifixEventBus;
import toolkit.eventbus.ConversionEvent;
import toolkit.runner.AmplifixRunner;

import static io.vertx.core.json.JsonObject.mapFrom;

@AllArgsConstructor
@Slf4j
public class Amplifix<T> {

    private AmplifixEventBus eventBus;

    public static Amplifix runSync() {
        return AmplifixRunner.runSync();
    }

    public static Future<Amplifix> runAsync() {
        return AmplifixRunner.runAsync();
    }

    public void send(T event) {
        eventBus.send(event);
    }

    public void send(T event, String conversionKey) {
        eventBus.send(ConversionEvent.of(conversionKey, mapFrom(event), event.getClass().getFields()));
    }

}
