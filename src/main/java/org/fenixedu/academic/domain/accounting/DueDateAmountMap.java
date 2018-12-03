package org.fenixedu.academic.domain.accounting;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.util.Money;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class DueDateAmountMap implements Map<LocalDate, Money>, Serializable {

    private final Map<LocalDate, Money> innerMap;

    private static final Type INNER_MAP_TYPE = new TypeToken<Map<LocalDate, Money>>() {
    }.getType();

    private static final long serialVersionUID = 5909963061071382186L;

    private static final DateTimeFormatter localDatePattern = DateTimeFormat.forPattern("dd/MM/yyyy");

    private static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapter(LocalDate.class,
            new TypeAdapter<LocalDate>() {
        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
            jsonWriter.value(localDate.toString(localDatePattern));
        }

        @Override
        public LocalDate read(final JsonReader jsonReader) throws IOException {
            return LocalDate.parse(jsonReader.nextString(), localDatePattern);
        }
    }).registerTypeAdapter(Money.class, new TypeAdapter<Money>() {
        @Override
        public void write(final JsonWriter jsonWriter, final Money money) throws IOException {
            jsonWriter.value(money.toPlainString());
        }

        @Override
        public Money read(final JsonReader jsonReader) throws IOException {
            return new Money(jsonReader.nextString());
        }
    }).create();

    public DueDateAmountMap(final Map<LocalDate,Money> m) {
        innerMap = new HashMap<>();
        innerMap.putAll(m);
    }

    public DueDateAmountMap(DueDateAmountMap dueDateAmountMap) {
        this(dueDateAmountMap.innerMap);
    }

    public static DueDateAmountMap fromJson(JsonElement json) {
        if (json == null || !json.isJsonObject()) {
            return null;
        }
        return new DueDateAmountMap(gson.<Map<LocalDate,Money>>fromJson(json, INNER_MAP_TYPE));
    }

    public JsonElement toJson() {
        return gson.toJsonTree(innerMap, INNER_MAP_TYPE);
    }

    @Override
    public int size() {
        return innerMap.size();
    }

    @Override
    public boolean isEmpty() {
        return innerMap.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return innerMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return innerMap.containsValue(value);
    }

    @Override
    public Money get(final Object key) {
        return innerMap.get(key);
    }

    @Override
    public Money put(final LocalDate key, final Money value) {
        return throwImmutable();
    }

    @Override
    public Money remove(final Object key) {
        return throwImmutable();
    }

    @Override
    public void putAll(final Map<? extends LocalDate, ? extends Money> m) {
        throwImmutable();
    }

    @Override
    public void clear() {
        throwImmutable();
    }

    @Override
    public Set<LocalDate> keySet() {
        return innerMap.keySet();
    }

    @Override
    public Collection<Money> values() {
        return innerMap.values();
    }

    @Override
    public Set<Entry<LocalDate, Money>> entrySet() {
        return Collections.unmodifiableSet(innerMap.entrySet());
    }

    private static <T> T throwImmutable() {
        throw new UnsupportedOperationException("This instance is immutable");
    }
}
