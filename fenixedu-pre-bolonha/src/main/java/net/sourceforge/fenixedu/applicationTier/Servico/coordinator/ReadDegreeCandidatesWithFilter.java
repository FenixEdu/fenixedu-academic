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
package org.fenixedu.academic.service.services.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.InfoCandidateSituation;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidate;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidateWithInfoPerson;
import org.fenixedu.academic.domain.CandidateSituation;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.domain.studentCurricularPlan.Specialization;
import org.fenixedu.academic.util.PrintAllCandidatesFilter;
import org.fenixedu.academic.util.SituationName;
import org.fenixedu.academic.util.State;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadDegreeCandidatesWithFilter {

    @Atomic
    public static List run(String degreeCurricularPlanId, PrintAllCandidatesFilter filterBy, String filterValue)
            throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        return createInfoMasterDegreeCandidateFromDomain(getMasterDegreeCandidates(degreeCurricularPlan, filterBy, filterValue));
    }

    private static Set<MasterDegreeCandidate> getMasterDegreeCandidates(DegreeCurricularPlan degreeCurricularPlan,
            PrintAllCandidatesFilter filterBy, String filterValue) {

        switch (filterBy) {
        case FILTERBY_SPECIALIZATION_VALUE:
            return MasterDegreeCandidate.readMasterDegreeCandidatesBySpecialization(degreeCurricularPlan, Specialization.valueOf(filterValue));

        case FILTERBY_SITUATION_VALUE:
            return MasterDegreeCandidate.readMasterDegreeCandidatesBySituatioName(degreeCurricularPlan, new SituationName(filterValue));

        case FILTERBY_GIVESCLASSES_VALUE:
            return MasterDegreeCandidate.readMasterDegreeCandidatesByCourseAssistant(degreeCurricularPlan, true);

        case FILTERBY_DOESNTGIVESCLASSES_VALUE:
            return MasterDegreeCandidate.readMasterDegreeCandidatesByCourseAssistant(degreeCurricularPlan, false);

        default:
            return null;
        }
    }

    private static List createInfoMasterDegreeCandidateFromDomain(Set<MasterDegreeCandidate> masterDegreeCandidates) {

        final State candidateSituationState = new State(State.ACTIVE);
        final List<InfoMasterDegreeCandidate> result = new ArrayList<InfoMasterDegreeCandidate>();

        for (final MasterDegreeCandidate masterDegreeCandidate : masterDegreeCandidates) {

            final InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                    InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);

            final List<InfoCandidateSituation> candidateSituations = new ArrayList<InfoCandidateSituation>();
            for (final CandidateSituation candidateSituation : masterDegreeCandidate.getSituationsSet()) {

                final InfoCandidateSituation infoCandidateSituation =
                        InfoCandidateSituation.newInfoFromDomain(candidateSituation);
                candidateSituations.add(infoCandidateSituation);

                if (candidateSituation.getValidation().equals(candidateSituationState)) {
                    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
                }
            }

            infoMasterDegreeCandidate.setSituationList(candidateSituations);
            result.add(infoMasterDegreeCandidate);
        }
        return result;
    }
}