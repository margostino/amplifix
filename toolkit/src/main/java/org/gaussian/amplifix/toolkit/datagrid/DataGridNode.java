package org.gaussian.amplifix.toolkit.datagrid;

import com.hazelcast.core.EntryListener;
import com.hazelcast.core.ReplicatedMap;

import java.util.concurrent.TimeUnit;

public class DataGridNode {

    private ReplicatedMap<String, DropRegistry> replicatedMap;

    public DataGridNode(ReplicatedMap<String, DropRegistry> replicatedMap) {
        this.replicatedMap = replicatedMap;

    }

    public void put(String key, DropRegistry dropRegistry, long ttl, TimeUnit timeUnit) {
        replicatedMap.put(key, dropRegistry, ttl, timeUnit);
    }

    public void remove(String key) {
        replicatedMap.remove(key);
    }

    public String addEntryListener(EntryListener mapListener) {
        return replicatedMap.addEntryListener(mapListener);
    }

}