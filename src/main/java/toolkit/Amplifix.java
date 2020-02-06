package toolkit;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.hazelcast.core.EntryListener;
import toolkit.configuration.AmplifixConfiguration;
import toolkit.datagrid.DataGridNode;
import toolkit.datagrid.DropRegistry;
import toolkit.datagrid.DropRegistryListener;
import toolkit.eventbus.ConversionEvent;
import toolkit.eventbus.EventListener;
import toolkit.metric.MetricBuilder;
import toolkit.metric.MetricSender;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class Amplifix<T, E> {

    private EventBus eventBus;
    private EventListener listener;
    private DataGridNode dataGridNode;

    /**
     * Main Toolkit Class
     *
     * @param configuration
     */
    public Amplifix(AmplifixConfiguration configuration) {
        initialize(configuration.nThreads, configuration.prefixMetric);
    }

    public Amplifix() {
        initialize(10, "amplifix");
    }

    private void initialize(int nThreads, String prefix) {
        this.eventBus = new AsyncEventBus(newFixedThreadPool(nThreads));
        this.dataGridNode = new DataGridNode();
        //this.schedulerExecutor = new SchedulerExecutor(new DataGridClient());
        this.listener = new EventListener(new MetricSender(), new MetricBuilder(prefix, dataGridNode));
        this.eventBus.register(listener);
        EntryListener mapListener = new DropRegistryListener<String, DropRegistry>(eventBus);
        this.dataGridNode.addEntryListener(mapListener);
    }

    public void post(T event) {
        eventBus.post(event);
    }

    public void post(T event, E conversionKey) {
        eventBus.post(new ConversionEvent(conversionKey.toString(), event));
    }

}
