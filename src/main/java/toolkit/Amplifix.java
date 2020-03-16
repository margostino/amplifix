package toolkit;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ReplicatedMap;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.slf4j.Slf4j;
import toolkit.configuration.AmplifixConfiguration;
import toolkit.datagrid.DataGridNode;
import toolkit.datagrid.DropRegistry;
import toolkit.datagrid.DropRegistryListener;
import toolkit.eventbus.AmplifixEventBus;
import toolkit.eventbus.ConversionEvent;
import toolkit.eventbus.EventConsumer;
import toolkit.metadatareader.MetadataReader;
import toolkit.metric.MetricBuilder;
import toolkit.metric.MetricSender;
import toolkit.verticle.AmplifixVerticle;

import java.util.Set;

import static io.vertx.core.json.JsonObject.mapFrom;
import static java.text.MessageFormat.format;
import static toolkit.util.ProcessUtils.getHZHost;

@Slf4j
public class Amplifix<T, E> {

    private AmplifixEventBus eventBus;
    private final String DEFAULT_PREFIX = "amplifix";
    private final String HZ_MANCENTER_PORT = "8080";
    private final String HZ_MANCENTER_PATH = "hazelcast-mancenter";

    /**
     * Main Toolkit Class
     *
     * @param configuration
     */
    // TODO
    public Amplifix(AmplifixConfiguration configuration) {
        //initialize(configuration.prefixMetric);
    }

    public Amplifix() {

        ClusterManager mgr = createClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, res -> {
            JsonObject handshake;
            if (res.succeeded()) {
                Vertx vertx = res.result();
                handshake = new JsonObject().put("startup", "Amplifix verticle deployed successfully");
                eventBus = createEventBus(vertx);
                vertx.deployVerticle(new AmplifixVerticle());
                eventBus.send(handshake);
            } else {
                LOG.error("Amplifix verticle deployment failed: " + res.cause().getMessage());
            }
        });

    }

    private ClusterManager createClusterManager() {
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

    private AmplifixEventBus createEventBus(Vertx vertx) {
        DataGridNode dataGridNode = createDataGrid();
        EventConsumer eventConsumer = new EventConsumer(new MetricSender(), new MetricBuilder(DEFAULT_PREFIX, dataGridNode));
        AmplifixEventBus amplifixEventBus = new AmplifixEventBus(vertx.eventBus(), new MetadataReader(), eventConsumer);
        dataGridNode.addEntryListener(createEntryListener(amplifixEventBus));
        return amplifixEventBus;
    }

    private DataGridNode createDataGrid() {
        Set<HazelcastInstance> instances = Hazelcast.getAllHazelcastInstances();
        HazelcastInstance hz = instances.stream().findFirst().get();
        ReplicatedMap replicatedMap = hz.getReplicatedMap("data"); // shared distributed map
        return new DataGridNode(replicatedMap);
    }

    private EntryListener createEntryListener(AmplifixEventBus eventBus) {
        return new DropRegistryListener<String, DropRegistry>(eventBus);
    }

    public void send(T event) {
        eventBus.send(event);
    }

    public void send(T event, String conversionKey) {
        eventBus.send(ConversionEvent.of(conversionKey, mapFrom(event), event.getClass().getFields()));
    }

}
