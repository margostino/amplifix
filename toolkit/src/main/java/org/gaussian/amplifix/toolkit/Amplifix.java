package org.gaussian.amplifix.toolkit;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxJmxMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.micrometer.backends.BackendRegistries;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.eventbus.AmplifixEventBus;
import org.gaussian.amplifix.toolkit.eventbus.ConversionEvent;
import org.gaussian.amplifix.toolkit.runner.AmplifixRunner;
import org.gaussian.amplifix.toolkit.runner.SystemStatus;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

import static io.vertx.core.json.JsonObject.mapFrom;

@AllArgsConstructor
@Slf4j
public class Amplifix<T> {

    private AmplifixEventBus eventBus;

    public static Amplifix runSync() {

        // Deploy without embedded server: we need to "manually" expose the prometheus metrics
        MicrometerMetricsOptions options = new MicrometerMetricsOptions()
                .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
                .setJmxMetricsOptions(new VertxJmxMetricsOptions().setEnabled(true))
                .setEnabled(true);
        Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(options));

        Router router = Router.router(vertx);
        PrometheusMeterRegistry registry = (PrometheusMeterRegistry) BackendRegistries.getDefaultNow();
        // Setup a route for metrics
        router.route("/metrics").handler(ctx -> {
            String response = registry.scrape();
            ctx.response().end(response);
        });
        vertx.createHttpServer().requestHandler(router::handle).listen(8888);


        registry.newCounter(new Meter.Id("amplifix.vertx", Tags.of("select query", "country"), null, "query desc", Meter.Type.COUNTER));
        //registry.newCounter(new Meter.Id("amplifix.vertx", Tags.of("select query", "country"), null, "query desc", Meter.Type.COUNTER));
        //registry.newCounter(new Meter.Id("amplifix.vertx", Tags.of("select query", "country"), null, "query desc", Meter.Type.COUNTER));


        try {
            // Initialize the object
            SystemStatus systemStatus = new SystemStatus("amplifix");

            // Register the object in the MBeanServer
            MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName("com.sysdig.app:name=SystemStatusExample");
            platformMBeanServer.registerMBean(systemStatus, objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
