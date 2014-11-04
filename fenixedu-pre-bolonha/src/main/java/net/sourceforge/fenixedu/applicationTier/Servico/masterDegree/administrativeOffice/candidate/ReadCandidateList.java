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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.dto.InfoCandidateSituation;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidate;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidateWithInfoPerson;
import org.fenixedu.academic.domain.CandidateSituation;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.domain.studentCurricularPlan.Specialization;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.util.SituationName;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadCandidateList {

    @Atomic
    public static List run(String degreeName, Specialization degreeType, SituationName candidateSituation,
            Integer candidateNumber, String executionYearString) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearString);
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(degreeName);

        final Set<MasterDegreeCandidate> result;
        if (executionDegree == null && degreeType == null && candidateSituation == null && candidateNumber == null) {
            result = new HashSet<MasterDegreeCandidate>();
            for (final ExecutionDegree executionDegreeIter : executionYear.getExecutionDegreesSet()) {
                result.addAll(executionDegreeIter.getMasterDegreeCandidatesSet());
            }
        } else {
            result =
                    MasterDegreeCandidate.readByExecutionDegreeOrSpecializationOrCandidateNumberOrSituationName(executionDegree,
                            degreeType, candidateNumber, candidateSituation);
        }

        final List candidateList = new ArrayList();
        final Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            final MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) iterator.next();
            final InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                    InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);

            final Iterator situationIterator = masterDegreeCandidate.getSituationsSet().iterator();
            final List situations = new ArrayList();
            while (situationIterator.hasNext()) {
                final InfoCandidateSituation infoCandidateSituation =
                        InfoCandidateSituation.newInfoFromDomain((CandidateSituation) situationIterator.next());
                situations.add(infoCandidateSituation);
            }
            infoMasterDegreeCandidate.setSituationList(situations);

            executionDegree = masterDegreeCandidate.getExecutionDegree();
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;
    }
}