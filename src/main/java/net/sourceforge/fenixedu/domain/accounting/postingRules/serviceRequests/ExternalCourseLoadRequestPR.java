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

public class ExternalCourseLoadRequestPR extends ExternalCourseLoadRequestPR_Base {

    private ExternalCourseLoadRequestPR() {
        super();
    }

    public ExternalCourseLoadRequestPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
            final DateTime endDate, final Money certificateAmount, final Money amountFirstPage, final Money amountPerPage) {
        this();
        super.init(EntryType.EXTERNAL_COURSE_LOAD_REQUEST_FEE, EventType.EXTERNAL_COURSE_LOAD_REQUEST, startDate, endDate,
                serviceAgreementTemplate, certificateAmount, amountPerPage);
        checkParameters(amountFirstPage);
        super.setAmountFirstPage(amountFirstPage);
    }

    @Override
    public ExternalCourseLoadRequestPR edit(final Money baseAmount, final Money amountFirstPage, final Money amountPerUnit) {
        deactivate();
        return new ExternalCourseLoadRequestPR(getServiceAgreementTemplate(), new DateTime().minus(1000), null, baseAmount,
                amountFirstPage, amountPerUnit);
    }
}
