package toolkit.instrumentation.asm.allocationinstrumenter;

import com.google.monitoring.runtime.instrumentation.AllocationRecorder;
import com.google.monitoring.runtime.instrumentation.Sampler;
import toolkit.factory.domain.Session;
import toolkit.instrumentation.asm.Product;

import static java.time.Instant.now;
import static toolkit.factory.domain.Status.APPROVED;
import static toolkit.factory.domain.Status.PENDING;

/*
 Compile: javac -classpath ./bin/java-allocation-instrumenter-3.0.jar ./src/test/java/toolkit/instrumentation/asm/Foo.java
 Run: java -javaagent:./bin/java-allocation-instrumenter-3.0.jar ./build/classes/java/test/toolkit/instrumentation/asm/Foo
 VM options: -javaagent:"/Users/martin.dagostino/workspace/amplifix/toolkit/bin/java-allocation-instrumenter-3.0.jar"
*/

public class Foo {

    public static void main(String [] args) throws Exception {
        AllocationRecorder.addSampler(new Sampler() {
            public void sampleAllocation(int count, String desc, Object newObj, long size) {
                System.out.println("I just allocated the object " + newObj + " of type " + desc + " whose size is " + size);
                if (count != -1) { System.out.println("It's an array of size " + count); }
            }
        });
//        for (int i = 0 ; i < 10; i++) {
//            new String("foo");
//        }
        new Product("computer");
        Session session = new Session("mock.id", now(), APPROVED);
        session.setStatus(PENDING);
    }

}
