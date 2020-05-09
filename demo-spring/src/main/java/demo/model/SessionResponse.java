package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gaussian.amplifix.toolkit.annotation.Counter;
import org.gaussian.amplifix.toolkit.annotation.Drop;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static demo.model.PaymentMethodCategory.PAY_LATER;
import static demo.model.PaymentMethodCategory.PAY_NOW;
import static demo.model.PaymentMethodCategory.PAY_OVER_TIME;
import static demo.model.Status.APPROVED;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.gaussian.amplifix.toolkit.util.MathUtils.getProbabilityAsInt;

@Drop(metricName = "session_drop",
      field = "session_id",
      event = "AuthorizeResponse",
      ttl = 10,
      timeUnit = SECONDS,
      tags = { "country" })
@Counter(metricName = "create_session", fields = {"payment_method_categories", "country"})
public class SessionResponse {

    @JsonProperty("session_id")
    public final String sessionId;
    public final String description;
    public final Instant created;
    public final String country;
    public final Status status;
    @JsonProperty("payment_method_categories")
    public final List<PaymentMethodCategory> paymentMethodCategories;

    public SessionResponse(String description, String country, Instant created) {
        this.sessionId = UUID.randomUUID().toString();
        this.description = description;
        this.created = created;
        this.country = country;
        this.status = APPROVED;

        int probability = getProbabilityAsInt();
        if (probability <= 5) {
            this.paymentMethodCategories = asList(PAY_NOW);
        } else if (probability <= 10) {
            this.paymentMethodCategories = asList(PAY_OVER_TIME);
        } else if (probability <= 15) {
            this.paymentMethodCategories = asList(PAY_LATER);
        } else if (probability <= 50) {
            this.paymentMethodCategories = asList(PAY_NOW, PAY_LATER);
        } else {
            this.paymentMethodCategories = asList(PAY_NOW, PAY_LATER, PAY_OVER_TIME);
        }

    }

}
