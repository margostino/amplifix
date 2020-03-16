package toolkit.eventbus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import toolkit.metadatareader.MetricAnnotation;

import java.util.List;

@AllArgsConstructor
public class Event {

    public EventMetadata metadata;
    public JsonObject data;

    @JsonIgnore
    public List<MetricAnnotation> getAnnotations() {
        return metadata.annotations;
    }
}
