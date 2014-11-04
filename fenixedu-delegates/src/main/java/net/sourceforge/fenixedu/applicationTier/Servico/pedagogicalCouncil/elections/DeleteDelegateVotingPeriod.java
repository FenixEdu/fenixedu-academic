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
package org.fenixedu.academic.service.services.pedagogicalCouncil.elections;

import static org.fenixedu.academic.predicate.AccessControl.check;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.pedagogicalCouncil.elections.ElectionPeriodBean;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.elections.DelegateElection;
import org.fenixedu.academic.domain.elections.DelegateElectionVotingPeriod;
import org.fenixedu.academic.domain.elections.YearDelegateElection;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteDelegateVotingPeriod {

    @Atomic
    public static void run(ElectionPeriodBean bean) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        try {
            DelegateElection election = bean.getElection();
            DelegateElectionVotingPeriod votingPeriod;
            if (election.hasVotingPeriod(bean.getStartDate(), bean.getEndDate())) {
                votingPeriod = election.getVotingPeriod(bean.getStartDate(), bean.getEndDate());
            } else {
                votingPeriod = election.getLastVotingPeriod();
            }
            election.deleteVotingPeriod(votingPeriod, bean.getRemoveCandidacyPeriod());
        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage(), ex.getArgs());
        }
    }

    @Atomic
    public static void run(ElectionPeriodBean bean, String degreeOID) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final Degree degree = FenixFramework.getDomainObject(degreeOID);

        DelegateElection election =
                YearDelegateElection.getYearDelegateElectionWithLastCandidacyPeriod(degree, executionYear,
                        bean.getCurricularYear());

        if (election != null) {
            bean.setElection(election);
        } else {
            throw new FenixServiceException("error.elections.edit.electionNotFound", new String[] { degree.getSigla(),
                    bean.getCurricularYear().getYear().toString() });
        }

        run(bean);
    }
}