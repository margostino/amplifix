package org.gaussian.amplifix.toolkit.processor;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.model.EventMetadata;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;
import org.gaussian.amplifix.toolkit.model.TagSerializable;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.text.MessageFormat.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public abstract class EventProcessor {

    protected final String DEFAULT_PREFIX = "amplifix";
    protected final Class<? extends Annotation> annotationClass;

    public EventProcessor(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    abstract List<Meter> getMeters(Event event);

    protected boolean filter(EventMetadata metadata) {
        return metadata.annotations.stream()
                                   .anyMatch(annotation -> annotation.filter(annotationClass));
    }

    public List<Meter> process(Event event) {
        return filter(event.metadata) ? getMeters(event) : emptyList();
    }

    protected String decorateMetricName(String prefix, String metricName) {
        return isNullOrEmpty(prefix) ? metricName : format("{0}.{1}", prefix, metricName);
    }

    protected Optional<MetricAnnotation> lookupAnnotation(Event event) {
        return event.getAnnotations().stream()
                    .filter(annotation -> annotation.filter(annotationClass))
                    .findFirst();
    }

    protected List<Tag> getTagsForMetric(List<TagSerializable> tags) {
        return tags.stream()
                   .map(tag -> Tag.of(tag.getKey(), tag.getValue()))
                   .collect(toList());
    }
}
