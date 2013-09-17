/*
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
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