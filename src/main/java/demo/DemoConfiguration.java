package demo;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tool.eventbus.EventBusAdapter;
import tool.metric.CounterRegistry;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class DemoConfiguration {

    @Bean("common_tags")
    public List<String> tags() {
        final String instanceId = "mock.instance";
        final List<String> tags = new ArrayList<>();
        tags.add("host");
        tags.add(!instanceId.isEmpty() ? instanceId : "empty");
        //tags.add("environment");
        //tags.add(!isNullOrEmpty(profile) ? profile : "empty");
        return tags;
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> registryCustomizer(@Qualifier("common_tags") List<String> tags) {
        return r -> r.config().commonTags(tags.toArray(new String[0]));
    }

    @Bean
    public DemoService createApiService(MeterRegistry meterRegistry) {
        CounterRegistry counterRegistry = new CounterRegistry(meterRegistry);
        EventBusAdapter eventBus = new EventBusAdapter(counterRegistry);
        return new DemoService(eventBus);
    }

}
