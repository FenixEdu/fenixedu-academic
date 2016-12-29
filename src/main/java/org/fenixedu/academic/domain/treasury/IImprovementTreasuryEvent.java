package org.fenixedu.academic.domain.treasury;

import java.math.BigDecimal;
import java.util.List;

import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.joda.time.LocalDate;

public interface IImprovementTreasuryEvent extends IAcademicTreasuryEvent {

    /*
     * -----------
     * IMPROVEMENT
     * -----------
     */

    public boolean isCharged(final EnrolmentEvaluation enrolmentEvaluation);
    
    public boolean isExempted(final EnrolmentEvaluation enrolmentEvaluation);

    default boolean isPayed(final EnrolmentEvaluation enrolmentEvaluation) {
        return getRemainingAmountToPay(enrolmentEvaluation).compareTo(BigDecimal.ZERO) <= 0;
    }
    
    default boolean isInDebt(final EnrolmentEvaluation enrolmentEvaluation) {
        return getRemainingAmountToPay(enrolmentEvaluation).compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean isDueDateExpired(final EnrolmentEvaluation enrolmentEvaluation, final LocalDate when);
    
    public boolean isBlockingAcademicalActs(final EnrolmentEvaluation enrolmentEvaluation, final LocalDate when);

    public BigDecimal getAmountToPay(final EnrolmentEvaluation enrolmentEvaluation);

    public BigDecimal getRemainingAmountToPay(final EnrolmentEvaluation enrolmentEvaluation);

    public BigDecimal getExemptedAmount(final EnrolmentEvaluation enrolmentEvaluation);
    
    public LocalDate getDueDate(final EnrolmentEvaluation enrolmentEvaluation);
    
    public String getExemptionReason(final EnrolmentEvaluation enrolmentEvaluation);
    
    public List<IAcademicTreasuryEventPayment> getPaymentsList(final EnrolmentEvaluation enrolmentEvaluation);
    
}
