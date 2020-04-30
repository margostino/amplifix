package demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Builder
@Data
public class AuthorizeRequest {

    @JsonProperty("selected_payment_method")
    public final String selectedPaymentMethod;

//    public AuthorizeRequest(PaymentMethodsResponse paymentMethods, String selectedPaymentMethod) {
//        this.sessionId = paymentMethods.sessionId;
//        this.paymentMethods = paymentMethods.paymentMethods;
//        this.selectedPaymentMethod = selectedPaymentMethod;
//    }

}
