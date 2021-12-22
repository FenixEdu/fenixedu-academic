package org.fenixedu.academic.domain.accounting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.accounting.events.EventExemption;
import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.json.JsonUtils;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import pt.ist.fenixframework.FenixFramework;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public double valueFor(final double ectsCredits, final EventTemplate.Type type) {
        final JsonObject config = getConfig().getAsJsonObject(type.name());
        final JsonObject byECTS = config.getAsJsonObject("byECTS");
        return new Money(byECTS.get("value").getAsString()).multiply(new BigDecimal(ectsCredits)).getAmount().doubleValue();
    }

    public Double getMaxCredits() {
        final JsonElement e = getConfig().get("maxCredits");
        return e == null || e.isJsonNull() ? null : e.getAsDouble();
    }

    public ExecutionSemester getSemester() {
        final JsonElement e = getConfig().get("semester");
        return e == null || e.isJsonNull() ? null : FenixFramework.getDomainObject(e.getAsString());
    }

    public void createEventsFor(final RegistrationDataByExecutionYear dataByExecutionYear) {
        Arrays.stream(EventTemplate.Type.values())
                .forEach(type -> createEventsFor(dataByExecutionYear, type));
    }

    private void createEventsFor(final RegistrationDataByExecutionYear dataByExecutionYear, final EventTemplate.Type type) {
        final Registration registration = dataByExecutionYear.getRegistration();
        final ExecutionYear executionYear = dataByExecutionYear.getExecutionYear();
        if (type != EventTemplate.Type.TUITION || !registration.getDegree().isEmpty()) {
            eventsFor(dataByExecutionYear, type).findAny().orElseGet(() -> createEvent(dataByExecutionYear, type));
        } else {
            registration.getStudentCurricularPlansSet().stream()
                    .flatMap(scp -> scp.getEnrolmentStream())
                    .filter(enrolment -> enrolment.getExecutionYear() == executionYear)
                    .filter(enrolment -> !enrolment.isAnnulled())
                    .forEach(enrolment -> {
                        eventsFor(enrolment, type).findAny().orElseGet(() -> createEvent(enrolment, type));
                    });
        }
    }

    private CustomEvent createEvent(final EventTemplate.Type type, final Registration registration,
                                    final LocalizedString description, final Map<LocalDate, Money> dueDateAmountMap,
                                    final Consumer<JsonObject> eventConfigConsumer) {
        final JsonObject config = getConfig().getAsJsonObject(type.name());
        final Person person = registration.getPerson();
        final org.fenixedu.academic.domain.accounting.Account account = FenixFramework.getDomainObject(config.get("accountId").getAsString());
        return new CustomEvent(person, account, dueDateAmountMap, JsonUtils.toJson(data -> {
            data.add("description", description.json());
            data.addProperty("type", type.name());
            final JsonObject penaltyAmountMap = config.getAsJsonObject("penaltyAmountMap");
            if (penaltyAmountMap != null) {
                data.add("penaltyAmountMap", penaltyAmountMap);
            }
            data.add("productCode", config.get("productCode"));
            data.add("productDescription", config.get("productDescription"));

            eventConfigConsumer.accept(data);
        }));
    }

    private Map<LocalDate, Money> toDueDateAmountMap(final JsonObject json) {
        final Map<LocalDate, Money> dueDateAmountMap = new TreeMap<>();
        final Money[] values = new Money[] { Money.ZERO, Money.ZERO };
        json.entrySet().forEach(e -> {
            final LocalDate date = DateTimeFormat.forPattern("dd/MM/yyyy").parseLocalDate(e.getKey());
            final Money value = new Money(e.getValue().getAsString());
            dueDateAmountMap.put(date, value);
            values[0] = values[0].add(value);
            if (date.toDateTimeAtStartOfDay().isBeforeNow()) {
                values[1] = values[1].add(value);
            }
        });
        if (values[0].equals(values[1])) {
            // all dates have passed... don't do anything else. Event will be created with fines
        } else if (values[1].isZero()) {
            // all is ok... nothing needs to be done.
        } else {
            dueDateAmountMap.keySet().stream()
                    .filter(date -> date.toDateTimeAtStartOfDay().isBeforeNow())
                    .collect(Collectors.toSet())
                    .forEach(date -> dueDateAmountMap.remove(date));
            final int count = dueDateAmountMap.size();
            if (count == 0) {
                throw new Error("Should never happen... problem calculating adjusted due date amount map.");
            }
            final Money valueToDistribute = values[1];
            final Money unitDistribution = valueToDistribute.divide(new BigDecimal(count));
            dueDateAmountMap.replaceAll((date, value) -> value.add(unitDistribution));
            final Money diff = valueToDistribute.subtract(unitDistribution.multiply(count));
            if (!diff.isZero()) {
                final LocalDate localDate = dueDateAmountMap.keySet().iterator().next();
                dueDateAmountMap.put(localDate, dueDateAmountMap.get(localDate).add(diff));
            }
        }
        return dueDateAmountMap;
    }

    private static Stream<CustomEvent> eventsFor(final RegistrationDataByExecutionYear dataByExecutionYear, final EventTemplate.Type type) {
        return eventsFor(dataByExecutionYear.getRegistration(), type)
                .filter(event -> dataByExecutionYear.getExecutionYear() == executionYearFor(event))
                .filter(event -> type != EventTemplate.Type.TUITION || dataByExecutionYear == registrationDataByExecutionYearFor(event));
    }

    private static Stream<CustomEvent> eventsFor(final Enrolment enrolment, final EventTemplate.Type type) {
        return eventsFor(enrolment.getRegistration(), type)
                .filter(event -> enrolment.getExecutionPeriod() == executionSemester(event))
                .filter(event -> enrolment.getCurricularCourse() == curricularCourseFor(event));
    }

    private static Stream<CustomEvent> eventsFor(final Registration registration, final EventTemplate.Type type) {
        return registration.getStudent().getPerson().getEventsSet().stream()
                .filter(CustomEvent.class::isInstance)
                .map(CustomEvent.class::cast)
                .filter(event -> type.isType(event))
                .filter(event -> !event.isCancelled());
    }

    private static ExecutionYear executionYearFor(final CustomEvent event) {
        return JsonUtils.toDomainObject(event.getConfigObject(), "executionYear");
    }

    private static ExecutionSemester executionSemester(final CustomEvent event) {
        return JsonUtils.toDomainObject(event.getConfigObject(), "executionSemester");
    }

    private static RegistrationDataByExecutionYear registrationDataByExecutionYearFor(final CustomEvent event) {
        return JsonUtils.toDomainObject(event.getConfigObject(), "registrationDataByExecutionYear");
    }

    private static CurricularCourse curricularCourseFor(final CustomEvent event) {
        return JsonUtils.toDomainObject(event.getConfigObject(), "curricularCourse");
    }

    public void updateEventsFor(final RegistrationDataByExecutionYear dataByExecutionYear) {
        Arrays.stream(EventTemplate.Type.values())
                .forEach(type -> updateEventsFor(dataByExecutionYear, type));
    }

    public void updateEventsFor(final RegistrationDataByExecutionYear dataByExecutionYear, final EventTemplate.Type type) {
        final Registration registration = dataByExecutionYear.getRegistration();
        final ExecutionYear executionYear = dataByExecutionYear.getExecutionYear();
        if (type != EventTemplate.Type.TUITION || !registration.getDegree().isEmpty()) {
            if (eventsFor(dataByExecutionYear, type).findAny().isPresent()) {
                final double payedAmount = eventsFor(dataByExecutionYear, type)
                        .map(this::close)
                        .mapToDouble(calculator -> calculator.getPaidDebtAmount().doubleValue())
                        .sum();
                final double newDebtAmount = valueFor(type);
                if (payedAmount > newDebtAmount) {
                    throw new Error("Payed amount exceeds new event value");
                }
                if (payedAmount < newDebtAmount) {
                    final CustomEvent event = createEvent(dataByExecutionYear, type);
                    if (event != null && payedAmount > 0d) {
                        new EventExemption(event, registration.getPerson(), new Money(payedAmount),
                                EventExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION, new DateTime(),
                                BundleUtil.getString(Bundle.STUDENT, "label.change.payment.plan.justification"));
                    }
                }
            }
        } else {
            registration.getStudentCurricularPlansSet().stream()
                    .flatMap(scp -> scp.getEnrolmentStream())
                    .filter(enrolment -> enrolment.getExecutionYear() == executionYear)
                    .filter(enrolment -> !enrolment.isAnnulled())
                    .forEach(enrolment -> {
                        if (eventsFor(enrolment, type).findAny().isPresent()) {
                            final double payedAmount = eventsFor(enrolment, type)
                                    .map(this::close)
                                    .mapToDouble(calculator -> calculator.getPaidDebtAmount().doubleValue())
                                    .sum();
                            final double newDebtAmount = valueFor(enrolment.getEctsCredits().doubleValue(), type);
                            if (payedAmount > newDebtAmount) {
                                throw new Error("Payed amount exceeds new event value");
                            }
                            if (payedAmount < newDebtAmount) {
                                final CustomEvent event = createEvent(enrolment, type);
                                if (payedAmount > 0d) {
                                    new EventExemption(event, registration.getPerson(), new Money(payedAmount),
                                            EventExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION, new DateTime(),
                                            BundleUtil.getString(Bundle.STUDENT, "label.change.payment.plan.justification"));
                                }
                            }
                        }
                    });
        }
    }

    private CustomEvent createEvent(final RegistrationDataByExecutionYear dataByExecutionYear, final EventTemplate.Type type) {
        final Registration registration = dataByExecutionYear.getRegistration();
        final ExecutionYear executionYear = dataByExecutionYear.getExecutionYear();
        final JsonObject config = getConfig().getAsJsonObject(type.name());
        final Map<LocalDate, Money> dueDateAmountMap = toDueDateAmountMap(config.getAsJsonObject("dueDateAmountMap"));
        if (dueDateAmountMap.isEmpty()) {
            return null;
        }
        final LocalizedString description = type == EventTemplate.Type.TUITION
                ? BundleUtil.getLocalizedString(Bundle.STUDENT, "label.custom.event." + type.name(),
                registration.getDegree().getSigla(), executionYear.getName())
                : BundleUtil.getLocalizedString(Bundle.STUDENT, "label.custom.event." + type.name(),
                executionYear.getName());
        return createEvent(type, registration, description, dueDateAmountMap, data -> {
            data.addProperty("executionYear", executionYear.getExternalId());
            if (type == EventTemplate.Type.TUITION) {
                data.addProperty("registrationDataByExecutionYear", dataByExecutionYear.getExternalId());
            }
        });
    }

    private CustomEvent createEvent(final Enrolment enrolment, final EventTemplate.Type type) {
        final JsonObject config = getConfig().getAsJsonObject(type.name());
        final Map<LocalDate, Money> dueDateAmountMap = new TreeMap<>();
        final JsonObject byECTS = config.getAsJsonObject("byECTS");
        dueDateAmountMap.put(new LocalDate().plusDays(JsonUtils.getInt(byECTS, "daysToPay")),
                new Money(byECTS.get("value").getAsString()).multiply(new BigDecimal(enrolment.getEctsCredits().doubleValue())));
        final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
        final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
        final LocalizedString description = BundleUtil.getLocalizedString(Bundle.STUDENT,
                "label.custom.event." + type.name(),
                curricularCourse.getName(),
                executionSemester.getQualifiedName());
        return createEvent(type, enrolment.getRegistration(), description, dueDateAmountMap, data -> {
            data.addProperty("executionSemester", executionSemester.getExternalId());
            data.addProperty("curricularCourse", curricularCourse.getExternalId());
        });
    }

    private DebtInterestCalculator close(final Event event) {
        final DebtInterestCalculator calculator = event.getDebtInterestCalculator(new DateTime());
        final BigDecimal debtAmount = calculator.getDebtAmount();
        if (debtAmount.signum() > 0) {
            event.exempt(Authenticate.getUser().getPerson(), BundleUtil.getString(Bundle.STUDENT, "label.change.payment.plan.justification"));
        }
        return calculator;
    }


    public Money calculateTuition(final RegistrationDataByExecutionYear dataByExecutionYear) {
        final JsonObject config = getConfig().getAsJsonObject(EventTemplate.Type.TUITION.name());
        if (config != null) {
            final Map<LocalDate, Money> dueDateAmountMap = toDueDateAmountMap(config.getAsJsonObject("dueDateAmountMap"));
            final Money baseTuition = dueDateAmountMap.values().stream().reduce(Money.ZERO, Money::add);
            final JsonObject byECTS = config.getAsJsonObject("byECTS");
            return byECTS == null ? baseTuition : baseTuition.add(new Money(byECTS.get("value").getAsString())
                    .multiply(enrolledECTS(dataByExecutionYear)));
        }
        return null;
    }

    private BigDecimal enrolledECTS(final RegistrationDataByExecutionYear dataByExecutionYear) {
        final ExecutionYear executionYear = dataByExecutionYear.getExecutionYear();
        return dataByExecutionYear.getRegistration().getStudentCurricularPlansSet().stream()
                .flatMap(scp -> scp.getEnrolmentStream())
                .map(enrolment -> enrolment.getEctsCreditsForCurriculum())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
