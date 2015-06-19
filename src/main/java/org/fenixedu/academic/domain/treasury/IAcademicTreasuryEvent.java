package org.fenixedu.academic.domain.treasury;

import java.math.BigDecimal;
import java.util.List;

import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.LocalDate;

public interface IAcademicTreasuryEvent {

    public LocalizedString getDescription();
    
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

    default boolean isInDebt() {
        return getRemainingAmountToPay().compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean isDueDateExpired(final LocalDate when);
    
    public boolean isBlockingAcademicalActs(final LocalDate when);

    public BigDecimal getAmountToPay();

    public BigDecimal getRemainingAmountToPay();

    public BigDecimal getExemptedAmount();
    
    public LocalDate getDueDate();
    
    public String getExemptionReason();
    
    public List<IAcademicTreasuryEventPayment> getPaymentsList();
    
    
    /* ---------------------------------------------
     * ACADEMIC SERVICE REQUEST EVENT & ACADEMIC TAX
     * ---------------------------------------------
     */
    
    public BigDecimal getBaseAmount();
    
    public BigDecimal getAdditionalUnitsAmount();

    default boolean isWithAdditionalUnits() {
        return getAdditionalUnitsAmount().compareTo(BigDecimal.ZERO) > 0;
    }
    
    public BigDecimal getPagesAmount();
    
    default boolean isWithPagesAmount() {
        return getPagesAmount().compareTo(BigDecimal.ZERO) > 0;
    }
    
    public BigDecimal getMaximumAmount();

    default boolean isWithMaximumAmount() {
        return getMaximumAmount().compareTo(BigDecimal.ZERO) > 0;
    }
    
    public BigDecimal getAmountForLanguageTranslationRate();
    
    default boolean isWithAmountForLanguageTranslationRate() {
        return getAmountForLanguageTranslationRate().compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getAmountForUrgencyRate();
    
    default boolean isWithAmountForUrgencyRate() {
        return getAmountForUrgencyRate().compareTo(BigDecimal.ZERO) > 0;
    }

    /* -------------------
     * TUITION INFORMATION
     * -------------------
     */
    
    public int getTuitionInstallmentSize();
    
    public BigDecimal getTuitionInstallmentAmountToPay(int installmentOrder);
    
    public BigDecimal getTuitionInstallmentRemainingAmountToPay(int installmentOrder);
    
    public BigDecimal getTuitionInstallmentExemptedAmount(int installmentOrder);
    
    public LocalDate getTuitionInstallmentDueDate(int installmentOrder);
    
    public String getTuitionInstallmentDescription(int installmentOrder);

    public boolean isTuitionInstallmentExempted(int installmentOrder);
    
    public String getTuitionInstallmentExemptionReason(int installmentOrder);
    
    public List<IAcademicTreasuryEventPayment> getTuitionInstallmentPaymentsList(int installmentOrder);
    
}
