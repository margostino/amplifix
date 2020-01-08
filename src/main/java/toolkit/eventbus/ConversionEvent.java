package toolkit.eventbus;

import lombok.AllArgsConstructor;
import toolkit.annotation.ConversionRegistry;

/**
 * This class is required to publish in event bus a conversion event to evaluate.
 * If this event is approved according the field configured, the drop metric will be dismissed.
 * @param <T>
 */
@ConversionRegistry
@AllArgsConstructor
public class ConversionEvent<T> {

    public String conversionKey;
    public T event;
}
