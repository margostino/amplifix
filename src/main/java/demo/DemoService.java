package demo;

import demo.model.AuthorizeRequest;
import demo.model.AuthorizeResponse;
import demo.model.OrderRequest;
import demo.model.OrderResponse;
import demo.model.PaymentMethodsResponse;
import demo.model.SessionRequest;
import demo.model.SessionResponse;
import toolkit.Amplifix;

import static demo.model.Status.APPROVED;
import static java.time.Instant.now;

public class DemoService {

    private Amplifix amplifix;

    public DemoService(Amplifix amplifix) {
        this.amplifix = amplifix;
    }

    public SessionResponse createSession(SessionRequest request) {
        //EventSerializer serializer = new EventSerializer();
        //eventBus.post(serializer.serialize(new Event(request)));
        SessionResponse response = new SessionResponse(request.description, request.country, now());
        amplifix.post(response);
        return response;
    }

    public PaymentMethodsResponse getPaymentMethods(String sessionId) {
        return new PaymentMethodsResponse(sessionId);
    }

    public AuthorizeResponse authorize(String sessionId, AuthorizeRequest request) {
        return new AuthorizeResponse(request.selectedPaymentMethod, APPROVED);
    }

    public OrderResponse createOrder(OrderRequest request) {
        return new OrderResponse(request.authorizedPaymentMethod, APPROVED);
    }

}
