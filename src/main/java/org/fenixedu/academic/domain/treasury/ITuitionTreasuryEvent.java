package org.fenixedu.academic.domain.treasury;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.LocalDate;

public interface ITuitionTreasuryEvent extends IAcademicTreasuryEvent {

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
