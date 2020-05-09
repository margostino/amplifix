package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gaussian.amplifix.toolkit.annotation.Counter;

import java.util.List;

import static java.util.Arrays.asList;
import static org.gaussian.amplifix.toolkit.util.MathUtils.getProbabilityAsInt;

@Counter(metricName = "payment_methods", fields = {"payment_methods"})
public class PaymentMethodsResponse {

    @JsonProperty("session_id")
    public final String sessionId;
    @JsonProperty("payment_methods")
    public final List<String> paymentMethods;

    public PaymentMethodsResponse(String sessionId) {
        this.sessionId = sessionId;

        int probability = getProbabilityAsInt();
        if (probability <= 10) {
            this.paymentMethods = asList("invoice", "card", "direct_debit");
        } else if (probability <= 65) {
            this.paymentMethods = asList("invoice", "card");
        } else {
            this.paymentMethods = asList("invoice", "card", "slice_it_by_card");
        }
    }

}
