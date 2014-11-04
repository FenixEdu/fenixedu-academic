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

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPerson;

public class DegreeCandidacyForGraduatedPersonEvent extends DegreeCandidacyForGraduatedPersonEvent_Base {

    private DegreeCandidacyForGraduatedPersonEvent() {
        super();
    }

    public DegreeCandidacyForGraduatedPersonEvent(final DegreeCandidacyForGraduatedPerson candidacy, final Person person) {
        this();
        super.init(candidacy, EventType.DEGREE_CANDIDACY_FOR_GRADUATED_PERSON, person);

        attachAvailablePaymentCode(person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    @Override
    public DegreeCandidacyForGraduatedPerson getIndividualCandidacy() {
        return (DegreeCandidacyForGraduatedPerson) super.getIndividualCandidacy();
    }

    public Degree getCandidacyDegree() {
        return getIndividualCandidacy().getSelectedDegree();
    }

    @Override
    protected EntryType getEntryType() {
        return EntryType.DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_FEE;
    }

}
