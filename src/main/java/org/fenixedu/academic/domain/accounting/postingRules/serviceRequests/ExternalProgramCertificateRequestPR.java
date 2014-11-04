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

public class ExternalProgramCertificateRequestPR extends ExternalProgramCertificateRequestPR_Base {

    private ExternalProgramCertificateRequestPR() {
        super();
    }

    public ExternalProgramCertificateRequestPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
            final DateTime endDate, final Money certificateAmount, final Money amountFirstPage, final Money amountPerPage) {
        this();
        super.init(EntryType.EXTERNAL_PROGRAM_CERTIFICATE_REQUEST_FEE, EventType.EXTERNAL_PROGRAM_CERTIFICATE_REQUEST, startDate,
                endDate, serviceAgreementTemplate, certificateAmount, amountPerPage);
        checkParameters(amountFirstPage);
        super.setAmountFirstPage(amountFirstPage);
    }

    @Override
    public ExternalProgramCertificateRequestPR edit(final Money certificateAmount, final Money amountFirstPage,
            final Money amountPerPage) {
        deactivate();
        return new ExternalProgramCertificateRequestPR(getServiceAgreementTemplate(), new DateTime().minus(1000), null,
                certificateAmount, amountFirstPage, amountPerPage);
    }
}
