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
import org.gaussian.amplifix.toolkit.eventbus.AmplifixSender;
import org.gaussian.amplifix.toolkit.proxy.AmplifixProxy;
import org.gaussian.amplifix.toolkit.runner.AmplifixRunner;
import org.gaussian.amplifix.toolkit.runner.SystemStatus;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;


@AllArgsConstructor
@Slf4j
public class Amplifix<E> {

    private AmplifixSender sender;
    private AmplifixProxy proxy;

    public <T> T create(Class<T> clazz, Object... arguments) {
        return proxy.create(clazz, arguments);
    }

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

    public void send(E event) {
        sender.send(event);
    }

    public void trace(E event) {
        sender.trace(event);
    }

    public void send(E event, String conversionKey) {
        sender.send(event, conversionKey);
    }

}
