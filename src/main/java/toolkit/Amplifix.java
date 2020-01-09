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
import toolkit.metric.CounterRegistry;
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
     * @param counterRegistry
     * @param configuration
     */
    public Amplifix(CounterRegistry counterRegistry, AmplifixConfiguration configuration) {
        initialize(configuration.nThreads, configuration.prefixMetric, counterRegistry);
    }

    public Amplifix(CounterRegistry counterRegistry) {
        initialize(10, "amplifix", counterRegistry);
    }

    private void initialize(int nThreads, String prefix, CounterRegistry counterRegistry) {
        this.eventBus = new AsyncEventBus(newFixedThreadPool(nThreads));
        this.dataGridNode = new DataGridNode();
        //this.schedulerExecutor = new SchedulerExecutor(new DataGridClient());
        this.listener = new EventListener(new MetricSender(counterRegistry), new MetricBuilder(prefix, dataGridNode));
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
