package demo;

import demo.exception.SessionNotFoundException;
import demo.model.AuthorizeRequest;
import demo.model.AuthorizeResponse;
import demo.model.OrderRequest;
import demo.model.OrderResponse;
import demo.model.PaymentMethodsResponse;
import demo.model.SessionRequest;
import demo.model.SessionResponse;
import toolkit.Amplifix;

import java.util.ArrayList;
import java.util.List;

import static demo.model.Status.APPROVED;
import static java.text.MessageFormat.format;
import static java.time.Instant.now;

public class DemoService {

    private Amplifix amplifix;
    private List<String> sessionDatabase; // Simulate DBs

    public DemoService(Amplifix amplifix) {
        this.amplifix = amplifix;
        this.sessionDatabase = new ArrayList<>();
    }

    public SessionResponse createSession(SessionRequest request) {
        //EventSerializer serializer = new EventSerializer();
        //eventBus.post(serializer.serialize(new Event(request)));
        SessionResponse response = new SessionResponse(request.description, request.country, now());
        sessionDatabase.add(response.sessionId);
        amplifix.post(response);
        return response;
    }

    public PaymentMethodsResponse getPaymentMethods(String sessionId) {

        if (!sessionDatabase.contains(sessionId)) {
            throw new SessionNotFoundException(format("Session {0} not found", sessionId));
        }
        return new PaymentMethodsResponse(sessionId);
    }

    public AuthorizeResponse authorize(String sessionId, AuthorizeRequest request) {
        AuthorizeResponse response = new AuthorizeResponse(request.selectedPaymentMethod, APPROVED);
        amplifix.post(response, sessionId);
        return response;
    }

    public OrderResponse createOrder(OrderRequest request) {
        return new OrderResponse(request.authorizedPaymentMethod, APPROVED);
    }

}
