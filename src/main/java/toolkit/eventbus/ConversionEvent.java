package toolkit.eventbus;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import toolkit.annotation.ConversionControl;
import toolkit.annotation.ConversionRegister;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

/**
 * This class is required to publish in event bus a conversion event to evaluate.
 * If this event is approved according the field configured, the drop metric will be dismissed.
 */
@ConversionRegister
@AllArgsConstructor
public class ConversionEvent {

    public String key;
    public Map<String, String> control;
    public JsonObject event;

    public static ConversionEvent of(String key, JsonObject event, Field[] fields) {
        Map<String, String> control = asList(fields).stream()
                                                    .filter(ConversionEvent::isControl)
                                                    .map(ConversionEvent::getControl)
                                                    .collect(toMap(kv -> kv.get("key"), kv -> kv.get("value")));

        return new ConversionEvent(key, control, event);
    }

    private static Map<String, String> getControl(Field field) {

        Map<String, String> control = new HashMap<>();
        Optional<Annotation> annotation = asList(field.getAnnotations()).stream()
                                                                        .filter(ConversionEvent::filterControl)
                                                                        .findFirst();
        if (annotation.isPresent()) {
            ConversionControl conversionControl = (ConversionControl) annotation.get();
            control.put("key", field.getName());
            control.put("value", conversionControl.value());
        }

        // TODO: validate if annotation is not present
        return control;
    }

    private static boolean isControl(Field field) {
        return asList(field.getAnnotations()).stream()
                                             .anyMatch(ConversionEvent::filterControl);
    }

    private static boolean filterControl(Annotation annotation) {
        return annotation.annotationType() == ConversionControl.class;
    }
}
