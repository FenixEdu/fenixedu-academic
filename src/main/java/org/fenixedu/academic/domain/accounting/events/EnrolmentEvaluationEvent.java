package org.fenixedu.academic.domain.accounting.events;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountType;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.postingRules.IEnrolmentEvaluationPR;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;

public class EnrolmentEvaluationEvent extends EnrolmentEvaluationEvent_Base {
    
    protected EnrolmentEvaluationEvent() {
        super();
    }

    public EnrolmentEvaluationEvent(AdministrativeOffice administrativeOffice, Person person, EventType eventType, PostingRule
            postingRule, EnrolmentEvaluation enrolmentEvaluation) {
        init(administrativeOffice, eventType, person);
        setupEnrolmentEvaluation(enrolmentEvaluation);
        setPostingRule(postingRule);
        setDueDate(((IEnrolmentEvaluationPR)getPostingRule()).getDueDate(this).orElse(getWhenOccured().toLocalDate()));
        persistDueDateAmountMap();
    }

    private void setupEnrolmentEvaluation(final EnrolmentEvaluation enrolmentEvaluation) {
        setEnrolmentEvaluation(enrolmentEvaluation);
        setCourseName(enrolmentEvaluation.getEnrolment().getName());
        setExecutionPeriodName(enrolmentEvaluation.getEnrolment().getExecutionPeriod().getQualifiedName());
    }

    private static Optional<EventType> getEventType(EnrolmentEvaluation enrolmentEvaluation) {
        if (enrolmentEvaluation.getEvaluationSeason().isImprovement()) {
            return Optional.of(EventType.IMPROVEMENT_OF_APPROVED_ENROLMENT);
        }
        if (enrolmentEvaluation.getEvaluationSeason().isSpecial()) {
            return Optional.of(EventType.SPECIAL_SEASON_ENROLMENT);
        }
        return Optional.empty();
    }

    public static EnrolmentEvaluationEvent create(EnrolmentEvaluation enrolmentEvaluation) {
        final Person student = enrolmentEvaluation.getStudentCurricularPlan().getPerson();

        final AdministrativeOffice administrativeOffice = enrolmentEvaluation.getStudentCurricularPlan()
                .getAdministrativeOffice();

        final EventType eventType = getEventType(enrolmentEvaluation).orElseThrow(
                () -> new DomainException("Unsupported enrolment evaluation",
                        enrolmentEvaluation.getEvaluationSeason().getName().getContent()));

        EnrolmentEvaluationEvent enrolmentEvaluationEvent = enrolmentEvaluation.getEnrolment().getPerson().getEventsByEventType(eventType).stream()
                .filter(EnrolmentEvaluationEvent.class::isInstance)
                .map(EnrolmentEvaluationEvent.class::cast)
                .filter(e -> e.getEnrolmentEvaluation() == null)
                .filter(e -> !e.isCancelled())
                .filter(e -> enrolmentEvaluation.getEnrolment().getName().equals(e.getCourseName()))
                .filter(e -> enrolmentEvaluation.getExecutionPeriod().getQualifiedName().equals(e.getExecutionPeriodName()))
                .findAny().orElse(null);
        if (enrolmentEvaluationEvent != null) {
        	FenixFramework.atomic(() -> enrolmentEvaluationEvent.setEnrolmentEvaluation(enrolmentEvaluation));
            return enrolmentEvaluationEvent;
        }

        final PostingRule eventTypePR =
                administrativeOffice.getServiceAgreementTemplate().findPostingRuleByEventType(eventType);

        if (eventTypePR == null) {
            throw new DomainException("No posting rule found");
        }

        if (!(eventTypePR instanceof IEnrolmentEvaluationPR)) {
            throw new DomainException("Incompatible posting rule type");
        }

        return Optional.ofNullable(enrolmentEvaluation.getEnrolmentEvaluationEvent()).orElseGet(() -> {
            try {
                return FenixFramework.atomic(() -> new EnrolmentEvaluationEvent(administrativeOffice, student, eventType,
                        eventTypePR, enrolmentEvaluation));
            } catch (DomainException e) {
                throw e;
            } catch (Exception e) {
                throw new DomainException("Error creating event", e);
            }
        });
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

    private void setPostingRule(PostingRule postingRule) {
        if (postingRule instanceof IEnrolmentEvaluationPR) {
            super.setEnrolmentEvaluationPostingRule(postingRule);
        } else {
            throw new UnsupportedOperationException("Can't associate non enrolment evaluation posting rule");
        }
    }

    @Override
    public PostingRule getPostingRule() {
        return super.getEnrolmentEvaluationPostingRule();
    }

    @Override
    public Map<LocalDate, Money> calculateDueDateAmountMap() {
        return Collections.singletonMap(getDueDate(), ((IEnrolmentEvaluationPR)getEnrolmentEvaluationPostingRule()).getFixedAmount());
    }

    @Override
    protected LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter result = new LabelFormatter();
        result.appendLabel(getEventType().getQualifiedName(), Bundle.ENUMERATION);
        result.appendLabel(" - ");
        result.appendLabel(getCourseName().getContent());

        result.appendLabel(" (");
        result.appendLabel(getExecutionPeriodName());
        result.appendLabel(")");

        return result;
    }

}
