package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan extends Service {

    public List run(Integer executionDegreeOID) throws ExcepcaoPersistencia {
	final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();

	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);
	
	if (executionDegree.hasScheduling()) {
	    final List<Proposal> finalDegreeWorkProposals = executionDegree.getScheduling().getProposals();
	    
	    for (final Proposal proposal : finalDegreeWorkProposals) {
		result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal, executionDegree));
	    }
	}

	return result;
    }
    
}
