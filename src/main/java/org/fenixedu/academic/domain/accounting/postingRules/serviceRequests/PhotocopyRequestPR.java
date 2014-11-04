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
import org.fenixedu.academic.domain.accounting.events.serviceRequests.PhotocopyRequestEvent;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class PhotocopyRequestPR extends PhotocopyRequestPR_Base {

    protected PhotocopyRequestPR() {
        super();
    }

    public PhotocopyRequestPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
            final DateTime endDate, final Money baseAmount, final Money amountPerPage) {
        this();
        super.init(EntryType.PHOTOCOPY_REQUEST_FEE, EventType.PHOTOCOPY_REQUEST, startDate, endDate, serviceAgreementTemplate,
                baseAmount, amountPerPage);
    }

    public PhotocopyRequestPR edit(final Money baseAmount, final Money amountPerUnit) {
        deactivate();
        return new PhotocopyRequestPR(getServiceAgreementTemplate(), new DateTime().minus(1000), null, baseAmount, amountPerUnit);
    }

    @Override
    protected Money getAmountForPages(final Event event) {
        final PhotocopyRequestEvent requestEvent = (PhotocopyRequestEvent) event;
        final int extraPages = requestEvent.getNumberOfPages().intValue() - 1;
        return getAmountPerPage().multiply(BigDecimal.valueOf(extraPages < 0 ? 0 : extraPages));
    }

    @Override
    protected boolean isUrgent(final Event event) {
        return ((PhotocopyRequestEvent) event).isUrgentRequest();
    }
}
