package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

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
