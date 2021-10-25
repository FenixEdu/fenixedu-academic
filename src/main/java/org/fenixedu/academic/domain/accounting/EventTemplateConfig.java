package org.fenixedu.academic.domain.accounting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import pt.ist.fenixframework.FenixFramework;

import java.util.Map;

public class EventTemplateConfig extends EventTemplateConfig_Base {

    EventTemplateConfig(final EventTemplate eventTemplate, final DateTime applyFrom, final DateTime applyUntil,
                        final JsonObject config) {
        setEventTemplate(eventTemplate);
        setApplyFrom(applyFrom);
        setApplyUntil(applyUntil);
        setData(config.toString());
    }

    public JsonObject getConfig() {
        return new JsonParser().parse(getData()).getAsJsonObject();
    }

    public boolean contains(final DateTime when) {
        return when.isBefore(getApplyUntil()) && !when.isBefore(getApplyFrom());
    }

    public Interval whenToApply() {
        return new Interval(getApplyFrom(), getApplyUntil());
    }

    public boolean overlaps(final Interval interval) {
        return whenToApply().overlaps(interval);
    }

    public double valueFor(final EventTemplate.Type type) {
        return getConfig().getAsJsonObject(type.name()).getAsJsonObject("dueDateAmountMap").entrySet().stream()
                .map(Map.Entry::getValue)
                .mapToDouble(e -> Double.parseDouble(e.getAsString()))
                .sum();
    }

    public Double getMaxCredits() {
        final JsonElement e = getConfig().get("maxCredits");
        return e == null || e.isJsonNull() ? null : e.getAsDouble();
    }

    public ExecutionSemester getSemester() {
        final JsonElement e = getConfig().get("semester");
        return e == null || e.isJsonNull() ? null : FenixFramework.getDomainObject(e.getAsString());
    }

}
