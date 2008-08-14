package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadMasterDegreeCandidateByID extends Service {

    public InfoMasterDegreeCandidate run(Integer candidateID) {
	MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);
	return (masterDegreeCandidate != null) ? InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate)
		: null;
    }

}
