package toolkit.eventbus;

import io.micrometer.core.instrument.Meter;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import toolkit.metric.MetricBuilder;
import toolkit.metric.MetricSender;

import java.util.List;

import static toolkit.json.JsonCodec.decode;

@Slf4j
public class EventConsumer implements Handler<Message<Object>> {

    private MetricBuilder metricBuilder;
    private MetricSender metricSender;

    public EventConsumer(MetricSender metricSender, MetricBuilder metricBuilder) {
        this.metricBuilder = metricBuilder;
        this.metricSender = metricSender;
    }

    public void handle(Message message) {
        JsonObject body = (JsonObject) message.body();
        Event event = decode(body.encode(), Event.class);
        if (event.data.containsKey("startup")) {
            LOG.info(body.getJsonObject("data").getString("startup"));
        } else {
            List<Meter> meters = metricBuilder.build(event);
            meters.stream().forEach(metricSender::send);
        }
    }
}
