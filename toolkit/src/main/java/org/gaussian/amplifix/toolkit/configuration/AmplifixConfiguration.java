package org.gaussian.amplifix.toolkit.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AmplifixConfiguration {

    public List<String> commonTags;
    public String prefixMetric = "amplifix";
    public int nThreads = 10;
}
