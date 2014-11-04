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
package org.fenixedu.academic.service.services.masterDegree.administrativeOffice.candidate;

import static org.fenixedu.academic.predicate.AccessControl.check;
import org.fenixedu.academic.service.services.ExcepcaoInexistente;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidate;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidateWithInfoPerson;
import org.fenixedu.academic.dto.InfoPersonEditor;
import org.fenixedu.academic.domain.CandidateSituation;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.util.State;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class EditMasterDegreeCandidate {

    @Atomic
    public static InfoMasterDegreeCandidate run(MasterDegreeCandidate oldMasterDegreeCandidate,
            InfoMasterDegreeCandidate newCandidate, InfoPersonEditor infoPersonEditor) throws ExcepcaoInexistente,
            FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        if (oldMasterDegreeCandidate == null) {
            throw new ExcepcaoInexistente("Unknown Candidate !!");
        }

        // Change personal Information
        Person person = oldMasterDegreeCandidate.getPerson();
        Country country = null;
        if ((infoPersonEditor.getInfoPais() != null)) {
            country = Country.readCountryByNationality(infoPersonEditor.getInfoPais().getNationality());
        }
        person.edit(infoPersonEditor, country);

        // Change Candidate Information
        oldMasterDegreeCandidate.setAverage(newCandidate.getAverage());
        oldMasterDegreeCandidate.setMajorDegree(newCandidate.getMajorDegree());
        oldMasterDegreeCandidate.setMajorDegreeSchool(newCandidate.getMajorDegreeSchool());
        oldMasterDegreeCandidate.setMajorDegreeYear(newCandidate.getMajorDegreeYear());
        oldMasterDegreeCandidate.setSpecializationArea(newCandidate.getSpecializationArea());

        // Change Situation
        CandidateSituation oldCandidateSituation = oldMasterDegreeCandidate.getActiveCandidateSituation();
        CandidateSituation newCandidateSituation = null;
        if (!oldCandidateSituation.getSituation().equals(newCandidate.getInfoCandidateSituation().getSituation())) {

            oldCandidateSituation.setValidation(new State(State.INACTIVE));
            newCandidateSituation = new CandidateSituation();
            newCandidateSituation.setDateYearMonthDay(new YearMonthDay());
            newCandidateSituation.setMasterDegreeCandidate(oldMasterDegreeCandidate);
            newCandidateSituation.setRemarks(newCandidate.getInfoCandidateSituation().getRemarks());
            newCandidateSituation.setSituation(newCandidate.getInfoCandidateSituation().getSituation());
            newCandidateSituation.setValidation(new State(State.ACTIVE));

        } else if (oldCandidateSituation.getSituation().equals(newCandidate.getInfoCandidateSituation().getSituation())) {
            newCandidateSituation = oldCandidateSituation;
            newCandidateSituation.setDateYearMonthDay(oldCandidateSituation.getDateYearMonthDay());
            newCandidateSituation.setMasterDegreeCandidate(oldMasterDegreeCandidate);
            newCandidateSituation.setRemarks(newCandidate.getInfoCandidateSituation().getRemarks());
            newCandidateSituation.setSituation(newCandidate.getInfoCandidateSituation().getSituation());
        }

        return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(oldMasterDegreeCandidate);
    }

}