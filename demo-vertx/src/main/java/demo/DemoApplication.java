package demo;

import demo.verticle.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.Amplifix;


@Slf4j
public class DemoApplication {

    public static void main(final String[] args) {
        VertxOptions options = new VertxOptions();
        Vertx vertx = Vertx.vertx(options);
        Amplifix amplifix = Amplifix.runSync();
        vertx.deployVerticle(new MainVerticle(amplifix), async -> {
                if (async.succeeded()){
                    log.info("Succesfully deployed ");
                } else {
                    log.error("Failed to deploy");
                }
            });

    }

}