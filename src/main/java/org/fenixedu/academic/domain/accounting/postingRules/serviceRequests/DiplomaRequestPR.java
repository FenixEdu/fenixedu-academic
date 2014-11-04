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
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class DiplomaRequestPR extends DiplomaRequestPR_Base {

    protected DiplomaRequestPR() {
        super();
    }

    public DiplomaRequestPR(final EntryType entryType, final EventType eventType, final DateTime startDate,
            final DateTime endDate, final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount) {
        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount);
    }

    @Override
    final public DiplomaRequestPR edit(final Money fixedAmount) {
        deactivate();
        return new DiplomaRequestPR(getEntryType(), getEventType(), new DateTime().minus(1000), null,
                getServiceAgreementTemplate(), fixedAmount);
    }

}
