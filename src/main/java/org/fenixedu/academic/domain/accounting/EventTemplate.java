package org.fenixedu.academic.domain.accounting;

import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class EventTemplate extends EventTemplate_Base {
    
    public EventTemplate(final LocalizedString title, final LocalizedString description) {
        setBennu(Bennu.getInstance());
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

}
