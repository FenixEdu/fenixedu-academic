package org.fenixedu.academic.domain.accounting.events.gratuity;

import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.IsAlienRule;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.EnrolmentGratuityPR;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import edu.emory.mathcs.backport.java.util.Collections;
import pt.ist.fenixframework.FenixFramework;

public class EnrolmentGratuityEvent extends EnrolmentGratuityEvent_Base {

    static {
        Signal.register(Enrolment.SIGNAL_CREATED, (DomainObjectEvent<Enrolment> wrapper) -> {
            final Enrolment enrolment = wrapper.getInstance();
            if (enrolment.getStudentCurricularPlan().getRegistration().isActive() && enrolment.getStudentCurricularPlan()
                    .getRegistration().hasToPayGratuityOrInsurance()) {
                enrolment.getGratuityEvent().orElseGet(() -> {
                    final IsAlienRule isAlienRule = new IsAlienRule();
                    if (enrolment.getStudentCurricularPlan().isEmptyDegree() || enrolment.getCurriculumGroup().isStandalone()) {
                        return create(enrolment.getPerson(), enrolment, EventType.STANDALONE_PER_ENROLMENT_GRATUITY,
                                isAlienRule.isAppliableFor(enrolment.getStudentCurricularPlan(), enrolment.getExecutionYear()));
                    }
                    return null;
                });
            }
        });
    }

    protected EnrolmentGratuityEvent(Person person, Enrolment enrolment, EnrolmentGratuityPR postingRule) {
        super();
        final AdministrativeOffice administrativeOffice = enrolment.getAcademicUnit().getAdministrativeOffice();
        final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
        final ExecutionYear executionYear = enrolment.getExecutionYear();

        if (postingRule == null) {
            throw new DomainException("No matching posting rule for enrolment gratuity event");
        }
        init(administrativeOffice, postingRule.getEventType(), person, studentCurricularPlan, executionYear);
        setEnrolment(enrolment);
        setEventPostingRule(postingRule);
    }

    private static DomainException cantCreateEvent(Enrolment enrolment) {
        return new DomainException("No matching EnrolmentGratuityPR for enrolment gratuity event",
                enrolment.getStudentCurricularPlan().getName());
    }

    public static EnrolmentGratuityEvent create(Person person, Enrolment enrolment, EventType eventType, boolean forAliens) {
        final ExecutionYear executionYear = enrolment.getExecutionYear();

        Set<PostingRule> postingRules =
                enrolment.getStudentCurricularPlan().getDegreeCurricularPlan().getServiceAgreementTemplate()
                        .getPostingRulesBy(eventType, executionYear.getBeginLocalDate().toDateTimeAtStartOfDay(),
                                executionYear.getEndLocalDate().toDateTimeAtStartOfDay());

        if (!postingRules.stream().allMatch(EnrolmentGratuityPR.class::isInstance)) {
            throw cantCreateEvent(enrolment);
        }

        EnrolmentGratuityPR enrolmentGratuityPR =
                postingRules.stream().map(EnrolmentGratuityPR.class::cast).filter(p -> p.isForAliens() == forAliens).findAny()
                        .orElseThrow(() -> cantCreateEvent(enrolment));

        return enrolment.getGratuityEvent().orElseGet(() -> {
            try {
                return FenixFramework.atomic(() -> new EnrolmentGratuityEvent(person, enrolment, enrolmentGratuityPR));
            } catch (Exception e) {
                throw cantCreateEvent(enrolment);
            }
        });
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
    public Map<LocalDate, Money> getDueDateAmountMap(PostingRule postingRule, DateTime when) {
        Money enrolmentAmount = postingRule.doCalculationForAmountToPayWithoutExemptions(this, when, false);
        LocalDate dueDate =
                getWhenOccured().toLocalDate().plusDays(getEventPostingRule().getNumberOfDaysToStartApplyingInterest());
        return Collections.<LocalDate, Money>singletonMap(dueDate, enrolmentAmount);
    }

}
