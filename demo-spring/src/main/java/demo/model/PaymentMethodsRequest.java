package demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Builder
@Data
public class PaymentMethodsRequest {

    public String sessionId;

//    public PaymentMethodsRequest(String sessionId) {
//        this.sessionId = sessionId;
//    }

}
