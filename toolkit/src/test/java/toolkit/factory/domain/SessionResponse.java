package toolkit.factory.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gaussian.amplifix.toolkit.annotation.DropConfig;
import org.gaussian.amplifix.toolkit.annotation.Counter;
import org.gaussian.amplifix.toolkit.annotation.Drop;
import org.gaussian.amplifix.toolkit.annotation.Trace;

import java.time.Instant;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static toolkit.factory.domain.PaymentMethodCategory.PAY_LATER;
import static toolkit.factory.domain.PaymentMethodCategory.PAY_NOW;
import static toolkit.factory.domain.PaymentMethodCategory.PAY_OVER_TIME;
import static toolkit.factory.domain.Status.APPROVED;

@Trace
@Drop(metricName = "session_drop",
      tags = {"country"},
      config = @DropConfig(event = "AuthorizeResponse", field = "session_id", ttl = 30, timeUnit = SECONDS))
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
        this.sessionId = randomUUID().toString();
        this.description = description;
        this.created = created;
        this.country = country;
        this.status = APPROVED;
        this.paymentMethodCategories = asList(PAY_NOW, PAY_LATER, PAY_OVER_TIME);
    }

}

