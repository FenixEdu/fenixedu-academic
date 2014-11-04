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
import org.fenixedu.academic.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import org.fenixedu.academic.util.Money;
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
