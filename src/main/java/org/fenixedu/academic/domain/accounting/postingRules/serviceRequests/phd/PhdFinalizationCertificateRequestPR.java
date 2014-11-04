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
package org.fenixedu.academic.domain.accounting.postingRules.serviceRequests.phd;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class PhdFinalizationCertificateRequestPR extends PhdFinalizationCertificateRequestPR_Base {

    protected PhdFinalizationCertificateRequestPR() {
        super();
    }

    public PhdFinalizationCertificateRequestPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
        this();
        init(EntryType.PHD_FINALIZATION_CERTIFICATE_REQUEST_FEE, EventType.PHD_FINALIZATION_CERTIFICATE_REQUEST, startDate,
                endDate, serviceAgreementTemplate, fixedAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        AcademicServiceRequestEvent academicServiceRequestEvent = (AcademicServiceRequestEvent) event;
        AcademicServiceRequest academicServiceRequest = academicServiceRequestEvent.getAcademicServiceRequest();

        return super.doCalculationForAmountToPay(academicServiceRequestEvent, when, applyDiscount).multiply(
                academicServiceRequest.isUrgentRequest() ? 2 : 1);
    }

    @Override
    public PhdFinalizationCertificateRequestPR edit(final Money fixedAmount) {

        deactivate();
        return new PhdFinalizationCertificateRequestPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
                fixedAmount);
    }

    public Money getUrgentAmount() {
        return super.getFixedAmount();
    }
}
