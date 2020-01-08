package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import toolkit.annotation.ConversionControl;

import java.util.UUID;

public class AuthorizeResponse {

    @JsonProperty("authorization_token")
    public String authorizationToken;
    @ConversionControl(value = "approved")
    public Status status;
    @JsonProperty("authorized_payment_method")
    public String authorizedPaymentMethod;

    public AuthorizeResponse(String authorizedPaymentMethod, Status status) {
        this.authorizationToken = UUID.randomUUID().toString();
        this.status = status;
        this.authorizedPaymentMethod = authorizedPaymentMethod;
    }


}
