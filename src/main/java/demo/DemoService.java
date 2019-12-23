package demo;

import demo.model.AuthorizeRequest;
import demo.model.AuthorizeResponse;
import demo.model.OrderRequest;
import demo.model.OrderResponse;
import demo.model.PaymentMethodsResponse;
import demo.model.SessionRequest;
import demo.model.SessionResponse;
import toolkit.eventbus.EventBusAdapter;

import static demo.model.Status.APPROVED;
import static java.time.Instant.now;

public class DemoService {

    private EventBusAdapter eventBus;

    public DemoService(EventBusAdapter eventBus) {
        this.eventBus = eventBus;
    }

    public SessionResponse createSession(SessionRequest request) {
        //EventSerializer serializer = new EventSerializer();
        //eventBus.post(serializer.serialize(new Event(request)));
        SessionResponse response = new SessionResponse(request.description, request.country, now());
        eventBus.post(response);
        return response;
    }

    public PaymentMethodsResponse getPaymentMethods(String sessionId) {
        return new PaymentMethodsResponse(sessionId);
    }

    public AuthorizeResponse authorize(AuthorizeRequest request) {
        return new AuthorizeResponse(request.selectedPaymentMethod, APPROVED);
    }

    public OrderResponse createOrder(OrderRequest request) {
        return new OrderResponse(request.authorizedPaymentMethod, APPROVED);
    }

}
