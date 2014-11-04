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
/**
 * 
 */
package org.fenixedu.academic.service.services.administrativeOffice.candidacy;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.List;

import org.fenixedu.academic.domain.candidacy.SubstituteCandidacySituation;
import org.fenixedu.academic.domain.util.workflow.StateMachine;
import org.fenixedu.academic.dto.administrativeOffice.candidacy.SelectDFACandidacyBean;
import org.fenixedu.academic.predicate.RolePredicates;

import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SelectCandidacies {

    @Atomic
    public static void run(List<SelectDFACandidacyBean> admittedCandidacies, List<SelectDFACandidacyBean> substituteCandidacies,
            List<SelectDFACandidacyBean> notAdmittedCandidacies) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        createNewCandidacySituations(notAdmittedCandidacies);
        createNewCandidacySituations(admittedCandidacies);
        createNewCandidacySituations(substituteCandidacies);

        fillSubstituteCandidaciesOrder(substituteCandidacies);

    }

    private static void fillSubstituteCandidaciesOrder(List<SelectDFACandidacyBean> substituteCandidacies) {
        if (substituteCandidacies != null && !substituteCandidacies.isEmpty()) {
            for (SelectDFACandidacyBean candidacyBean : substituteCandidacies) {
                SubstituteCandidacySituation candidacySituation =
                        (SubstituteCandidacySituation) candidacyBean.getCandidacy().getActiveCandidacySituation();
                candidacySituation.setCandidacyOrder(candidacyBean.getOrder());
            }
        }
    }

    private static void createNewCandidacySituations(List<SelectDFACandidacyBean> candidacies) {
        if (candidacies != null && !candidacies.isEmpty()) {
            for (SelectDFACandidacyBean candidacyBean : candidacies) {
                StateMachine.execute(candidacyBean.getCandidacy().getActiveCandidacySituation(), candidacyBean);
                candidacyBean.getCandidacy().getActiveCandidacySituation().setRemarks(candidacyBean.getRemarks());
            }
        }
    }

}