/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.postingRules.serviceRequests;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.EquivalencePlanRequestEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class EquivalencePlanRequestPR extends EquivalencePlanRequestPR_Base {

    private EquivalencePlanRequestPR() {
        super();
    }

    public EquivalencePlanRequestPR(final DateTime startDate, final DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money amountPerUnit, final Money maximumAmount) {
        this();
        super.init(EntryType.EQUIVALENCE_PLAN_REQUEST_FEE, EventType.EQUIVALENCE_PLAN_REQUEST, startDate, endDate,
                serviceAgreementTemplate);

        checkParameters(amountPerUnit, maximumAmount);

        setAmountPerUnit(amountPerUnit);
        super.setMaximumAmount(maximumAmount);
    }

    private void checkParameters(Money amountPerUnit, Money maximumAmount) {
        if (amountPerUnit == null) {
            throw new DomainException("error.accounting.postingRules.EquivalencePlanRequestPR.amountPerUnit.cannot.be.null");
        }

        if (maximumAmount == null) {
            throw new DomainException("error.accounting.postingRules.EquivalencePlanRequestPR.maximumAmount.cannot.be.null");
        }
    }

    @Override
    public void setMaximumAmount(final Money maximumAmount) {
        throw new DomainException("error.accounting.postingRules.EquivalencePlanRequestPR.maximumAmount.cannot.be.modified");
    }

    @Override
    protected Money doCalculationForAmountToPay(final Event event, final DateTime when, boolean applyDiscount) {
        final EquivalencePlanRequestEvent planRequest = (EquivalencePlanRequestEvent) event;

        Money amountToPay = getAmountPerUnit();

        if (planRequest.getNumberOfEquivalences() != null && planRequest.getNumberOfEquivalences().intValue() != 0) {
            amountToPay = amountToPay.multiply(planRequest.getNumberOfEquivalences().intValue());
        }

        if (getMaximumAmount().greaterThan(Money.ZERO)) {
            if (amountToPay.greaterThan(getMaximumAmount())) {
                amountToPay = getMaximumAmount();
            }
        }

        return amountToPay;
    }

    public EquivalencePlanRequestPR edit(final Money amountPerUnit, final Money maximumAmount) {
        deactivate();
        return new EquivalencePlanRequestPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), amountPerUnit,
                maximumAmount);
    }

    public String getMaximumAmountDescription() {
        if (Money.ZERO.equals(this.getMaximumAmount())) {
            return BundleUtil.getString(Bundle.APPLICATION, "label.base.amount.plus.units.with.no.maximum.value");
        }

        return this.getMaximumAmount().getAmountAsString();
    }

}
