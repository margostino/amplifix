package toolkit.factory;

import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.worker.ActionExecutor;
import toolkit.factory.domain.OrderResponse;

@Slf4j
public abstract class OrderSender<I> implements ActionExecutor<I> {

    public void execute(OrderResponse orderResponse) {
        // do something async
        String result = action();
        //LOG.info(result);
    }

    private String action() {
        return "mock.action";
    }
}
