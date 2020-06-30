package toolkit.factory.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Builder
@Data
public class OrderRequest {

    public String authorizedPaymentMethod;

    public OrderRequest(@JsonProperty("authorized_payment_method") String authorizedPaymentMethod) {
        this.authorizedPaymentMethod = authorizedPaymentMethod;
    }

}
