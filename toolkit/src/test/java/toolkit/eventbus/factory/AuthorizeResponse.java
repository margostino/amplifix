package toolkit.eventbus.factory;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gaussian.amplifix.toolkit.annotation.ConversionControl;
import org.gaussian.amplifix.toolkit.annotation.Counter;

import java.util.UUID;

@Counter(metricName = "authorize", fields = {"selected_payment_method", "status"})
public class AuthorizeResponse {

    @JsonProperty("authorization_token")
    public String authorizationToken;
    @ConversionControl(value = "approved")
    public Status status;
    @JsonProperty("selected_payment_method")
    public final String selectedPaymentMethod;

    public AuthorizeResponse(String selectedPaymentMethod, Status status) {
        this.authorizationToken = UUID.randomUUID().toString();
        this.status = status;
        this.selectedPaymentMethod = selectedPaymentMethod;
    }


}
