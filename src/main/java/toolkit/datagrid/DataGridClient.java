package toolkit.datagrid;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import static com.hazelcast.client.HazelcastClient.newHazelcastClient;

public class DataGridClient {

    private HazelcastInstance hzClient;

    public DataGridClient() {
        ClientConfig config = new ClientConfig();
//        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
//        clientNetworkConfig.addOutboundPort(5700);
        //clientNetworkConfig.addAddress("localhost:5700");
//        config.setNetworkConfig(clientNetworkConfig);
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("dev");
        groupConfig.setPassword("dev-pass");
        hzClient = newHazelcastClient(config);
    }

    public IMap getMap(String key) {
        return hzClient.getMap(key);
    }
}

//class Main {
//    public static void main(String[] args) {
//        DataGridClient client = new DataGridClient();
//        System.out.println("Test");
//    }
//}