package toolkit.instrumentation.asm.allocationinstrumenter;

import com.google.monitoring.runtime.instrumentation.ConstructorCallback;
import com.google.monitoring.runtime.instrumentation.ConstructorInstrumenter;

import java.lang.instrument.UnmodifiableClassException;

public class Bar {

    static int count = 0;

    int x;

    Bar() {
        x = count;
    }

    public static void main(String[] args) {
        try {
            ConstructorInstrumenter.instrumentClass(
                    Bar.class, new ConstructorCallback<Bar>() {
                        @Override
                        public void sample(Bar t) {
                            System.out.println(
                                    "Constructing an element of type Test with x = " + t.x);
                            count++;
                        }
                    });
        } catch (UnmodifiableClassException e) {
            System.out.println("Class cannot be modified");
        }
        for (int i = 0; i < 10; i++) {
            new Bar();
        }
        System.out.println("Constructed " + count + " instances of Test");
    }
}
