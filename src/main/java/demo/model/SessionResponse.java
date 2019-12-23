package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import toolkit.annotation.Counter;
import toolkit.annotation.Drop;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static demo.model.PaymentMethodCategory.PAY_LATER;
import static demo.model.PaymentMethodCategory.PAY_NOW;
import static demo.model.Status.APPROVED;
import static java.util.Arrays.asList;
import static toolkit.util.MathUtils.getProbability;

@Drop(metricName = "create_session_drop", field = "sessionId", event = "PaymentMethodsResponse")
@Counter(metricName = "create_session", fields = {"paymentMethodCategories", "country"})
public class SessionResponse {

    @JsonProperty("session_id")
    public final String sessionId;
    public final String description;
    public final Instant created;
    public final String country;
    public final Status status;
    @JsonProperty("payment_methods_categories")
    public final List<PaymentMethodCategory> paymentMethodCategories;

    public SessionResponse(String description, String country, Instant created) {
        this.sessionId = UUID.randomUUID().toString();
        this.description = description;
        this.created = created;
        this.country = country;
        this.status = APPROVED;

        //this.paymentMethodCategories = asList(PAY_NOW, PAY_LATER);
        int probability = getProbability();
        if (probability <= 10) {
            this.paymentMethodCategories = asList(PAY_NOW, PAY_LATER);
        } else if (probability <= 65) {
            this.paymentMethodCategories = asList(PAY_NOW);
        } else {
            this.paymentMethodCategories = asList(PAY_LATER);
        }

    }

}
