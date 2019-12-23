package toolkit.util;

import java.util.Random;

public class MathUtils {

    public static int getProbability() {
        Random random = new Random();
        return random.ints(0, 99)
                     .findFirst()
                     .getAsInt();
    }
}
