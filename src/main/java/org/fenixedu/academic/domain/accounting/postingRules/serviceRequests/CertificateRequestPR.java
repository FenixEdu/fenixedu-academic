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

import java.math.BigDecimal;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class CertificateRequestPR extends CertificateRequestPR_Base {

    protected CertificateRequestPR() {
        super();
    }

    public CertificateRequestPR(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerUnit, Money amountPerPage,
            Money maximumAmount) {
        init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage,
                maximumAmount);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerUnit, Money amountPerPage,
            Money maximumAmount) {
        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, baseAmount, amountPerUnit, maximumAmount);
        checkParameters(amountPerPage);
        super.setAmountPerPage(amountPerPage);
    }

    private void checkParameters(Money amountPerPage) {
        if (amountPerPage == null) {
            throw new DomainException(
                    "error.accounting.postingRules.serviceRequests.CertificateRequestPR.amountPerPage.cannot.be.null");
        }
    }

    @Override
    public void setAmountPerPage(Money amountPerPage) {
        throw new DomainException(
                "error.accounting.postingRules.serviceRequests.CertificateRequestPR.cannot.modify.amountPerPage");
    }

    @Override
    protected Integer getNumberOfUnits(Event event) {
        return ((CertificateRequestEvent) event).getNumberOfUnits();
    }

    protected boolean isUrgent(CertificateRequestEvent certificateRequestEvent) {
        return certificateRequestEvent.isUrgentRequest();
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event) {
        final CertificateRequestEvent certificateRequestEvent = (CertificateRequestEvent) event;
        Money totalAmountToPay =
                isUrgent(certificateRequestEvent) ? getBaseAmount().multiply(BigDecimal.valueOf(2)).add(getAmountForUnits(event)) : super
                        .doCalculationForAmountToPay(event);
        totalAmountToPay = totalAmountToPay.add(calculateAmountToPayForPages(certificateRequestEvent));

        return totalAmountToPay;
    }

    protected Money calculateAmountToPayForPages(CertificateRequestEvent event) {
        return getAmountPerPage().multiply(BigDecimal.valueOf(event.getNumberOfPages()));
    }

    public CertificateRequestPR edit(final Money baseAmount, final Money amountPerUnit, final Money amountPerPage,
            final Money maximumAmount) {
        deactivate();
        return new CertificateRequestPR(getEntryType(), getEventType(), new DateTime().minus(1000), null,
                getServiceAgreementTemplate(), baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

}
