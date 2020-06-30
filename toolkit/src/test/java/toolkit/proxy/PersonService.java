package toolkit.proxy;

import org.gaussian.amplifix.toolkit.annotation.Counter;

@Counter(metricName = "create_order", fields = {"payment_method", "status"})
public class PersonService {
    public String sayHello(String name) {
        return "Hello " + name;
    }

    public Integer lengthOfName(String name) {
        return name.length();
    }
}
