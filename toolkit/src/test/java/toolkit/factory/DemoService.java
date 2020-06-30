package toolkit.factory;

import org.gaussian.amplifix.toolkit.Amplifix;
import toolkit.factory.domain.AuthorizeRequest;
import toolkit.factory.domain.AuthorizeResponse;
import toolkit.factory.domain.OrderRequest;
import toolkit.factory.domain.OrderResponse;
import toolkit.factory.domain.PaymentMethodsResponse;
import toolkit.factory.domain.SessionRequest;
import toolkit.factory.domain.SessionResponse;

import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;
import static toolkit.factory.domain.Status.APPROVED;


public class DemoService {

    private final Amplifix amplifix;
    private final List<String> sessionDatabase;

    public DemoService(Amplifix amplifix) {
        this.amplifix = amplifix;
        this.sessionDatabase = new ArrayList<>();
    }

    public SessionResponse createSession(SessionRequest request) {
        SessionResponse response = new SessionResponse(request.description, request.country, now());
        sessionDatabase.add(response.sessionId);
        amplifix.send(response);
        return response;
    }

    public PaymentMethodsResponse getPaymentMethods(String sessionId) {
        PaymentMethodsResponse response = new PaymentMethodsResponse(sessionId);
        amplifix.send(response);
        return response;
    }

    public AuthorizeResponse authorize(String sessionId, AuthorizeRequest request) {
        AuthorizeResponse response = new AuthorizeResponse(request.selectedPaymentMethod, APPROVED);
        amplifix.send(response, sessionId);
        return response;
    }

    public OrderResponse createOrder(OrderRequest request) {
        OrderResponse response = new OrderResponse(request.authorizedPaymentMethod, APPROVED);
        amplifix.send(response);
        return response;
    }

}
