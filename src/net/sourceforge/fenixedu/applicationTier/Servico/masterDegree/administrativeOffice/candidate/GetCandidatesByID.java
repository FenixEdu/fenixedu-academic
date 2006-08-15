/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetCandidatesByID extends Service {

	public InfoMasterDegreeCandidate run(Integer candidateID) throws FenixServiceException, ExcepcaoPersistencia {
		MasterDegreeCandidate masterDegreeCandidate = null;

		if (candidateID == null) {
			throw new NonExistingServiceException();
		}

		masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);

		InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
				.newInfoFromDomain(masterDegreeCandidate);

		final ExecutionDegree executionDegree = masterDegreeCandidate.getExecutionDegree();
		final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
				.newInfoFromDomain(executionDegree);
		infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

		Iterator situationIterator = masterDegreeCandidate.getSituations().iterator();
		List situations = new ArrayList();
		while (situationIterator.hasNext()) {
			InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
					.newInfoFromDomain((CandidateSituation) situationIterator.next());
			situations.add(infoCandidateSituation);

			// Check if this is the Active Situation
			if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE))) {

				infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
			}

		}
		infoMasterDegreeCandidate.setSituationList(situations);
		return infoMasterDegreeCandidate;
	}
}