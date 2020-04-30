package demo.verticle;

import demo.model.SessionResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.gaussian.amplifix.toolkit.Amplifix;

import static java.time.Instant.now;

public class MainVerticle extends AbstractVerticle {

    private Amplifix amplifix;

    public MainVerticle(Amplifix amplifix) {
        this.amplifix = amplifix;
    }

    @Override
    public void start(Promise<Void> startPromise) {

        Router router = Router.router(vertx);
        router.post("/payments/sessions")
              .consumes("*/json")
              .handler(this::createSession);

        vertx.createHttpServer()
             .requestHandler(router::handle)
             .listen(config().getInteger("http.port", 8082), result -> {
                 if (result.succeeded()) {
                     startPromise.complete();
                 } else {
                     startPromise.fail(result.cause());
                 }
             });
    }

    private void createSession(RoutingContext context) {
        final String payload = context.getBodyAsString();
        SessionResponse response = new SessionResponse("description", "SE", now());
        amplifix.send(response);
        context.response()
               .putHeader("content-type", "application/json")
               .setStatusCode(200)
               .end(Json.encodePrettily(response));
    }


}