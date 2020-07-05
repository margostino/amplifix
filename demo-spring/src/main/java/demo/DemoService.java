package demo;

import demo.model.AuthorizeRequest;
import demo.model.AuthorizeResponse;
import demo.model.OrderRequest;
import demo.model.OrderResponse;
import demo.model.PaymentMethodsResponse;
import demo.model.SessionRequest;
import demo.model.SessionResponse;
import demo.repository.RepositoryService;
import demo.repository.Session;
import org.gaussian.amplifix.toolkit.Amplifix;

import java.util.ArrayList;
import java.util.List;

import static demo.model.Status.APPROVED;
import static demo.model.Status.PENDING;
import static java.time.Instant.now;


public class DemoService {

    private final Amplifix amplifix;
    private final List<String> sessionDatabase;
    private final TriggerStatusService triggerStatusService;
    private final RepositoryService repositoryService;

    public DemoService(Amplifix amplifix, RepositoryService repositoryService) {
        this.amplifix = amplifix;
        this.repositoryService = repositoryService;
        this.sessionDatabase = new ArrayList<>();
        this.triggerStatusService = new TriggerStatusService();
    }

    public SessionResponse createSession(SessionRequest request) {
        triggerStatusService.randomHttpStatus();
        SessionResponse response = new SessionResponse(request.description, request.country, now());
        sessionDatabase.add(response.sessionId);
        amplifix.send(response);
        amplifix.trace(request);
        return response;
    }

    public SessionResponse readSession(String sessionId) {
        triggerStatusService.randomHttpStatus();
        Session session = repositoryService.getSession(sessionId);
        session.setStatus(PENDING);
        SessionResponse response = new SessionResponse(session.description, session.country, session.createdAt);
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
        OrderResponse response = (OrderResponse) amplifix.create(OrderResponse.class, request.authorizedPaymentMethod, APPROVED);
        response.dummy();
        //amplifix.send(response);
        return response;
    }

}
