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
/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class ReadPublishedFinalDegreeWorkProposalHeaders {

    @Atomic
    public static List<FinalDegreeWorkProposalHeader> run(String executionDegreeOID) {
        final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeOID);
        if (executionDegree != null && executionDegree.hasScheduling()) {
            final Set<Proposal> finalDegreeWorkProposals = executionDegree.getScheduling().findPublishedProposals();

            for (final Proposal proposal : finalDegreeWorkProposals) {
                result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal, executionDegree));
            }
        }

        return result;
    }

}