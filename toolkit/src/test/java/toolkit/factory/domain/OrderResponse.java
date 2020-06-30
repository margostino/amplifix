package toolkit.factory.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gaussian.amplifix.toolkit.annotation.AsyncAction;
import org.gaussian.amplifix.toolkit.annotation.Counter;

import java.util.UUID;

@Counter(metricName = "create_order", fields = {"payment_method", "status"})
@AsyncAction(executor = "OrderSender")
public class OrderResponse {

    @JsonProperty("order_id")
    public final String orderId;
    public final Status status;
    @JsonProperty("payment_method")
    public final String paymentMethod;

    public OrderResponse(String paymentMethod, Status status) {
        this.orderId = UUID.randomUUID().toString();
        this.status = status;
        this.paymentMethod = paymentMethod;
    }


}
