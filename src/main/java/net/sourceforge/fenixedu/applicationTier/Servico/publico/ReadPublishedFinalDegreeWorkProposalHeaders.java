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
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Luis Cruz
 * 
 */
public class ReadPublishedFinalDegreeWorkProposalHeaders {

    @Service
    public static List<FinalDegreeWorkProposalHeader> run(Integer executionDegreeOID) {
        final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();

        final ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeOID);
        if (executionDegree != null && executionDegree.hasScheduling()) {
            final Set<Proposal> finalDegreeWorkProposals = executionDegree.getScheduling().findPublishedProposals();

            for (final Proposal proposal : finalDegreeWorkProposals) {
                result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal, executionDegree));
            }
        }

        return result;
    }

}