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

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Filtro.CandidateApprovalAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ApproveCandidates {

    protected void run(String[] situations, String[] ids, String[] remarks, String[] substitutes) {

        for (int i = 0; i < situations.length; i++) {

            MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(ids[i]);
            CandidateSituation candidateSituationOldFromBD = masterDegreeCandidate.getActiveCandidateSituation();

            candidateSituationOldFromBD.setValidation(new State(State.INACTIVE));

            if ((substitutes[i] != null) && (substitutes[i].length() > 0)) {
                masterDegreeCandidate.setSubstituteOrder(new Integer(substitutes[i]));
            }

            // Create the new Candidate Situation

            CandidateSituation candidateSituation = new CandidateSituation();
            candidateSituation.setDate(Calendar.getInstance().getTime());
            candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
            candidateSituation.setRemarks(remarks[i]);
            candidateSituation.setSituation(new SituationName(situations[i]));
            candidateSituation.setValidation(new State(State.ACTIVE));

            // masterDegreeCandidate.getSituations().add(candidateSituation);

        }

    }

    // Service Invokers migrated from Berserk

    private static final ApproveCandidates serviceInstance = new ApproveCandidates();

    @Atomic
    public static void runApproveCandidates(String[] situations, String[] ids, String[] remarks, String[] substitutes)
            throws NotAuthorizedException {
        CandidateApprovalAuthorizationFilter.instance.execute(situations, ids, remarks, substitutes);
        serviceInstance.run(situations, ids, remarks, substitutes);
    }

}