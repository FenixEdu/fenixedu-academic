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
/*
 * Created on 14/Mar/2003
 *
 */
package org.fenixedu.academic.service.services.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.dto.InfoCandidateSituation;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidate;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidateWithInfoPerson;
import org.fenixedu.academic.domain.CandidateSituation;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.util.State;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetCandidatesByID {

    @Atomic
    public static InfoMasterDegreeCandidate run(String candidateID) throws FenixServiceException {
        MasterDegreeCandidate masterDegreeCandidate = null;

        if (candidateID == null) {
            throw new NonExistingServiceException();
        }

        masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);

        InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);

        final ExecutionDegree executionDegree = masterDegreeCandidate.getExecutionDegree();
        final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
        infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

        Iterator situationIterator = masterDegreeCandidate.getSituationsSet().iterator();
        List situations = new ArrayList();
        while (situationIterator.hasNext()) {
            InfoCandidateSituation infoCandidateSituation =
                    InfoCandidateSituation.newInfoFromDomain((CandidateSituation) situationIterator.next());
            situations.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE))) {

                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
            }

        }
        infoMasterDegreeCandidate.setSituationList(situations);
        return infoMasterDegreeCandidate;
    }
}