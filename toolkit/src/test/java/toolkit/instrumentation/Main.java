package toolkit.instrumentation;

import com.sun.management.HotSpotDiagnosticMXBean;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import toolkit.instrumentation.proxy.Developer;
import toolkit.instrumentation.proxy.PersonService;

import javax.management.MBeanServer;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Developer.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, argz, proxy) -> {
            if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                return "Hello Tom!";
            } else {
                return proxy.invokeSuper(obj, argz);
            }
        });
        Developer developer = (Developer) enhancer.create(new Class[]{String.class}, new Object[]{"juan"});
        System.out.println(developer.getName());

        try {
            dumpHeap("dump_heap.hprof", true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void dumpHeap(String filePath, boolean live) throws IOException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        mxBean.dumpHeap(filePath, live);
    }

    public static void firstProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PersonService.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, argz, proxy) -> {
            if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                return "Hello Tom!";
            } else {
                return proxy.invokeSuper(obj, argz);
            }
        });

        PersonService proxy = (PersonService) enhancer.create();

        String res = proxy.sayHello(null);
        int lengthOfName = proxy.lengthOfName("Mary");

        String test = "Hello Tom!";

        BeanGenerator beanGenerator = new BeanGenerator();

        beanGenerator.addProperty("name", String.class);
        Object myBean = beanGenerator.create();
        try {
            Method setter = myBean.getClass().getMethod("setName", String.class);
            setter.invoke(myBean, "some string value set by a cglib");

            Method getter = myBean.getClass().getMethod("getName");
            String a = "some string value set by a cglib";
            getter.invoke(myBean);
            System.out.println("end");
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }
}
