package org.gaussian.amplifix.toolkit.datagrid;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.eventbus.AmplifixEventBus;
import org.gaussian.amplifix.toolkit.eventbus.AmplifixSender;

@AllArgsConstructor
@Slf4j
public class DropRegistryListener<K, V> implements EntryListener<K, V> {

    private AmplifixSender sender;

    @Override
    public void entryEvicted(EntryEvent<K, V> event) {
        onEvictedEntryEvent(event);
    }

    @Override
    public void entryAdded(EntryEvent<K, V> event) {
        LOG.info("entryAdded missing");
    }

    @Override
    public void entryUpdated(EntryEvent<K, V> event) {
        LOG.info("entryUpdated missing");
    }

    @Override
    public void entryRemoved(EntryEvent<K, V> event) {
        LOG.info("entryRemoved missing");
    }

    @Override
    public void mapCleared(MapEvent event) {
        LOG.info("mapCleared missing");
    }

    @Override
    public void mapEvicted(MapEvent event) {
        LOG.info("mapEvicted missing");
    }

    /**
     * This method is called when an one of the methods of the {@link EntryListener} is not
     * overridden. It can be practical if you want to bundle some/all of the methods to a single method.
     *
     * @param event the EntryEvent.
     */
    public void onEvictedEntryEvent(EntryEvent<K, V> event) {
        DropRegistry dropRegistry = (DropRegistry) event.getOldValue();
        sender.send(dropRegistry);
        LOG.info("Drop Object from DataGrid");
    }
}
