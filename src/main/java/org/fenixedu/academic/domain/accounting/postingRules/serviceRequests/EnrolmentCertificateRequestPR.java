/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class EnrolmentCertificateRequestPR extends EnrolmentCertificateRequestPR_Base {

    protected EnrolmentCertificateRequestPR() {
        super();
    }

    public EnrolmentCertificateRequestPR(final DateTime startDate, final DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money baseAmount, final Money amountPerUnit,
            final Money amountPerPage, final Money maximumAmount) {
        this();
        init(EntryType.ENROLMENT_CERTIFICATE_REQUEST_FEE, EventType.ENROLMENT_CERTIFICATE_REQUEST, startDate, endDate,
                serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final CertificateRequestEvent requestEvent = (CertificateRequestEvent) event;
        Money totalAmountToPay =
                calculateAmountToPayWithUnits(requestEvent, true).add(calculateAmountToPayForPages(requestEvent));

        return totalAmountToPay;
    }

    private Money calculateAmountToPayWithUnits(final CertificateRequestEvent requestEvent, final boolean checkUrgency) {
        Money total = checkUrgency && isUrgent(requestEvent) ? getBaseAmount().multiply(2) : getBaseAmount();

        final EnrolmentCertificateRequest request = (EnrolmentCertificateRequest) requestEvent.getAcademicServiceRequest();
        if (request.getDetailed() != null && request.getDetailed().booleanValue()) {
            total = total.add(getAmountForUnits(requestEvent));
        }
        return total;
    }

    @Override
    public EnrolmentCertificateRequestPR edit(final Money baseAmount, final Money amountPerUnit, final Money amountPerPage,
            final Money maximumAmount) {
        deactivate();
        return new EnrolmentCertificateRequestPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), baseAmount,
                amountPerUnit, amountPerPage, maximumAmount);
    }
}
