package org.fenixedu.academic.domain.treasury;

import java.math.BigDecimal;

import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.LocalDate;

public interface IAcademicTreasuryEventPayment {
    
    public LocalDate getPaymentDate();
    
    public BigDecimal getPayedAmount();
    
}
