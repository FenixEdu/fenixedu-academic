package org.fenixedu.academic.domain.accounting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

import java.text.Collator;

public class EventTemplate extends EventTemplate_Base implements Comparable<EventTemplate> {

    static {
        getRelationEventTemplateRegistrationDataByExecutionYear().addListener(new RelationAdapter<RegistrationDataByExecutionYear, EventTemplate>() {
            @Override
            public void afterAdd(final RegistrationDataByExecutionYear dataByExecutionYear, final EventTemplate eventTemplate) {
                if (dataByExecutionYear != null && eventTemplate != null) {
                    final DateTime dateTime = dataByExecutionYear.getExecutionYear().getBeginLocalDate().plusWeeks(4)
                            .toDateTimeAtStartOfDay();
                    final EventTemplateConfig templateConfig = eventTemplate.getConfigFor(dateTime);
                    if (templateConfig == null) {
                        throw new Error("No template config available for registration date: " + dateTime.minusWeeks(4)
                                .toString("yyyy-MM-dd"));
                    }
                    dataByExecutionYear.setMaxCreditsPerYear(templateConfig.getMaxCredits());
                    dataByExecutionYear.setAllowedSemesterForEnrolments(templateConfig.getSemester());

                    dataByExecutionYear.checkEnrolmentsConformToSettings();

                    templateConfig.updateEventsFor(dataByExecutionYear);
                }
            }
        });
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

    public enum Type {
        TUITION, INSURANCE, ADMIN_FEES;

        public boolean isType(final CustomEvent event) {
            final JsonElement type = event.getConfigObject().get("type");
            return type != null && !type.isJsonNull() && type.getAsString().equals(name());
        }
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

    public static EventTemplate templateFor(final RegistrationDataByExecutionYear dataByExecutionYear) {
        if (dataByExecutionYear != null) {
            final EventTemplate eventTemplate = dataByExecutionYear.getEventTemplate();
            return eventTemplate == null ? dataByExecutionYear.getRegistration().getEventTemplate() : eventTemplate;
        }
        return null;
    }

    @Atomic
    public void changePlanFor(final RegistrationDataByExecutionYear dataByExecutionYear) {
        final EventTemplate current = templateFor(dataByExecutionYear);
        if (current != this && current != null && (current.getAlternativeEventTemplateSet().contains(this)
                || getAlternativeEventTemplateSet().contains(current))) {
            dataByExecutionYear.setEventTemplate(this);
        }
    }

    @Override
    public int compareTo(final EventTemplate et) {
        final int t = Collator.getInstance().compare(getTitle().getContent(), et.getTitle().getContent());
        return t == 0 ? getExternalId().compareTo(et.getExternalId()) : t;
    }

    public void createEventsFor(final RegistrationDataByExecutionYear dataByExecutionYear) {
        final LocalDate enrolmentDate = dataByExecutionYear.getEnrolmentDate();
        if (enrolmentDate != null) {
            final DateTime enrolmentDateTime = enrolmentDate.toDateTimeAtStartOfDay();
            if (enrolmentDateTime.plusDays(12).isBeforeNow()) {
                final EventTemplateConfig templateConfig = getConfigFor(enrolmentDateTime);
                if (templateConfig != null) {
                    templateConfig.createEventsFor(dataByExecutionYear);
                }
            }
        }
    }

}
