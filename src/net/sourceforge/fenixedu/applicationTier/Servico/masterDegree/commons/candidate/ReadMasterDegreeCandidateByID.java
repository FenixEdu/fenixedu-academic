package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadMasterDegreeCandidateByID implements IService {

    public InfoMasterDegreeCandidate run(Integer candidateID) throws ExcepcaoPersistencia {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) ps
                .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                        candidateID);

        return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);
    }

}
