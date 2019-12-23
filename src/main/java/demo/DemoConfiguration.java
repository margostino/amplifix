package demo;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toolkit.configuration.MetricConfiguration;
import toolkit.eventbus.EventBusAdapter;
import toolkit.metric.CounterRegistry;
import toolkit.metric.MetricBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class DemoConfiguration {

    // Custom Tags
    @Bean("common_tags")
    public List<String> commonTags2() {
        final List<String> tags = new ArrayList<>();
        tags.add("host");
        tags.add("my_host");
        tags.add("environment");
        tags.add("dev");
        tags.add("service");
        tags.add("demo");
        return tags;
    }

    // Mandatory instance MeterRegistry
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> registryCustomizer(@Qualifier("common_tags") List<String> tags) {
        return r -> r.config().commonTags(tags.toArray(new String[0]));
    }

    // Custom Bean with Configuration
    @Bean
    public DemoService createApiService(MeterRegistry meterRegistry, @Qualifier("common_tags") List<String> tags) {
        CounterRegistry counterRegistry = new CounterRegistry(meterRegistry);
        MetricBuilder metricBuilder = new MetricBuilder("demo.amplifix");
        MetricConfiguration metricConfiguration = new MetricConfiguration(tags, metricBuilder);
        EventBusAdapter eventBus = new EventBusAdapter(counterRegistry, metricConfiguration);
        return new DemoService(eventBus);
    }

    // Custom Bean without Configuration
//    @Bean
//    public DemoService createApiService(MeterRegistry meterRegistry) {
//        CounterRegistry counterRegistry = new CounterRegistry(meterRegistry);
//        EventBusAdapter eventBus = new EventBusAdapter(counterRegistry);
//        return new DemoService(eventBus);
//    }

}
