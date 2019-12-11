package demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Builder
@Data
public class SessionRequest {

    public final String country;
    public final String description;

//    public SessionRequest(String description, String country) {
//        this.country = country;
//        this.description = description;
//    }

}
