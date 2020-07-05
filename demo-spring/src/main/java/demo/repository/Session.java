package demo.repository;

import demo.model.Status;
import lombok.AllArgsConstructor;
import org.gaussian.amplifix.toolkit.annotation.Trace;

import java.time.Instant;

import static java.lang.String.format;

@Trace
@AllArgsConstructor
public class Session {

    public String sessionId;
    public String description;
    public String country;
    public Instant createdAt;
    public Status status;
    
    public void setStatus(Status status) {
        this.status = status;
    }

    public void doNotMutate(Integer value) {
        System.out.println(format("The object does not mutate. Value: %s", value));
    }

}