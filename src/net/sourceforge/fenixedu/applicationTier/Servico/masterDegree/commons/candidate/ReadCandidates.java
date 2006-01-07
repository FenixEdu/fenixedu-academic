package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidates implements IService {

	public List run(String[] candidateList) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = null;
		List result = new ArrayList();

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		// Read the admited candidates
		int size = candidateList.length;
		int i = 0;
		for (i = 0; i < size; i++) {

			result.add(InfoMasterDegreeCandidateWithInfoPerson
					.newInfoFromDomain((MasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate()
							.readByOID(MasterDegreeCandidate.class, new Integer(candidateList[i]))));
		}

		return result;
	}
}