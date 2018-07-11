package org.fenixedu.academic.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.gratuity.EnrolmentGratuityEvent;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

/**
 * 
 */
public class EnrolmentGratuityPR extends EnrolmentGratuityPR_Base {
    
    public EnrolmentGratuityPR() {
        super();
    }

    public EnrolmentGratuityPR(DateTime startDate, DateTime endDate, EventType eventType, ServiceAgreementTemplate
            serviceAgreementTemplate, BigDecimal amountPerEcts, Integer numberOfDaysToStartApplyingInterest, boolean
            forAliens) {
        super.init(EntryType.GRATUITY_FEE, eventType, startDate, endDate, serviceAgreementTemplate);
        setAmountPerEcts(amountPerEcts);
        setNumberOfDaysToStartApplyingInterest(numberOfDaysToStartApplyingInterest);
        setForAliens(forAliens);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final BigDecimal ectsCreditsForCurriculum = ((EnrolmentGratuityEvent) event).getEnrolment().getEctsCreditsForCurriculum();
        return new Money(getAmountPerEcts().multiply(ectsCreditsForCurriculum));
    }

    @Override
    public BigDecimal getAmountPerEcts() {
        return super.getAmountPerEcts();
    }

    @Override
    public Integer getNumberOfDaysToStartApplyingInterest() {
        return super.getNumberOfDaysToStartApplyingInterest();
    }

    public boolean isForAliens() {
        return getForAliens();
    }

    @Override
    public String getFormulaDescription() {
        String key = this.getClass().getSimpleName() + ".formulaDescription";

        if (isForAliens()) {
            key += ".forAliens";
        }
        
        return BundleUtil.getString(Bundle.APPLICATION, key, getAmountPerEcts().toPlainString(), getNumberOfDaysToStartApplyingInterest().toString());
    }
}
