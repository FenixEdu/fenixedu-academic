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
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class ReadAvailableFinalDegreeWorkProposalHeadersForGroup {

    @Atomic
    public static List run(String groupOID) {
        check(RolePredicates.STUDENT_PREDICATE);
        final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();

        final FinalDegreeWorkGroup group = FenixFramework.getDomainObject(groupOID);

        if (group != null && group.getExecutionDegree() != null) {
            final Set<Proposal> finalDegreeWorkProposals = group.getExecutionDegree().getScheduling().findPublishedProposals();

            for (final Proposal proposal : finalDegreeWorkProposals) {
                if (!CollectionUtils.exists(group.getGroupProposals(), new PREDICATE_FIND_GROUP_PROPOSAL_BY_PROPOSAL(proposal))) {
                    result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal));
                }
            }
        }

        return result;
    }

    private static class PREDICATE_FIND_GROUP_PROPOSAL_BY_PROPOSAL implements Predicate {

        Proposal proposal;

        @Override
        public boolean evaluate(Object arg0) {
            GroupProposal groupProposal = (GroupProposal) arg0;
            return proposal.equals(groupProposal.getFinalDegreeWorkProposal());
        }

        public PREDICATE_FIND_GROUP_PROPOSAL_BY_PROPOSAL(Proposal proposal) {
            super();
            this.proposal = proposal;
        }
    }

}