package org.fenixedu.academic.domain.accounting.events.gratuity;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.IsAlienRule;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.EnrolmentGratuityPR;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.RegistrationRegimeType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;

public class EnrolmentGratuityEvent extends EnrolmentGratuityEvent_Base {

    protected EnrolmentGratuityEvent(Person person, Enrolment enrolment, EnrolmentGratuityPR postingRule) {
        super();
        final AdministrativeOffice administrativeOffice =
                enrolment.getDegreeCurricularPlanOfStudent().getDegree().getAdministrativeOffice();
        final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
        final ExecutionYear executionYear = enrolment.getExecutionYear();

        if (postingRule == null) {
            throw new DomainException("No matching posting rule for enrolment gratuity event");
        }
        init(administrativeOffice, postingRule.getEventType(), person, studentCurricularPlan, executionYear);
        setEventPostingRule(postingRule);
        setupEnrolment(enrolment);
        persistDueDateAmountMap();
    }

    private void setupEnrolment(Enrolment enrolment) {
        setEnrolment(enrolment);
        setEcts(enrolment.getEctsCreditsForCurriculum());
        setCourseName(enrolment.getName());
        setExecutionPeriodName(enrolment.getExecutionPeriod().getQualifiedName());
    }

    private static DomainException cantCreateEvent(Enrolment enrolment) {
        return new DomainException("No matching EnrolmentGratuityPR for enrolment gratuity event",
                enrolment.getStudentCurricularPlan().getName());
    }

    /***
     * Creates an @{link EnrolmentGratuityEvent} if necessary
     * Checks if the enrolment is a standalone or partial regime enrolment.
     * @param enrolment the enrolment to associate the event
     * @return an {@link Optional} with the event if already existed or just created
     */
    public static Optional<EnrolmentGratuityEvent> create(Enrolment enrolment) {
        if (enrolment.getStudentCurricularPlan().getRegistration().isActive() && enrolment.getStudentCurricularPlan()
                .getRegistration().hasToPayGratuityOrInsurance()) {
            EventType eventType = null;
            if (enrolment.getStudentCurricularPlan().isEmptyDegree() || enrolment.isStandalone()) {
                eventType = EventType.STANDALONE_PER_ENROLMENT_GRATUITY;
            } else {
                if (RegistrationRegimeType.PARTIAL_TIME.equals(enrolment.getStudentCurricularPlan().getRegistration()
                        .getRegimeType(enrolment.getExecutionYear()))) {
                    eventType = EventType.PARTIAL_REGIME_ENROLMENT_GRATUITY;
                }
            }

            if (eventType != null) {
                final IsAlienRule isAlienRule = new IsAlienRule();
                boolean forAliens =
                        isAlienRule.isAppliableFor(enrolment.getStudentCurricularPlan(), enrolment.getExecutionYear());
                return Optional.of(create(enrolment.getPerson(), enrolment, eventType, forAliens));
            }
        }
        return Optional.empty();
    }

    @Override
    public void cancel(Person responsible, String cancelJustification) {
        super.cancel(responsible, cancelJustification);
        setEnrolment(null);
    }

    public static EnrolmentGratuityEvent create(Person person, Enrolment enrolment, EventType eventType, boolean forAliens) {
        final ExecutionYear executionYear = enrolment.getExecutionYear();

        //when there are enrollments and unenrollments in curricular units the connection to the enrolment is lost but the event still remains
        //whe don't want to create several different events for the same curricular unit
        EnrolmentGratuityEvent enrolmentGratuityEvent = person.getEventsByEventType(eventType).stream()
                .filter(EnrolmentGratuityEvent.class::isInstance)
                .map(EnrolmentGratuityEvent.class::cast)
                .filter(e -> e.getEnrolment() == null)
                .filter(e -> !e.isCancelled())
                .filter(e -> e.getEventPostingRule().isForAliens() == forAliens)
                .filter(e -> e.getCourseName().equals(enrolment.getName()))
                .filter(e -> e.getExecutionPeriodName().equals(enrolment.getExecutionPeriod().getQualifiedName()))
                .filter(e -> e.getEcts().equals(enrolment.getEctsCreditsForCurriculum()))
                .findAny().orElse(null);
        if (enrolmentGratuityEvent != null) {
        	FenixFramework.atomic(() -> enrolmentGratuityEvent.setEnrolment(enrolment));
            return enrolmentGratuityEvent;
        }

        Set<PostingRule> postingRules =
                enrolment.getStudentCurricularPlan().getDegreeCurricularPlan().getServiceAgreementTemplate()
                        .getPostingRulesBy(eventType, executionYear.getBeginLocalDate().toDateTimeAtStartOfDay(),
                                executionYear.getEndLocalDate().toDateTimeAtStartOfDay());

        if (!postingRules.stream().allMatch(EnrolmentGratuityPR.class::isInstance)) {
            throw cantCreateEvent(enrolment);
        }

        EnrolmentGratuityPR enrolmentGratuityPR =
                postingRules.stream().map(EnrolmentGratuityPR.class::cast).filter(p -> p.isForAliens() == forAliens)
                		.sorted(PostingRule.COMPARATOR_BY_START_DATE.reversed()).findFirst()
                        .orElseThrow(() -> cantCreateEvent(enrolment));

        try {
            return FenixFramework.atomic(() -> new EnrolmentGratuityEvent(person, enrolment, enrolmentGratuityPR));
        } catch (Exception e) {
            throw cantCreateEvent(enrolment);
        }
    }

    @Override
    public PostingRule getPostingRule() {
        return super.getEventPostingRule();
    }

    @Override
    public Enrolment getEnrolment() {
        return super.getEnrolment();
    }

    @Override
    public Map<LocalDate, Money> calculateDueDateAmountMap() {
        Money enrolmentAmount = getEventPostingRule().calculateTotalAmountToPay(this);
        LocalDate dueDate =
                getWhenOccured().toLocalDate().plusDays(getEventPostingRule().getNumberOfDaysToStartApplyingInterest());
        return Collections.singletonMap(dueDate, enrolmentAmount);
    }

    @Override
    protected LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter result = new LabelFormatter();
        result.appendLabel(getEventType().getQualifiedName(), Bundle.ENUMERATION);
        if (getEventPostingRule().isForAliens()) {
            result.appendLabel(" (");
            result.appendLabel("label.forAliens", Bundle.APPLICATION);
            result.appendLabel(" )");
        }
        result.appendLabel(" - ");
        result.appendLabel(getCourseName().getContent());

        result.appendLabel(" (");
        result.appendLabel(getExecutionPeriodName());
        result.appendLabel(")");

        result.appendLabel(" - ");
        result.appendLabel(getEcts().toPlainString());
        result.appendLabel(" ");
        result.appendLabel("label.ects", Bundle.APPLICATION);

        return result;
    }
}