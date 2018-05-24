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
package org.fenixedu.academic.domain.phd.candidacy;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class PhdProgramCandidacyPR extends PhdProgramCandidacyPR_Base {

    private PhdProgramCandidacyPR() {
        super();
    }

    public PhdProgramCandidacyPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
            final DateTime endDate, final Money amount) {
        this();
        init(EntryType.CANDIDACY_ENROLMENT_FEE, EventType.CANDIDACY_ENROLMENT, startDate, endDate, serviceAgreementTemplate,
                amount);
    }

    @Override
    public PhdProgramCandidacyPR edit(final Money amount) {
        deactivate();
        return new PhdProgramCandidacyPR(getServiceAgreementTemplate(), new DateTime().minus(1000), null, amount);
    }

}
