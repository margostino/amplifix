package toolkit.instrumentation.asm.myagent;

// javac TestInstrumented
// java jdk.internal.org.objectweb.asm.util.ASMifier TestInstrumented

public class HelloPrinter
{
    public static void main(String[] args) {
        printOne();
        printOne();
        printTwo();
    }

    public static void printOne() {
        System.out.println("Hello World");
    }

    public static void printTwo() {
        printOne();
        printOne();
    }
}