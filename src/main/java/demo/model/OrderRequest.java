package demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Builder
@Data
public class OrderRequest {

    @JsonProperty("authorized_payment_method")
    public String authorizedPaymentMethod;

//    public OrderRequest(AuthorizeResponse authorization) {
//        this.authorizationToken = authorization.authorizationToken;
//        this.paymentMethod = authorization.authorizedPaymentMethod;
//    }

}
