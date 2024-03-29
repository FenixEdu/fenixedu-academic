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
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class EventTemplate extends EventTemplate_Base implements Comparable<EventTemplate> {

    static {
        getRelationEventTemplateRegistrationDataByExecutionYear().addListener(new RelationAdapter<RegistrationDataByExecutionYear, EventTemplate>() {
            @Override
            public void afterAdd(final RegistrationDataByExecutionYear dataByExecutionYear, final EventTemplate eventTemplate) {
                if (dataByExecutionYear != null && dataByExecutionYear.getRegistration().getEventTemplate() != null) {
                    final DateTime dateTime = dataByExecutionYear.getExecutionYear().getBeginLocalDate().plusWeeks(4)
                            .toDateTimeAtStartOfDay();
                    final EventTemplate newEventTemplate = eventTemplate == null
                            ? dataByExecutionYear.getRegistration().getEventTemplate() : eventTemplate;
                    final EventTemplateConfig templateConfig = newEventTemplate.getConfigFor(dateTime);
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

    public EventTemplateConfig getLastConfig() {
        return getEventTemplateConfigSet().stream()
                .max(Comparator.comparing((EventTemplateConfig c) -> c.getApplyFrom())).orElse(null);
    }

    public Interval getAppliedInterval() {
        DateTime start = null;
        DateTime end = null;

        for (EventTemplateConfig config : getEventTemplateConfigSet()) {
            if (start == null || config.getApplyFrom().isBefore(start)) {
                start = config.getApplyFrom();
            }
            if (end == null || config.getApplyUntil().isAfter(end)) {
                end = config.getApplyUntil();
            }
        }

        if (start == null || end == null) {
            return null;
        }

        return new Interval(start, end);
    }

    public static EventTemplate readByCode(final String code) {
        return Bennu.getInstance().getEventTemplateSet().stream()
                .filter(t -> t.getCode().equals(code))
                .findAny().orElse(null);
    }

    public static Set<EventTemplate> getTopLevelTemplates() {
        return Bennu.getInstance().getEventTemplateSet().stream()
                .filter(t -> t.getEventTemplateFromAlternativeSet().isEmpty())
                .collect(Collectors.toSet());
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
        LocalDate enrolmentDate = dataByExecutionYear.getEnrolmentDate();
        if (enrolmentDate == null && dataByExecutionYear.getRegistration().isInMobilityState()) {
            enrolmentDate = dataByExecutionYear.getExecutionYear().getBeginLocalDate().plusDays(12);
        }
        if (enrolmentDate != null) {
            final DateTime enrolmentDateTime = enrolmentDate.toDateTimeAtStartOfDay();
//            if (enrolmentDateTime.plusDays(12).isBeforeNow()) {
                final EventTemplateConfig templateConfig = getConfigFor(enrolmentDateTime);
                if (templateConfig != null) {
                    templateConfig.createEventsFor(dataByExecutionYear);
                }
//            }
        }
    }

}
