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
package org.fenixedu.academic.domain.accounting.events.candidacy;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.candidacyProcess.over23.Over23IndividualCandidacy;

public class Over23IndividualCandidacyEvent extends Over23IndividualCandidacyEvent_Base {

    private Over23IndividualCandidacyEvent() {
        super();
    }

    public Over23IndividualCandidacyEvent(final Over23IndividualCandidacy candidacy, final Person person) {
        this();
        super.init(candidacy, EventType.OVER23_INDIVIDUAL_CANDIDACY, person);

        attachAvailablePaymentCode(person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    @Override
    protected EntryType getEntryType() {
        return EntryType.OVER23_INDIVIDUAL_CANDIDACY_FEE;
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

}
