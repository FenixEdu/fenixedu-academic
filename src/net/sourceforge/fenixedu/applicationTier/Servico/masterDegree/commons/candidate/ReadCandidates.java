package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidates extends Service {

	public List run(String[] candidateList) throws FenixServiceException, ExcepcaoPersistencia {

		List result = new ArrayList();

		// Read the admited candidates
		int size = candidateList.length;
		int i = 0;
		for (i = 0; i < size; i++) {

			result.add(InfoMasterDegreeCandidateWithInfoPerson
					.newInfoFromDomain(rootDomainObject.readMasterDegreeCandidateByOID(new Integer(candidateList[i]))));
		}

		return result;
	}
}