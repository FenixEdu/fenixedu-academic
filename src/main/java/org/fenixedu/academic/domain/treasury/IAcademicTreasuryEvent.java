package org.fenixedu.academic.domain.treasury;

import java.math.BigDecimal;
import java.util.List;

import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.LocalDate;

public interface IAcademicTreasuryEvent {

    public LocalizedString getDescription();

    public String getDebtAccountURL();

    /* -------------------------
     * KIND OF EVENT INFORMATION
     * -------------------------
     */

    public boolean isTuitionEvent();

    public boolean isAcademicServiceRequestEvent();

    public boolean isAcademicTax();

    public boolean isImprovementTax();

    /* ---------------------
     * FINANTIAL INFORMATION
     * ---------------------
     */

    public boolean isWithDebitEntry();

    public boolean isExempted();

    default boolean isPayed() {
        return getRemainingAmountToPay().compareTo(BigDecimal.ZERO) <= 0;
    }

    default boolean isInDebt() {
        return getRemainingAmountToPay().compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isDueDateExpired(final LocalDate when);

    public boolean isBlockingAcademicalActs(final LocalDate when);

    public BigDecimal getAmountToPay();
    
    public BigDecimal getInterestsAmountToPay();

    public BigDecimal getRemainingAmountToPay();

    public BigDecimal getExemptedAmount();

    public LocalDate getDueDate();

    public String getExemptionReason();

    public List<IAcademicTreasuryEventPayment> getPaymentsList();

    public String formatMoney(BigDecimal moneyValue);

}
