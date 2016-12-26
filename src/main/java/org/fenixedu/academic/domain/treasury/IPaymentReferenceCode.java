package org.fenixedu.academic.domain.treasury;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

public interface IPaymentReferenceCode {
    
    public String getEntityCode();
    public String getReferenceCode();
    public String getFormattedCode();
    public BigDecimal getPayableAmount();
    public LocalDate getEndDate();
    
    public boolean isUsed();
    public boolean isProcessed();
    public boolean isAnnuled();
}
