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
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.ExecutionDegreeCoordinatorOrScientificCouncilmemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import pt.ist.fenixframework.Atomic;

public class ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan {

    protected List run(final ExecutionDegree executionDegree) {
        final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();

        if (executionDegree.hasScheduling()) {
            final Collection<Proposal> finalDegreeWorkProposals = executionDegree.getScheduling().getProposals();

            for (final Proposal proposal : finalDegreeWorkProposals) {
                result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal, executionDegree));
            }
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan serviceInstance =
            new ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan();

    @Atomic
    public static List runReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan(ExecutionDegree executionDegree)
            throws NotAuthorizedException {
        try {
            ExecutionDegreeCoordinatorOrScientificCouncilmemberAuthorizationFilter.instance.execute(executionDegree);
            return serviceInstance.run(executionDegree);
        } catch (NotAuthorizedException ex1) {
            try {
                GEPAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionDegree);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}