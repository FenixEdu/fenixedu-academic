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
package org.fenixedu.academic.service.services.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.dto.InfoCandidateSituation;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidate;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidateWithInfoPerson;
import org.fenixedu.academic.domain.CandidateSituation;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.util.State;
import pt.ist.fenixframework.FenixFramework;

public class ReadDegreeCandidates {

    public static List run(InfoExecutionDegree infoExecutionDegree) {
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        return createInfoMasterDegreeCandidates(executionDegree.getMasterDegreeCandidatesSet());
    }

    public static List run(String degreeCurricularPlanId) {
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        return createInfoMasterDegreeCandidates(MasterDegreeCandidate.readMasterDegreeCandidates(degreeCurricularPlan));
    }

    private static List createInfoMasterDegreeCandidates(final Set<MasterDegreeCandidate> masterDegreeCandidates) {
        final State activeCandidateSituationState = new State(State.ACTIVE);
        final List<InfoMasterDegreeCandidate> result = new ArrayList<InfoMasterDegreeCandidate>();

        for (final MasterDegreeCandidate masterDegreeCandidate : masterDegreeCandidates) {
            InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                    InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);

            final List<InfoCandidateSituation> infoCandidateSituations = new ArrayList<InfoCandidateSituation>();
            for (final CandidateSituation candidateSituation : masterDegreeCandidate.getSituationsSet()) {
                final InfoCandidateSituation infoCandidateSituation =
                        InfoCandidateSituation.newInfoFromDomain(candidateSituation);
                infoCandidateSituations.add(infoCandidateSituation);

                if (candidateSituation.getValidation().equals(activeCandidateSituationState)) {
                    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
                }
            }
            infoMasterDegreeCandidate.setSituationList(infoCandidateSituations);
            result.add(infoMasterDegreeCandidate);
        }
        return result;
    }
}