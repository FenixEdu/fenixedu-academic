package org.fenixedu.academic.domain.treasury;

import org.joda.time.LocalDate;

public interface IPaymentReferenceCode {
    
    public String getEntityCode();
    public String getReferenceCode();
    public String getFormattedCode();
    public LocalDate getEndDate();
    
    public boolean isUsed();
    public boolean isProcessed();
    public boolean isAnnuled();
}
