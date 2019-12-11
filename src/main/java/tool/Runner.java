package tool;

import demo.DemoService;
import demo.model.AuthorizeRequest;
import demo.model.AuthorizeResponse;
import demo.model.OrderRequest;
import demo.model.OrderResponse;
import demo.model.PaymentMethodsRequest;
import demo.model.PaymentMethodsResponse;
import demo.model.SessionRequest;
import demo.model.SessionResponse;
import tool.eventbus.EventBusAdapter;


public class Runner {

    public static void main(String[] args) {

        EventBusAdapter eventBus = new EventBusAdapter(null);
        DemoService demoService = new DemoService(eventBus);

        SessionRequest sessionRequest = SessionRequest.builder()
                                                      .country("SE")
                                                      .description("test session")
                                                      .build();
        SessionResponse sessionResponse = demoService.createSession(sessionRequest);

        PaymentMethodsRequest paymentMethodsRequest = PaymentMethodsRequest.builder()
                                                                           .sessionId(sessionResponse.sessionId)
                                                                           .build();
        PaymentMethodsResponse paymentMethodsResponse = demoService.getPaymentMethods(sessionResponse.sessionId);

        AuthorizeRequest authorizeRequest = AuthorizeRequest.builder()
                                                            .selectedPaymentMethod("invoice")
                                                            .build();
        AuthorizeResponse authorizeResponse = demoService.authorize(authorizeRequest);

        OrderRequest orderRequest = OrderRequest.builder()
                                                .authorizedPaymentMethod(authorizeResponse.authorizedPaymentMethod)
                                                .build();
        OrderResponse orderResponse = demoService.createOrder(orderRequest);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

}
