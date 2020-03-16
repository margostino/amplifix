package toolkit.util;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

public class StringUtils {

    public static String toSnakeCase(String value) {
        return UPPER_CAMEL.to(LOWER_UNDERSCORE, value);
    }

}
