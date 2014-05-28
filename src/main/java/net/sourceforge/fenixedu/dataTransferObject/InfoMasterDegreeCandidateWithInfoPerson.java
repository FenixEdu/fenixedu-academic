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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Fernanda Quitério Created on 1/Jul/2004
 * 
 */
public class InfoMasterDegreeCandidateWithInfoPerson extends InfoMasterDegreeCandidate {

    @Override
    public void copyFromDomain(MasterDegreeCandidate masterDegreeCandidate) {
        super.copyFromDomain(masterDegreeCandidate);
        if (masterDegreeCandidate != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(masterDegreeCandidate.getPerson()));
            setAverage(masterDegreeCandidate.getAverage());
            setCandidateNumber(masterDegreeCandidate.getCandidateNumber());
            setGivenCredits(masterDegreeCandidate.getGivenCredits());
            setGivenCreditsRemarks(masterDegreeCandidate.getGivenCreditsRemarks());
            setMajorDegree(masterDegreeCandidate.getMajorDegree());
            setMajorDegreeSchool(masterDegreeCandidate.getMajorDegreeSchool());
            setMajorDegreeYear(masterDegreeCandidate.getMajorDegreeYear());
            setSpecializationArea(masterDegreeCandidate.getSpecializationArea());
            setSubstituteOrder(masterDegreeCandidate.getSubstituteOrder());
            setSituationList(new ArrayList<InfoCandidateSituation>(masterDegreeCandidate.getSituations().size()));

            for (CandidateSituation candidateSituation : masterDegreeCandidate.getSituations()) {
                getSituationList().add(InfoCandidateSituation.newInfoFromDomain(candidateSituation));

                if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {
                    setInfoCandidateSituation(InfoCandidateSituation.newInfoFromDomain(candidateSituation));
                }
            }

        }
    }

    public static InfoMasterDegreeCandidate newInfoFromDomain(MasterDegreeCandidate masterDegreeCandidate) {
        InfoMasterDegreeCandidateWithInfoPerson infoMasterDegreeCandidateWithInfoPerson = null;
        if (masterDegreeCandidate != null) {
            infoMasterDegreeCandidateWithInfoPerson = new InfoMasterDegreeCandidateWithInfoPerson();
            infoMasterDegreeCandidateWithInfoPerson.copyFromDomain(masterDegreeCandidate);
        }
        return infoMasterDegreeCandidateWithInfoPerson;
    }

}