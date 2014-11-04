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
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.service.filter.ReadCandidatesForSelectionAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidateWithInfoPerson;
import org.fenixedu.academic.domain.CandidateSituation;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.util.SituationName;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidatesForSelection {

    protected List run(String executionDegreeID, List<SituationName> situationNames) throws FenixServiceException {

        // Read the candidates

        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);

        List<CandidateSituation> resultTemp = MasterDegreeCandidate.getCandidateSituationsInSituation(executionDegree, situationNames);

        if (resultTemp.isEmpty()) {
            throw new NonExistingServiceException();
        }

        Iterator candidateIterator = resultTemp.iterator();
        List result = new ArrayList();
        while (candidateIterator.hasNext()) {
            CandidateSituation candidateSituation = (CandidateSituation) candidateIterator.next();
            result.add(InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(candidateSituation.getMasterDegreeCandidate()));
        }

        return result;

    }

    // Service Invokers migrated from Berserk

    private static final ReadCandidatesForSelection serviceInstance = new ReadCandidatesForSelection();

    @Atomic
    public static List runReadCandidatesForSelection(String executionDegreeID, List<SituationName> situationNames)
            throws FenixServiceException, NotAuthorizedException {
        ReadCandidatesForSelectionAuthorizationFilter.instance.execute(executionDegreeID, situationNames);
        return serviceInstance.run(executionDegreeID, situationNames);
    }

}