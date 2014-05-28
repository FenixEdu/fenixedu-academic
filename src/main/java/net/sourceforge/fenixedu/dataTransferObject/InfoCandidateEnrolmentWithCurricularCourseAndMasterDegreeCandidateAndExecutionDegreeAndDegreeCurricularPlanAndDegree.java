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

import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

/**
 * @author Luis Cruz
 * 
 */
public class InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree
        extends InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidate {

    @Override
    public void copyFromDomain(CandidateEnrolment candidateEnrolment) {
        super.copyFromDomain(candidateEnrolment);
        if (candidateEnrolment != null) {
            ExecutionDegree executionDegree = candidateEnrolment.getMasterDegreeCandidate().getExecutionDegree();
            getInfoMasterDegreeCandidate().setInfoExecutionDegree(InfoExecutionDegree.newInfoFromDomain(executionDegree));
        }
    }

    public static InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree newInfoFromDomain(
            CandidateEnrolment candidateEnrolment) {
        InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree infoCandidateEnrolment =
                null;
        if (candidateEnrolment != null) {
            infoCandidateEnrolment =
                    new InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree();
            infoCandidateEnrolment.copyFromDomain(candidateEnrolment);
        }
        return infoCandidateEnrolment;
    }

}