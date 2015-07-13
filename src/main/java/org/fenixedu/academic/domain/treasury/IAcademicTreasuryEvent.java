package org.fenixedu.academic.domain.treasury;

import java.math.BigDecimal;

import org.fenixedu.commons.i18n.LocalizedString;

public interface IAcademicTreasuryEvent {

    public LocalizedString getDescription();

    public boolean isWithDebitEntry();
    
    default boolean isInDebt() {
        return false;
    }
    
    default boolean isDueDateExpired() {
        return false;
    }
    
    default boolean isBlockingAcademicActs() {
        return false;
    }

    public BigDecimal getAmountToPay();

    public BigDecimal getRemainingAmountToPay();

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

    public static IAcademicTreasuryEvent zeroValuesEvent(final LocalizedString description) {
        return new IAcademicTreasuryEvent() {

            @Override
            public boolean isWithDebitEntry() {
                return false;
            }

            @Override
            public BigDecimal getRemainingAmountToPay() {
                return BigDecimal.ZERO;
            }

            @Override
            public BigDecimal getPagesAmount() {
                return BigDecimal.ZERO;
            }

            @Override
            public BigDecimal getMaximumAmount() {
                return BigDecimal.ZERO;
            }

            @Override
            public LocalizedString getDescription() {
                return description;
            }

            @Override
            public BigDecimal getBaseAmount() {
                return BigDecimal.ZERO;
            }

            @Override
            public BigDecimal getAmountToPay() {
                return BigDecimal.ZERO;
            }

            @Override
            public BigDecimal getAmountForUrgencyRate() {
                return BigDecimal.ZERO;
            }

            @Override
            public BigDecimal getAmountForLanguageTranslationRate() {
                return BigDecimal.ZERO;
            }

            @Override
            public BigDecimal getAdditionalUnitsAmount() {
                return BigDecimal.ZERO;
            }
        };
    }

}
