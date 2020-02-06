package toolkit.eventprocessor;

import io.micrometer.core.instrument.Meter;
import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.ConversionControl;
import toolkit.annotation.ConversionRegistry;
import toolkit.datagrid.DataGridNode;
import toolkit.eventbus.ConversionEvent;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;


@Slf4j
public class ConversionProcessor<E> extends EventProcessor<E> {

    public DataGridNode dataGridNode;

    public ConversionProcessor(String prefix, DataGridNode dataGridNode) {
        super(prefix, ConversionRegistry.class);
        this.dataGridNode = dataGridNode;
    }

    protected List<Meter> getMeters(E event) {
        ConversionEvent conversionEvent = (ConversionEvent) event;
        List<Field> fields = asList(conversionEvent.event.getClass().getFields());

        Optional<Field> fieldControl = fields.stream()
                                             .filter(field -> asList(field.getAnnotations()).stream()
                                                                                            .anyMatch(a -> a instanceof ConversionControl))
                                             .findFirst();

        if (fieldControl.isPresent()) {
            try {
                Field field = fieldControl.get();
                ConversionControl conversionControl = field.getAnnotation(ConversionControl.class);
                String eventValue = fieldControl.get().get(conversionEvent.event).toString();
                String fieldControlValue = conversionControl.value();

                if (fieldControlValue.equalsIgnoreCase(eventValue)) {
                    dataGridNode.remove(conversionEvent.conversionKey);
                }
            } catch (IllegalAccessException e) {
                // TODO
            }
        }

        return emptyList();
    }

}
