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
package org.fenixedu.academic.service.services.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidate;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidateWithInfoPerson;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.util.SituationName;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadSubstituteCandidates {

    @Atomic
    public static List<InfoMasterDegreeCandidate> run(String[] candidateList, String[] ids) throws FenixServiceException {

        List<InfoMasterDegreeCandidate> result = new ArrayList<InfoMasterDegreeCandidate>();

        // Read the substitute candidates
        int size = candidateList.length;

        for (int i = 0; i < size; i++) {
            if (candidateList[i].equals(SituationName.SUPLENTE_STRING)
                    || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING)
                    || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING)
                    || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING)) {

                String externalId = ids[i];

                MasterDegreeCandidate masterDegreeCandidateToWrite = FenixFramework.getDomainObject(externalId);
                result.add(InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidateToWrite));
            }
        }

        return result;
    }

}