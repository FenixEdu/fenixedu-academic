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
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ReadCandidateEnrolmentsByCandidateIDAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree;
import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadCandidateEnrolmentsByCandidateID {

    protected List run(String candidateID) throws FenixServiceException {
        List result = new ArrayList();

        MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);

        if (masterDegreeCandidate == null) {
            throw new NonExistingServiceException();
        }

        Collection candidateEnrolments = masterDegreeCandidate.getCandidateEnrolments();

        if (candidateEnrolments == null) {
            throw new NonExistingServiceException();
        }

        for (final Iterator candidateEnrolmentIterator = candidateEnrolments.iterator(); candidateEnrolmentIterator.hasNext();) {
            CandidateEnrolment candidateEnrolmentTemp = (CandidateEnrolment) candidateEnrolmentIterator.next();
            InfoCandidateEnrolment infoCandidateEnrolment =
                    InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree
                            .newInfoFromDomain(candidateEnrolmentTemp);
            result.add(infoCandidateEnrolment);
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCandidateEnrolmentsByCandidateID serviceInstance = new ReadCandidateEnrolmentsByCandidateID();

    @Atomic
    public static List runReadCandidateEnrolmentsByCandidateID(String candidateID) throws FenixServiceException,
            NotAuthorizedException {
        ReadCandidateEnrolmentsByCandidateIDAuthorizationFilter.instance.execute(candidateID);
        return serviceInstance.run(candidateID);
    }

}