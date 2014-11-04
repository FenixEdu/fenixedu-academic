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
