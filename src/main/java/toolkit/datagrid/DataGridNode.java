package toolkit.datagrid;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ReplicatedMap;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.hazelcast.core.Hazelcast.newHazelcastInstance;
import static java.lang.System.getenv;
import static java.text.MessageFormat.format;

public class DataGridNode {

    private HazelcastInstance hzInstance;
    private ReplicatedMap<String, DropRegistry> dataGrid;
    private final String HZ_MANCENTER_HOST = getHZHost();
    private final String HZ_MANCENTER_PORT = "8080";
    private final String HZ_MANCENTER_PATH = "hazelcast-mancenter";

    public DataGridNode() {
        Config config = new Config();
        NetworkConfig network = config.getNetworkConfig();
        network.setPort(5701).setPortCount(20);
        network.setPortAutoIncrement(true);
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(true);
        //join.getKubernetesConfig().setEnabled(true);

        final String hzMancenterUrl = format("http://{0}:{1}/{2}", HZ_MANCENTER_HOST, HZ_MANCENTER_PORT, HZ_MANCENTER_PATH);
        ManagementCenterConfig manCenterCfg = new ManagementCenterConfig().setEnabled(true)
                                                                          .setUrl(hzMancenterUrl);

        config.setManagementCenterConfig(manCenterCfg);
        hzInstance = newHazelcastInstance(config);
        dataGrid = hzInstance.getReplicatedMap("data");
    }

    public void put(String key, DropRegistry dropRegistry, long ttl, TimeUnit timeUnit) {
        dataGrid.put(key, dropRegistry, ttl, timeUnit);
    }

    public String addEntryListener(EntryListener mapListener) {
        return dataGrid.addEntryListener(mapListener);
    }

    private String getHZHost() {
        String host = getenv("HZ_HOST");
        return isNullOrEmpty(host) ? "localhost" : host;
    }
}