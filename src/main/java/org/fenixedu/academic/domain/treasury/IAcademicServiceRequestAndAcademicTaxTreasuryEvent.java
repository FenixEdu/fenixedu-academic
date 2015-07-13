package org.fenixedu.academic.domain.treasury;

import java.math.BigDecimal;

public interface IAcademicServiceRequestAndAcademicTaxTreasuryEvent extends IAcademicTreasuryEvent {

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
    
}
