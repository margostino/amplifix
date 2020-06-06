package org.gaussian.amplifix.toolkit.eventbus;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.metric.AsyncWorker;
import org.gaussian.amplifix.toolkit.model.Event;

import java.util.List;

import static org.gaussian.amplifix.toolkit.json.JsonCodec.decode;


@Slf4j
public class EventConsumer implements Handler<Message<Object>> {

    private List<AsyncWorker> workers;

    public EventConsumer(List<AsyncWorker> workers) {
        this.workers = workers;
    }

    public void handle(Message message) {
        JsonObject body = (JsonObject) message.body();
        Event event = decode(body.encode(), Event.class);
        if (event.data.containsKey("startup")) {
            LOG.info(body.getJsonObject("data").getString("startup"));
        } else {
            workers.stream().forEach(worker -> worker.execute(event));
        }
    }
}
