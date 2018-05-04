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
package org.fenixedu.academic.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public abstract class SpecializationDegreeGratuityPR extends SpecializationDegreeGratuityPR_Base implements IGratuityPR {

    protected SpecializationDegreeGratuityPR() {
        super();
    }

    public SpecializationDegreeGratuityPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal specializationDegreePartialAcceptedPercentage) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate,
                specializationDegreeTotalAmount, specializationDegreePartialAcceptedPercentage);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal specializationDegreePartialAcceptedPercentage) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);

        checkParameters(specializationDegreeTotalAmount, specializationDegreePartialAcceptedPercentage);

        super.setSpecializationDegreeTotalAmount(specializationDegreeTotalAmount);
        super.setSpecializationDegreePartialAcceptedPercentage(specializationDegreePartialAcceptedPercentage);
    }

    private void checkParameters(Money specializationDegreeTotalAmount, BigDecimal specializationDegreePartialAcceptedPercentage) {
        if (specializationDegreeTotalAmount == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.specializationDegreeTotalAmount.cannot.be.null");
        }

        if (specializationDegreePartialAcceptedPercentage == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.specializationDegreePartialAcceptedPercentage.cannot.be.null");
        }
    }

    @Override
    public void setSpecializationDegreeTotalAmount(Money specializationDegreeTotalAmount) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.cannot.modify.specializationDegreeTotalAmount");
    }

    @Override
    protected void checkIfCanAddAmount(Money amountToPay, Event event, DateTime when) {
        if (((GratuityEvent) event).isCustomEnrolmentModel()) {
            checkIfCanAddAmountForCustomEnrolmentModel(event, when, amountToPay);
        } else {
            checkIfCanAddAmountForCompleteEnrolmentModel(amountToPay, event, when);
        }
    }

    private void checkIfCanAddAmountForCustomEnrolmentModel(Event event, DateTime when, Money amountToAdd) {
        if (event.calculateAmountToPay(when).greaterThan(amountToAdd)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

    private void checkIfCanAddAmountForCompleteEnrolmentModel(final Money amountToAdd, final Event event, final DateTime when) {

        if (hasAlreadyPayedAnyAmount(event, when)) {
            final Money totalFinalAmount = event.getPayedAmount().add(amountToAdd);
            if (!(totalFinalAmount.greaterOrEqualThan(calculateTotalAmountToPay(event, when)) || totalFinalAmount
                    .equals(getPartialPaymentAmount(event, when)))) {
                throw new DomainExceptionWithLabelFormatter(
                        "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
                        event.getDescriptionForEntryType(getEntryType()));
            }
        } else {
            if (!isPayingTotalAmount(event, when, amountToAdd) && !isPayingPartialAmount(event, when, amountToAdd)) {
                final LabelFormatter percentageLabelFormatter = new LabelFormatter();
                percentageLabelFormatter.appendLabel(getSpecializationDegreePartialAcceptedPercentage().multiply(
                        BigDecimal.valueOf(100)).toString());

                throw new DomainExceptionWithLabelFormatter(
                        "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.invalid.partial.payment.value",
                        event.getDescriptionForEntryType(getEntryType()), percentageLabelFormatter);
            }
        }
    }

    private boolean isPayingTotalAmount(final Event event, final DateTime when, Money amountToAdd) {
        return amountToAdd.greaterOrEqualThan(event.calculateAmountToPay(when));
    }

    private boolean isPayingPartialAmount(final Event event, final DateTime when, final Money amountToAdd) {
        return amountToAdd.equals(getPartialPaymentAmount(event, when));
    }

    private boolean hasAlreadyPayedAnyAmount(final Event event, final DateTime when) {
        return !calculateTotalAmountToPay(event, when).equals(event.calculateAmountToPay(when));
    }

    private Money getPartialPaymentAmount(final Event event, final DateTime when) {
        return calculateTotalAmountToPay(event, when).multiply(getSpecializationDegreePartialAcceptedPercentage());
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final Money result;
        if (((GratuityEvent) event).isCustomEnrolmentModel()) {
            result = calculateSpecializationDegreeGratuityTotalAmountToPay(event);
        } else {
            result = getSpecializationDegreeTotalAmount();
        }

        return result;
    }

    abstract protected Money calculateSpecializationDegreeGratuityTotalAmountToPay(Event event);

    @Override
    public Money getDefaultGratuityAmount(ExecutionYear executionYear) {
        return getSpecializationDegreeTotalAmount();
    }

}
