package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.SituationName;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidatesForSelection extends Service {

	public List run(Integer executionDegreeID, List<SituationName> situationNames)
			throws FenixServiceException{

		// Read the candidates

		ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
        
        List<CandidateSituation> resultTemp = executionDegree.getCandidateSituationsInSituation(situationNames);
        
		if (resultTemp.isEmpty()) {
			throw new NonExistingServiceException();
		}

		Iterator candidateIterator = resultTemp.iterator();
		List result = new ArrayList();
		while (candidateIterator.hasNext()) {
			CandidateSituation candidateSituation = (CandidateSituation) candidateIterator
					.next();
			result.add(InfoMasterDegreeCandidateWithInfoPerson
					.newInfoFromDomain(candidateSituation
							.getMasterDegreeCandidate()));
		}

		return result;

	}

}