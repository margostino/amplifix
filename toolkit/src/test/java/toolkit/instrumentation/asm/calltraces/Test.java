package toolkit.instrumentation.asm.calltraces;

public class Test {

    public static void main(String[] args) {
        printOne();
        printOne();
        printTwo();
    }

    public static void printOne() {
        System.out.println("Hello World4");
    }

    public static void printTwo() {
        printOne();
        printOne();
    }
}
