package toolkit.factory.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@AllArgsConstructor
@JsonInclude(NON_EMPTY)
@Builder
public class SessionRequest {

    public final String country = "ds";
    public final String description = "ds";

}
