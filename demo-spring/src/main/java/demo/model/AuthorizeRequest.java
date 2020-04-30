package demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Builder
public class AuthorizeRequest {

    public final String selectedPaymentMethod;

    public AuthorizeRequest(@JsonProperty("selected_payment_method") String selectedPaymentMethod) {
        this.selectedPaymentMethod = selectedPaymentMethod;
    }
}
