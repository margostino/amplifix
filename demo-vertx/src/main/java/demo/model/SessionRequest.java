package demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@AllArgsConstructor
@JsonInclude(NON_EMPTY)
@Builder
@Data
public class SessionRequest {

    public final String country;
    public final String description;

}
