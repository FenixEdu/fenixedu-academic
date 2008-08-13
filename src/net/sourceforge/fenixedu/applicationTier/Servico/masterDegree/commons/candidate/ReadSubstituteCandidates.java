package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.util.SituationName;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadSubstituteCandidates extends Service {

    public List<InfoMasterDegreeCandidate> run(String[] candidateList, String[] ids) throws FenixServiceException {

	List<InfoMasterDegreeCandidate> result = new ArrayList<InfoMasterDegreeCandidate>();

	// Read the substitute candidates
	int size = candidateList.length;

	for (int i = 0; i < size; i++) {
	    if (candidateList[i].equals(SituationName.SUPLENTE_STRING)
		    || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING)
		    || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING)
		    || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING)) {

		Integer idInternal = new Integer(ids[i]);

		MasterDegreeCandidate masterDegreeCandidateToWrite = rootDomainObject.readMasterDegreeCandidateByOID(idInternal);
		result.add(InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidateToWrite));
	    }
	}

	return result;
    }

}
