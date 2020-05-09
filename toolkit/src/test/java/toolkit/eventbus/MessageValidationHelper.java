package toolkit.eventbus;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MessageValidationHelper {

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

            context.assertEquals(2, annotations.size());
            context.assertEquals("session_drop", drop.getString("metric_name"));
            context.assertEquals("session_id", drop.getString("field"));
            context.assertEquals("AuthorizeResponse", drop.getString("event"));
            context.assertEquals(Long.valueOf(30), drop.getLong("ttl"));
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
            context.assertEquals(3, paymentMethodCategories.size());
            context.assertTrue(paymentMethodCategories.contains("PAY_NOW"));
            context.assertTrue(paymentMethodCategories.contains("PAY_LATER"));
            context.assertTrue(paymentMethodCategories.contains("PAY_OVER_TIME"));
            async.complete();
        };
    }

    protected static Handler<Message<Object>> assertValidConversionMessage(TestContext context) {
        Async async = context.async();
        return message -> {
            JsonObject body = (JsonObject) message.body();
            JsonObject metadata = body.getJsonObject("metadata");
            JsonObject data = body.getJsonObject("data");
            JsonObject event = data.getJsonObject("event");
            JsonArray annotations = metadata.getJsonArray("annotations");
            JsonArray eventAnnotations = event.getJsonObject("metadata").getJsonArray("annotations");
            JsonArray eventFields = eventAnnotations.getJsonObject(0).getJsonArray("fields");
            JsonObject control = data.getJsonObject("control");

            context.assertEquals(1, annotations.size());
            context.assertEquals("conversion_register", annotations.getJsonObject(0).getString("id"));
            context.assertEquals("mock.session.id", control.getString("key"));
            context.assertEquals("approved", control.getJsonObject("control").getString("status"));
            context.assertEquals("APPROVED", event.getJsonObject("data").getString("status"));
            context.assertTrue(!isNullOrEmpty(event.getJsonObject("data").getString("authorization_token")));
            context.assertEquals("invoice", event.getJsonObject("data").getString("selected_payment_method"));
            context.assertEquals(1, eventAnnotations.size());
            context.assertEquals("counter", eventAnnotations.getJsonObject(0).getString("id"));
            context.assertEquals("authorize", eventAnnotations.getJsonObject(0).getString("metric_name"));
            context.assertEquals(2, eventFields.size());
            context.assertTrue(eventFields.contains("selected_payment_method"));
            context.assertTrue(eventFields.contains("status"));
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
            JsonArray tags = data.getJsonArray("tags");

            context.assertEquals(1, annotations.size());
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

    protected static Handler<Message<Object>> assertValidOrderResponseMessage(TestContext context) {
        Async async = context.async();
        return message -> {
            JsonObject body = (JsonObject) message.body();
            JsonObject metadata = body.getJsonObject("metadata");
            JsonObject data = body.getJsonObject("data");
            JsonArray annotations = metadata.getJsonArray("annotations");
            JsonObject counter = (JsonObject) annotations.stream().filter(annotation -> ((JsonObject) annotation).getString("id").equals("counter")).findFirst().get();
            JsonArray fields = counter.getJsonArray("fields");

            context.assertEquals(1, annotations.size());
            context.assertEquals("counter", counter.getString("id"));
            context.assertEquals("create_order", counter.getString("metric_name"));
            context.assertEquals(2, fields.size());
            context.assertTrue(fields.contains("payment_method"));
            context.assertTrue(fields.contains("status"));
            context.assertTrue(!isNullOrEmpty(data.getString("order_id")));
            context.assertEquals("APPROVED", data.getString("status"));
            context.assertEquals("invoice", data.getString("payment_method"));
            async.complete();
        };
    }

    protected static Handler<Message<Object>> assertValidTraceSessionResponseMessage(TestContext context) {
        Async async = context.async();
        return message -> {
            JsonObject body = (JsonObject) message.body();
            JsonObject metadata = body.getJsonObject("metadata");
            JsonObject data = body.getJsonObject("data");
            JsonObject event = data.getJsonObject("event");
            JsonArray paymentMethodCategories = event.getJsonArray("payment_method_categories");
            JsonArray annotations = metadata.getJsonArray("annotations");

            context.assertEquals(0, annotations.size());
            context.assertEquals("test session response", event.getString("description"));
            context.assertTrue(!isNullOrEmpty(event.getString("created")));
            context.assertEquals("SE", event.getString("country"));
            context.assertEquals("APPROVED", event.getString("status"));
            context.assertTrue(!isNullOrEmpty(event.getString("session_id")));
            context.assertEquals(3, paymentMethodCategories.size());
            context.assertTrue(paymentMethodCategories.contains("PAY_NOW"));
            context.assertTrue(paymentMethodCategories.contains("PAY_LATER"));
            context.assertTrue(paymentMethodCategories.contains("PAY_OVER_TIME"));
            async.complete();
        };
    }
}
