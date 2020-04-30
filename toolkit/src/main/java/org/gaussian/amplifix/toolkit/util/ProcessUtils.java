package org.gaussian.amplifix.toolkit.util;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.System.getenv;

public class ProcessUtils {

    public static String getHZHost() {
        String host = getenv("HZ_HOST");
        return isNullOrEmpty(host) ? "localhost" : host;
    }
}
