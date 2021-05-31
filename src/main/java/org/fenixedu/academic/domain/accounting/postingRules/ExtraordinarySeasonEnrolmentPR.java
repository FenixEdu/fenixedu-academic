package org.fenixedu.academic.domain.accounting.postingRules;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.EnrolmentEvaluationEvent;
import org.fenixedu.academic.domain.accounting.events.SpecialSeasonEnrolmentEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Optional;

public class ExtraordinarySeasonEnrolmentPR extends ExtraordinarySeasonEnrolmentPR_Base implements IEnrolmentEvaluationPR  {

    protected ExtraordinarySeasonEnrolmentPR() {
        super();
    }

    public ExtraordinarySeasonEnrolmentPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
                                    Money fixedAmount) {
        super.init(EntryType.EXTRAORDINARY_SEASON_ENROLMENT_FEE, EventType.EXTRAORDINARY_SEASON_ENROLMENT, startDate, endDate,
                serviceAgreementTemplate);
        checkParameters(fixedAmount);
        super.setFixedAmount(fixedAmount);
    }

    private void checkParameters(Money fixedAmount) {
        if (fixedAmount == null) {
            throw new DomainException("error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.fixedAmount.cannot.be.null");
        }
    }

    public ExtraordinarySeasonEnrolmentPR edit(final Money fixedAmount) {
        deactivate();
        return new ExtraordinarySeasonEnrolmentPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event) {
        return getFixedAmount();
    }

    @Override
    public Money getFixedAmountPenalty() {
        return Money.ZERO;
    }

    @Override
    public Optional<LocalDate> getDueDate(final Event event) {
        return Optional.empty();
    }
    
}
