package demo;

import demo.model.AuthorizeRequest;
import demo.model.AuthorizeResponse;
import demo.model.OrderRequest;
import demo.model.OrderResponse;
import demo.model.PaymentMethodsResponse;
import demo.model.SessionRequest;
import demo.model.SessionResponse;
import org.gaussian.amplifix.toolkit.Amplifix;

import java.util.ArrayList;
import java.util.List;

import static demo.model.Status.APPROVED;
import static java.time.Instant.now;


public class DemoService {

    private final Amplifix amplifix;
    private final List<String> sessionDatabase;
    private final TriggerStatusService triggerStatusService;

    public DemoService(Amplifix amplifix, TriggerStatusService triggerStatusService) {
        this.amplifix = amplifix;
        this.sessionDatabase = new ArrayList<>();
        this.triggerStatusService = triggerStatusService;
    }

    public SessionResponse createSession(SessionRequest request) {
        triggerStatusService.randomHttpStatus();
        SessionResponse response = new SessionResponse(request.description, request.country, now());
        sessionDatabase.add(response.sessionId);
        amplifix.send(response);
        return response;
    }

    public PaymentMethodsResponse getPaymentMethods(String sessionId) {
        triggerStatusService.mockedTrigger(sessionId);
        triggerStatusService.randomHttpStatus();
        PaymentMethodsResponse response = new PaymentMethodsResponse(sessionId);
        amplifix.send(response);
        return response;
    }

    public AuthorizeResponse authorize(String sessionId, AuthorizeRequest request) {
        triggerStatusService.randomHttpStatus();
        AuthorizeResponse response = new AuthorizeResponse(request.selectedPaymentMethod, APPROVED);
        amplifix.send(response, sessionId);
        return response;
    }

    public OrderResponse createOrder(OrderRequest request) {
        triggerStatusService.randomHttpStatus();
        OrderResponse response = new OrderResponse(request.authorizedPaymentMethod, APPROVED);
        amplifix.send(response);
        return response;
    }

}
