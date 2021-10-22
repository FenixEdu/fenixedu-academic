package org.fenixedu.academic.domain.accounting;

import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class EventTemplate extends EventTemplate_Base {

    public enum Type {
        TUITION, INSURANCE, ADMIN_FEES
    }

    public EventTemplate(final String code, final LocalizedString title, final LocalizedString description) {
        if (Bennu.getInstance().getEventTemplateSet().stream().anyMatch(t -> t.getCode().equals(code))) {
            throw new Error("Duplicate event template for code " + code);
        }
        setBennu(Bennu.getInstance());
        setCode(code);
        setTitle(title);
        setDescription(description);
    }

    public EventTemplateConfig createConfig(final DateTime applyFrom, final DateTime applyUntil,
                                            final JsonObject config) {
        final Interval interval = new Interval(applyFrom, applyUntil);
        if (getEventTemplateConfigSet().stream().anyMatch(etc -> etc.overlaps(interval))) {
            throw new Error("Overlaps with other template config");
        }
        return new EventTemplateConfig(this, applyFrom, applyUntil, config);
    }

    public EventTemplateConfig getConfigFor(final DateTime when) {
        return getEventTemplateConfigSet().stream()
                .filter(config -> config.contains(when))
                .findAny().orElse(null);
    }

    public static EventTemplate readByCode(final String code) {
        return Bennu.getInstance().getEventTemplateSet().stream()
                .filter(t -> t.getCode().equals(code))
                .findAny().orElse(null);
    }

}
