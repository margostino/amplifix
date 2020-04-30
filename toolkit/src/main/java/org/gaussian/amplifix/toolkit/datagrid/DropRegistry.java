package org.gaussian.amplifix.toolkit.datagrid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gaussian.amplifix.toolkit.annotation.DropRegister;
import org.gaussian.amplifix.toolkit.metric.TagSerializable;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@DropRegister
public class DropRegistry implements Serializable {

    @JsonProperty("metric_name")
    public String metricName;
    public String key;
    public String value;
    public List<TagSerializable> tags;
    public String event;
    public Instant timestamp;

    @JsonCreator
    public DropRegistry(@JsonProperty("metric_name") String metricName,
                        @JsonProperty("key") String key,
                        @JsonProperty("value") String value,
                        @JsonProperty("tags") List<TagSerializable> tags,
                        @JsonProperty("event") String event,
                        @JsonProperty("timestamp") Instant timestamp) {
        this.metricName = metricName;
        this.key = key;
        this.value = value;
        this.tags = tags;
        this.event = event;
        this.timestamp = timestamp;
    }
}
