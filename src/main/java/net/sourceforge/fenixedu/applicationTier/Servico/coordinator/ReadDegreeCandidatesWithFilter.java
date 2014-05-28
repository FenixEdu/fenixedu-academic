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
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.PrintAllCandidatesFilter;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
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
            return degreeCurricularPlan.readMasterDegreeCandidatesBySpecialization(Specialization.valueOf(filterValue));

        case FILTERBY_SITUATION_VALUE:
            return degreeCurricularPlan.readMasterDegreeCandidatesBySituatioName(new SituationName(filterValue));

        case FILTERBY_GIVESCLASSES_VALUE:
            return degreeCurricularPlan.readMasterDegreeCandidatesByCourseAssistant(true);

        case FILTERBY_DOESNTGIVESCLASSES_VALUE:
            return degreeCurricularPlan.readMasterDegreeCandidatesByCourseAssistant(false);

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