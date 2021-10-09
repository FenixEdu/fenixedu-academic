package org.fenixedu.academic.domain.accounting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.DateTime;
import org.joda.time.Interval;

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

}
