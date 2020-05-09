package demo;

import demo.exception.BadRequestException;
import demo.exception.ForbiddenException;
import demo.exception.GatewayTimeoutException;
import demo.exception.InternalErrorException;
import demo.exception.NotFoundException;
import demo.exception.ServiceUnavailableException;

import static java.text.MessageFormat.format;
import static org.gaussian.amplifix.toolkit.util.MathUtils.getProbabilityAsDouble;

public class TriggerStatusService {

    public void randomHttpStatus() {

        double probability = getProbabilityAsDouble();
        if (probability <= 1) {
            throw new InternalErrorException(format("Triggered internal error"));
        } else if (probability <= 1.2) {
            throw new ServiceUnavailableException(format("Triggered service unavailable"));
        } else if (probability <= 1.5) {
            throw new ForbiddenException(format("Triggered forbidden"));
        } else if (probability <= 1.6) {
            throw new GatewayTimeoutException(format("Triggered gateway timeout"));
        } else if (probability <= 3) {
            throw new BadRequestException(format("Triggered bad request"));
        } else if (probability <= 5) {
            throw new NotFoundException(format("Triggered not found"));
        }
    }

    public void mockedTrigger(String key) {
        if ("mock.session.not.found".equalsIgnoreCase(key)) {
            throw new NotFoundException(format("Session {0} not found", key));
        }
    }

}
