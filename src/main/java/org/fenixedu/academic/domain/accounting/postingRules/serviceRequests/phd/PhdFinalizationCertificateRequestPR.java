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
package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.phd;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.util.Money;

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
