package org.gaussian.amplifix.toolkit.model;

import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class TagSerializable implements Tag, Serializable {

    private final String key;
    private final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
