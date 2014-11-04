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
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class StandaloneEnrolmentCertificateRequestPR extends StandaloneEnrolmentCertificateRequestPR_Base {

    public StandaloneEnrolmentCertificateRequestPR() {
        super();
    }

    public StandaloneEnrolmentCertificateRequestPR(EntryType entryType, EventType eventType, DateTime startDate,
            DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerUnit,
            Money amountPerPage) {
        init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage);
    }
}
