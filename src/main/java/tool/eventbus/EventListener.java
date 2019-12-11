package tool.eventbus;

import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tool.annotation.Counter;
import tool.metric.CounterRegistry;
import tool.metric.Metric;
import tool.metric.MetricBuilder;
import tool.metric.MetricSender;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
public class EventListener<T> {

    private Gson gson;
    private MetricBuilder metricBuilder;
    private MetricSender metricSender;

    public EventListener(CounterRegistry counterRegistry) {
        this.gson = new Gson();
        this.metricBuilder = new MetricBuilder();
        this.metricSender = new MetricSender(counterRegistry);
    }

    @Subscribe
    public void listen(T event) {
        String json = gson.toJson(event);

        Annotation[] annotations = getAnnotations(event);
        for (Annotation annotation : annotations) {
            if (annotation instanceof Counter) {
                //((Counter) annotation).key()

                try {
                    //Class clazzType = getType(event, "paymentMethodCategories");
                    Type parameterizedType = event.getClass().getField("paymentMethodCategories").getGenericType();
                    //((ParameterizedTypeImpl) parameterizedType).getActualTypeArguments()[0] Category ENUM
                    List<String> value = getValue(event, "paymentMethodCategories");
                } catch (NoSuchFieldException e) {
                    LOG.error(e.getMessage());
                } catch (IllegalAccessException e) {
                    LOG.error(e.getMessage());
                }


            }
        }

        Metric metric = metricBuilder.build(json);
        metricSender.send(metric);
    }

    private Annotation[] getAnnotations(T event) {
        return event.getClass().getAnnotations();
    }

    private List<String> getValue(T event, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return (List) event.getClass()
                           .getField(fieldName)
                           .get(event);
    }

    private Class<?> getType(T event, String fieldName) throws NoSuchFieldException {
        return event.getClass()
                    .getField(fieldName)
                    .getType();
    }
}
