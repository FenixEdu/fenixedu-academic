package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadMasterDegreeCandidateByID extends Service {

    public InfoMasterDegreeCandidate run(Integer candidateID) throws ExcepcaoPersistencia {

        MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) persistentSupport
                .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                        candidateID);

        return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);
    }

}
