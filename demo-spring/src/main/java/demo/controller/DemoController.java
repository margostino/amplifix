package demo.controller;

import demo.DemoService;
import demo.model.AuthorizeRequest;
import demo.model.AuthorizeResponse;
import demo.model.OrderRequest;
import demo.model.OrderResponse;
import demo.model.PaymentMethodsResponse;
import demo.model.SessionRequest;
import demo.model.SessionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Slf4j
@Validated
@RestController
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @RequestMapping(value = "/payments/sessions",
                    method = POST,
                    consumes = APPLICATION_JSON_VALUE,
                    produces = APPLICATION_JSON_VALUE)
    public SessionResponse createSession(HttpServletRequest httpRequest,
                                         @RequestBody SessionRequest request) {
        //HttpRequestProperties httpRequestProperties = HttpRequestProperties.fromRequest(httpRequest);
        log.info("Create session");
        return demoService.createSession(request);
    }

    @RequestMapping(value = "/payments/sessions/{session_id}",
                    method = GET,
                    produces = APPLICATION_JSON_VALUE)
    public SessionResponse createSession(HttpServletRequest httpRequest,
                                         @PathVariable("session_id") @NotNull String sessionId) {
        log.info("Read session");
        return demoService.readSession(sessionId);
    }

    @RequestMapping(value = "/payments/sessions/{session_id}/payment_methods",
                    method = GET,
                    produces = APPLICATION_JSON_VALUE)
    public PaymentMethodsResponse getPaymentMethods(HttpServletRequest httpRequest,
                                                    @PathVariable("session_id") @NotNull String sessionId) {
        log.info("Get Payment Methods");
        return demoService.getPaymentMethods(sessionId);
    }

    @RequestMapping(value = "/payments/sessions/{session_id}/authorizations",
                    method = POST,
                    consumes = APPLICATION_JSON_VALUE,
                    produces = APPLICATION_JSON_VALUE)
    public AuthorizeResponse authorize(HttpServletRequest httpRequest,
                                       @PathVariable("session_id") @NotNull String sessionId,
                                       @RequestBody AuthorizeRequest request) {
        log.info("Authorize");
        return demoService.authorize(sessionId, request);
    }

    @RequestMapping(value = "/payments/authorizations/{authorization_token}/order",
                    method = POST,
                    consumes = APPLICATION_JSON_VALUE,
                    produces = APPLICATION_JSON_VALUE)
    public OrderResponse createOrder(HttpServletRequest httpRequest,
                                     @PathVariable("authorization_token") @NotNull String authorizationToken,
                                     @RequestBody OrderRequest request) {
        log.info("Create order");
        return demoService.createOrder(request);
    }

}