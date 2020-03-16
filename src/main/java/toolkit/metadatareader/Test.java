package toolkit.metadatareader;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import toolkit.eventbus.Event;
import toolkit.json.JsonCodec;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Test {

    public static void main(String[] args) {
        JsonObject data = new JsonObject().put("name", "martin");
        //List<String> annotations = Arrays.asList("drop");
        //EventMetadata2 metadata = new EventMetadata2(annotations);
        //Event2 event = new Event2(metadata, data);

        JsonArray fields = new JsonArray().add("country");
        JsonObject counter = new JsonObject().put("metric_name", "counter").put("fields", fields);

        JsonObject drop = new JsonObject().put("metric_name", "drop")
                                          .put("field", "session_id")
                                          .put("event", "authorize")
                                          .put("ttl", 12)
                                          .put("time_unit", SECONDS.name())
                                          .put("tags", new JsonArray().add("country"));

//        JsonObject counter = new JsonObject().put("metric_name", "counter")
//                                             .put("fields", new JsonArray().add("paymentMethodCategories").add("country"));

        JsonObject json = new JsonObject().put("metadata", new JsonArray().add(drop).add(counter))
                                          .put("data", data);

        Event decoded = JsonCodec.decode(json.encode(), Event.class);

        System.out.println("out");
    }
}
