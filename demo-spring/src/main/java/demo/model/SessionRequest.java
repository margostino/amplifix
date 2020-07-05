package demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.gaussian.amplifix.toolkit.annotation.Trace;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Trace
@AllArgsConstructor
@JsonInclude(NON_EMPTY)
@Builder
public class SessionRequest {

    public final String country;
    public final String description;

}
