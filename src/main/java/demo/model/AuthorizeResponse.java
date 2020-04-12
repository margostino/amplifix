package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import toolkit.annotation.ConversionControl;

import java.util.UUID;

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
