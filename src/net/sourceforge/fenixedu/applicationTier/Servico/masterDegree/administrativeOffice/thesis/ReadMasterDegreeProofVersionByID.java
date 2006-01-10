package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadMasterDegreeProofVersionByID implements IService {

    public Object run(Integer masterDegreeProofVersionID) throws FenixServiceException,
            ExcepcaoPersistencia {
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        MasterDegreeProofVersion masterDegreeProofVersion = (MasterDegreeProofVersion) sp
                .getIPersistentMasterDegreeProofVersion().readByOID(MasterDegreeProofVersion.class,
                        masterDegreeProofVersionID);

        if (masterDegreeProofVersion == null)
            throw new NonExistingServiceException(
                    "error.exception.masterDegree.nonExistingMasterDegreeProofVersion");

        return InfoMasterDegreeProofVersion.newInfoFromDomain(masterDegreeProofVersion);
    }
}