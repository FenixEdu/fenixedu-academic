package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidatesForSelection implements IService {

	public List run(Integer executionDegreeID, List situations)
			throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = null;
		List resultTemp = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		// Read the candidates

		IPersistentExecutionDegree executionDegreeDAO = sp
				.getIPersistentExecutionDegree();
		IExecutionDegree executionDegree = (ExecutionDegree) executionDegreeDAO
				.readByOID(ExecutionDegree.class, executionDegreeID);

		resultTemp = sp.getIPersistentCandidateSituation()
				.readActiveSituationsByExecutionDegreeAndNames(
						executionDegree.getIdInternal(), situations);

		if ((resultTemp == null) || (resultTemp.size() == 0)) {
			throw new NonExistingServiceException();
		}

		Iterator candidateIterator = resultTemp.iterator();
		List result = new ArrayList();
		while (candidateIterator.hasNext()) {
			ICandidateSituation candidateSituation = (ICandidateSituation) candidateIterator
					.next();
			result.add(InfoMasterDegreeCandidateWithInfoPerson
					.newInfoFromDomain(candidateSituation
							.getMasterDegreeCandidate()));
		}

		return result;

	}

}