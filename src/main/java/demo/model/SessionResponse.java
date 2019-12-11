package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import tool.annotation.Counter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static demo.model.PaymentMethodCategory.PAY_LATER;
import static demo.model.PaymentMethodCategory.PAY_NOW;
import static demo.model.Status.APPROVED;
import static java.util.Arrays.asList;

@Counter(key = "paymentMethodCategories")
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
        this.paymentMethodCategories = asList(PAY_LATER, PAY_NOW);
    }

}
