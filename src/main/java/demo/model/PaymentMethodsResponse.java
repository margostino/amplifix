package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodsResponse {

    @JsonProperty("session_id")
    public final String sessionId;
    @JsonProperty("payment_methods")
    public final List<String> paymentMethods = new ArrayList<>();

    public PaymentMethodsResponse(String sessionId) {
        this.sessionId = sessionId;
        this.paymentMethods.add("invoice");
        this.paymentMethods.add("card");
        this.paymentMethods.add("direct_debit");
    }

}
