package org.gaussian.amplifix.toolkit.proxy;

import lombok.AllArgsConstructor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.gaussian.amplifix.toolkit.eventbus.AmplifixSender;
import org.gaussian.amplifix.toolkit.model.EventTag;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@AllArgsConstructor
public class AmplifixProxy {

    private AmplifixSender sender;

    public <T> T create(Class<T> clazz, Object... arguments) {

        List<Class> argumentTypesList = new ArrayList<>();
        for (Object argument : arguments) {
            argumentTypesList.add(argument.getClass());
        }

        Class[] argumentTypes = argumentTypesList.toArray(new Class[0]);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((MethodInterceptor) (obj, method, argz, proxy) -> {
            List<EventTag> tags = asList(new EventTag("class", obj.getClass().getCanonicalName()), new EventTag("method", method.getName()));
            sender.trace(obj, tags);
            Object response = proxy.invokeSuper(obj, argz);
            sender.trace(obj, tags);
            return response;
        });
        return clazz.cast(enhancer.create(argumentTypes, arguments));
    }

}
