package toolkit;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MessageValidationHelpers {

    protected static Handler<Message<Object>> assertValidSessionResponseMessage(TestContext context) {
        Async async = context.async();
        return message -> {
            JsonObject body = (JsonObject) message.body();
            JsonObject metadata = body.getJsonObject("metadata");
            JsonObject data = body.getJsonObject("data");
            JsonArray annotations = metadata.getJsonArray("annotations");
            JsonObject drop = (JsonObject) annotations.stream().filter(annotation -> ((JsonObject) annotation).getString("id").equals("drop")).findFirst().get();
            JsonObject counter = (JsonObject) annotations.stream().filter(annotation -> ((JsonObject) annotation).getString("id").equals("counter")).findFirst().get();
            JsonArray fields = counter.getJsonArray("fields");
            JsonArray paymentMethodCategories = data.getJsonArray("payment_method_categories");

            context.assertTrue(metadata.getJsonObject("control").isEmpty());
            context.assertEquals(2, annotations.size());
            context.assertEquals("session_drop", drop.getString("metric_name"));
            context.assertEquals("session_id", drop.getString("field"));
            context.assertEquals("AuthorizeResponse", drop.getString("event"));
            context.assertEquals(Long.valueOf(10), drop.getLong("ttl"));
            context.assertEquals(SECONDS.name(), drop.getString("time_unit"));
            context.assertEquals(1, drop.getJsonArray("tags").size());
            context.assertTrue(drop.getJsonArray("tags").contains("country"));
            context.assertEquals("counter", counter.getString("id"));
            context.assertEquals("create_session", counter.getString("metric_name"));
            context.assertEquals(2, fields.size());
            context.assertTrue(fields.contains("payment_method_categories"));
            context.assertTrue(fields.contains("country"));
            context.assertEquals("test session response", data.getString("description"));
            context.assertTrue(!isNullOrEmpty(data.getString("created")));
            context.assertEquals("SE", data.getString("country"));
            context.assertEquals("APPROVED", data.getString("status"));
            context.assertTrue(!isNullOrEmpty(data.getString("session_id")));
            context.assertEquals(2, paymentMethodCategories.size());
            context.assertTrue(paymentMethodCategories.contains("PAY_NOW"));
            context.assertTrue(paymentMethodCategories.contains("PAY_LATER"));
            async.complete();
        };
    }

    protected static Handler<Message<Object>> assertValidConversionMessage(TestContext context) {
        Async async = context.async();
        return message -> {
            JsonObject body = (JsonObject) message.body();
            JsonObject metadata = body.getJsonObject("metadata");
            JsonObject data = body.getJsonObject("data");
            JsonArray annotations = metadata.getJsonArray("annotations");
            JsonObject control = metadata.getJsonObject("control");

            context.assertEquals(1, annotations.size());
            context.assertEquals("conversion_register", annotations.getJsonObject(0).getString("id"));
            context.assertEquals("mock.session.id", control.getString("key"));
            context.assertEquals("approved", control.getJsonObject("control").getString("status"));
            context.assertEquals("APPROVED", data.getString("status"));
            context.assertTrue(!isNullOrEmpty(data.getString("authorization_token")));
            context.assertEquals("invoice", data.getString("selected_payment_method"));
            async.complete();
        };
    }

    protected static Handler<Message<Object>> assertValidDropMessage(TestContext context) {
        Async async = context.async();
        return message -> {
            JsonObject body = (JsonObject) message.body();
            JsonObject metadata = body.getJsonObject("metadata");
            JsonObject data = body.getJsonObject("data");
            JsonArray annotations = metadata.getJsonArray("annotations");
            JsonObject control = metadata.getJsonObject("control");
            JsonArray tags = data.getJsonArray("tags");

            context.assertEquals(1, annotations.size());
            context.assertEquals(0, control.size());
            context.assertEquals("drop_register", annotations.getJsonObject(0).getString("id"));
            context.assertEquals("session_drop", data.getString("metric_name"));
            context.assertEquals("session_id", data.getString("key"));
            context.assertTrue(!isNullOrEmpty(data.getString("value")));
            context.assertEquals("AuthorizeResponse", data.getString("event"));
            context.assertTrue(!isNullOrEmpty(data.getString("timestamp")));
            context.assertEquals(1, tags.size());
            context.assertEquals("country", tags.getJsonObject(0).getString("key"));
            context.assertEquals("BR", tags.getJsonObject(0).getString("value"));
            async.complete();
        };
    }
}
