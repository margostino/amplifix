package toolkit.datagrid;

import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;
import toolkit.annotation.DropRegistryControl;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@DropRegistryControl
@AllArgsConstructor
public class DropRegistry implements Serializable {

    public String metricName;
    public String key;
    public String value;
    public List<Tag> tags;
    public String event;
    public Instant timestamp;

}
