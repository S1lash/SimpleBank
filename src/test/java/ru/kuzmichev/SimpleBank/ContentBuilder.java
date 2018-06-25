package ru.kuzmichev.SimpleBank;

import org.jglue.fluentjson.JsonArrayBuilder;
import org.jglue.fluentjson.JsonBuilderFactory;
import org.jglue.fluentjson.JsonObjectBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;

public class ContentBuilder {
    private final PerformBuilder performBuilder;
    private final JsonObjectBuilder jsonObjectBuilder;
    private ContentBuilder parentBuilder;

    public ContentBuilder(PerformBuilder performBuilder) {
        Objects.requireNonNull(performBuilder, "performBuilder");

        this.performBuilder = performBuilder;
        this.jsonObjectBuilder = JsonBuilderFactory.buildObject();
    }

    public ContentBuilder(PerformBuilder performBuilder, ContentBuilder parentBuilder, JsonObjectBuilder jsonObjectBuilder) {
        Objects.requireNonNull(performBuilder, "performBuilder");
        Objects.requireNonNull(parentBuilder, "parentBuilder");
        Objects.requireNonNull(jsonObjectBuilder, "jsonObjectBuilder");

        this.performBuilder = performBuilder;
        this.parentBuilder = parentBuilder;
        this.jsonObjectBuilder = jsonObjectBuilder;
    }

    public ContentBuilder add(String key, Object value) {
        addObject(key, value);
        return this;
    }

    public ContentBuilder add(String key1, Object value1,
                              String key2, Object value2) {
        addObject(key1, value1);
        addObject(key2, value2);
        return this;
    }

    public ContentBuilder add(String key1, Object value1,
                              String key2, Object value2,
                              String key3, Object value3) {
        addObject(key1, value1);
        addObject(key2, value2);
        addObject(key3, value3);
        return this;
    }

    public ContentBuilder addObject(String key) {
        return new ContentBuilder(performBuilder, this, jsonObjectBuilder.addObject(key));
    }

    public ContentBuilder end() {
        jsonObjectBuilder.end();
        return parentBuilder;
    }

    public PerformBuilder build() {
        return performBuilder.withContent(jsonObjectBuilder.getJson().toString());
    }

    public ResultActions call() throws Exception {
        performBuilder.withContent(jsonObjectBuilder.getJson().toString());
        return performBuilder.call();
    }

    private void addObject(String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Number) {
            jsonObjectBuilder.add(key, (Number) value);
        } else if (value instanceof String) {
            jsonObjectBuilder.add(key, (String) value);
        } else if (value instanceof Iterable) {
            fillArray(key, (Iterable) value);
        } else {
            throw new IllegalArgumentException("Not supported type " + value.getClass());
        }
    }

    private void fillArray(String key, Iterable value) {
        JsonArrayBuilder jsonArrayBuilder = jsonObjectBuilder.addArray(key);
        for (Object o : value) {
            if (o instanceof Number) {
                jsonArrayBuilder.add((Number) o);
            } else if (o instanceof String) {
                jsonArrayBuilder.add((String) o);
            } else {
                throw new IllegalArgumentException("Not supported type in array " + o.getClass());
            }
        }
    }
}
