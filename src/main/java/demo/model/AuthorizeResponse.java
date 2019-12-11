package demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class AuthorizeResponse {

    @JsonProperty("authorization_token")
    public String authorizationToken;
    public Status status;
    @JsonProperty("authorized_payment_method")
    public String authorizedPaymentMethod;

    public AuthorizeResponse(String authorizedPaymentMethod, Status status) {
        this.authorizationToken = UUID.randomUUID().toString();
        this.status = status;
        this.authorizedPaymentMethod = authorizedPaymentMethod;
    }


}
