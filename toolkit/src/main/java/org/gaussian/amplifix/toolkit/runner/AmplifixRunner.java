package org.gaussian.amplifix.toolkit.runner;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ReplicatedMap;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.Amplifix;
import org.gaussian.amplifix.toolkit.datagrid.DataGridNode;
import org.gaussian.amplifix.toolkit.datagrid.DropRegistry;
import org.gaussian.amplifix.toolkit.datagrid.DropRegistryListener;
import org.gaussian.amplifix.toolkit.eventbus.AmplifixEventBus;
import org.gaussian.amplifix.toolkit.eventbus.AmplifixSender;
import org.gaussian.amplifix.toolkit.eventbus.EventConsumer;
import org.gaussian.amplifix.toolkit.processor.ConversionProcessor;
import org.gaussian.amplifix.toolkit.processor.CounterProcessor;
import org.gaussian.amplifix.toolkit.processor.DropProcessor;
import org.gaussian.amplifix.toolkit.processor.EventProcessor;
import org.gaussian.amplifix.toolkit.processor.RegisterProcessor;
import org.gaussian.amplifix.toolkit.processor.TraceProcessor;
import org.gaussian.amplifix.toolkit.proxy.AmplifixProxy;
import org.gaussian.amplifix.toolkit.verticle.AmplifixVerticle;
import org.gaussian.amplifix.toolkit.worker.AsyncWorker;
import org.gaussian.amplifix.toolkit.worker.MetricWorker;
import org.gaussian.amplifix.toolkit.worker.TraceWorker;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.gaussian.amplifix.toolkit.util.ProcessUtils.getHZHost;


@Slf4j
public class AmplifixRunner {

    private static final String HZ_MANCENTER_PORT = "8080";
    private static final String HZ_MANCENTER_PATH = "hazelcast-mancenter";

    public static Amplifix runSync() {
        Future<Amplifix> amplifixFuture = runAsync();

        // This will block the application startup until the Verticle deployment is completed or failed
        while (!amplifixFuture.isComplete()) {
            if (amplifixFuture.failed()) {
                break;
            }
        }

        if (amplifixFuture.succeeded()) {
            return amplifixFuture.result();
        } else {
            LOG.error(amplifixFuture.cause().getMessage());
            return new Amplifix(null, null);
        }

    }

    public static Future<Amplifix> runAsync() {
        return createAsyncVertxCluster();
    }

    private static Future<Amplifix> createAsyncVertxCluster() {
        Instant start = Instant.now();
        Promise<Amplifix> startup = Promise.promise();
        Vertx.clusteredVertx(getVertxClusterOptions(), async -> {
            if (async.succeeded()) {
                Duration duration = Duration.between(start, Instant.now());
                LOG.info("Amplifix verticle deployed in {} seconds", duration.getSeconds());
                AmplifixSender amplifixSender = deployVertxVerticle(async.result());
                AmplifixProxy amplifixProxy = new AmplifixProxy(amplifixSender);
                startup.complete(new Amplifix(amplifixSender, amplifixProxy));
            } else {
                LOG.error("Amplifix verticle deployment failed: " + async.cause().getMessage());
                startup.fail(async.cause());
            }
        });
        return startup.future();
    }

    private static VertxOptions getVertxClusterOptions() {
        ClusterManager mgr = createHazelcastClusterManager();
        return new VertxOptions().setClusterManager(mgr);
    }

    private static ClusterManager createHazelcastClusterManager() {
        Config hazelcastConfig = new Config();
        NetworkConfig network = hazelcastConfig.getNetworkConfig();
        network.setPort(5701).setPortCount(20);
        network.setPortAutoIncrement(true);
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(true);
        //join.getKubernetesConfig().setEnabled(true);
        final String hzMancenterUrl = format("http://{0}:{1}/{2}", getHZHost(), HZ_MANCENTER_PORT, HZ_MANCENTER_PATH);
        ManagementCenterConfig manCenterCfg = new ManagementCenterConfig().setEnabled(true)
                                                                          .setUrl(hzMancenterUrl);
        hazelcastConfig.setManagementCenterConfig(manCenterCfg);
        return new HazelcastClusterManager(hazelcastConfig);
    }

    private static AmplifixSender deployVertxVerticle(Vertx vertx) {
        JsonObject handshake = new JsonObject().put("startup", "Amplifix handshake successfully");
        AmplifixEventBus amplifixEventBus = createEventBus(vertx);
        vertx.deployVerticle(new AmplifixVerticle());
        AmplifixSender amplifixSender = new AmplifixSender(amplifixEventBus);
        EventConsumer eventConsumer = createEventConsumer(amplifixSender);
        amplifixEventBus.setConsumer(eventConsumer);
        amplifixSender.send(handshake);
        return amplifixSender;
    }

    private static AmplifixEventBus createEventBus(Vertx vertx) {
        AmplifixEventBus amplifixEventBus = new AmplifixEventBus(vertx.eventBus());
        return amplifixEventBus;
    }

    private static EventConsumer createEventConsumer(AmplifixSender sender) {
        DataGridNode dataGridNode = createDataGrid();
        dataGridNode.addEntryListener(createEntryListener(sender));
        List<EventProcessor> metricProcessors = getMetricProcessor(dataGridNode);
        List<EventProcessor> traceProcessors = asList(new TraceProcessor());
        List<AsyncWorker> workers = asList(new MetricWorker(metricProcessors), new TraceWorker(traceProcessors));
        return new EventConsumer(workers);
    }

    private static DataGridNode createDataGrid() {
        Set<HazelcastInstance> instances = Hazelcast.getAllHazelcastInstances();
        HazelcastInstance hz = instances.stream().findFirst().get();
        ReplicatedMap replicatedMap = hz.getReplicatedMap("data"); // shared distributed map
        return new DataGridNode(replicatedMap);
    }

    private static EntryListener createEntryListener(AmplifixSender sender) {
        return new DropRegistryListener<String, DropRegistry>(sender);
    }

    private static List<EventProcessor> getMetricProcessor(DataGridNode dataGridNode) {
        List<EventProcessor> processors = new ArrayList<>();
        processors.add(new CounterProcessor());
        processors.add(new RegisterProcessor(dataGridNode));
        processors.add(new ConversionProcessor(dataGridNode));
        processors.add(new DropProcessor());
        return processors;
    }
}
