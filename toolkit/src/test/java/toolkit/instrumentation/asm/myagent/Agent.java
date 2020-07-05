package toolkit.instrumentation.asm.myagent;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        ClassLogger transformer = new ClassLogger();
        instrumentation.addTransformer(transformer);
    }

}