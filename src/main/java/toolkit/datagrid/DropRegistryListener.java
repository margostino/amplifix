package toolkit.datagrid;

import com.google.common.eventbus.EventBus;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DropRegistryListener<K, V> implements EntryListener<K, V> {

    private EventBus eventBus;

    public DropRegistryListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

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
     * This method is called when an one of the methods of the {@link com.hazelcast.core.EntryListener} is not
     * overridden. It can be practical if you want to bundle some/all of the methods to a single method.
     *
     * @param event the EntryEvent.
     */
    public void onEvictedEntryEvent(EntryEvent<K, V> event) {
        DropRegistry dropRegistry = (DropRegistry) event.getOldValue();
        eventBus.post(dropRegistry);
        LOG.info("Drop Object from DataGrid");
    }
}
