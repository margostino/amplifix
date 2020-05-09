package org.gaussian.amplifix.toolkit.util;

import java.util.Random;

public class MathUtils {

    public static int getProbabilityAsInt() {
        Random random = new Random();
        return random.ints(0, 99)
                     .findFirst()
                     .getAsInt();
    }

    public static double getProbabilityAsDouble() {
        Random random = new Random();
        return random.doubles(0, 99)
                     .findFirst()
                     .getAsDouble();
    }
}
