package org.fenixedu.academic.domain.accounting.postingRules.gratuity;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

/***
 * This class represents the posting rule of the fixed part of the partial regime
 * It returns always the same amount
 */
public class PartialRegimePR extends PartialRegimePR_Base {
    
    protected PartialRegimePR() {
        super();
    }

    public PartialRegimePR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate
            serviceAgreementTemplate, Money amount, Integer numberOfDaysToStartApplyingInterest, boolean forAliens) {
        super.init(EntryType.GRATUITY_FEE, EventType.PARTIAL_REGIME_GRATUITY, startDate, endDate, serviceAgreementTemplate);
        setNumberOfDaysToStartApplyingInterest(numberOfDaysToStartApplyingInterest);
        setAmount(amount);
        setForAliens(forAliens);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        return getAmount();
    }

    @Override
    public Integer getNumberOfDaysToStartApplyingInterest() {
        return super.getNumberOfDaysToStartApplyingInterest();
    }

    @Override
    public Money getAmount() {
        return super.getAmount();
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

        return BundleUtil.getString(Bundle.APPLICATION, key, getAmount().toPlainString(), getNumberOfDaysToStartApplyingInterest().toString());
    }

    @Override
    public boolean overlaps(PostingRule postingRule) {
        if (super.overlaps(postingRule)) {
            return ((PartialRegimePR)postingRule).isForAliens() == isForAliens();
        }
        return false;
    }
}