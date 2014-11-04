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
package org.fenixedu.academic.service.services.commons.degree;

import org.fenixedu.academic.service.filter.ReadExecutionDegreeByCandidateIDAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByCandidateID {

    protected InfoExecutionDegree run(String candidateID) throws NonExistingServiceException {

        MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);

        ExecutionDegree executionDegree =
                FenixFramework.getDomainObject(masterDegreeCandidate.getExecutionDegree().getExternalId());

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionDegreeByCandidateID serviceInstance = new ReadExecutionDegreeByCandidateID();

    @Atomic
    public static InfoExecutionDegree runReadExecutionDegreeByCandidateID(String candidateID) throws NonExistingServiceException,
            NotAuthorizedException {
        ReadExecutionDegreeByCandidateIDAuthorizationFilter.instance.execute(candidateID);
        return serviceInstance.run(candidateID);
    }

}