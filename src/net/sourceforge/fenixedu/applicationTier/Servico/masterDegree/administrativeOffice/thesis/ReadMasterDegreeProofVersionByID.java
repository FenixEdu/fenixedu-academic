package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ReadMasterDegreeProofVersionByID implements IService {

    public Object run(Integer masterDegreeProofVersionID) throws FenixServiceException {
        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;
        IMasterDegreeProofVersion masterDegreeProofVersion = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            masterDegreeProofVersion = (IMasterDegreeProofVersion) sp
                    .getIPersistentMasterDegreeProofVersion().readByOID(MasterDegreeProofVersion.class,
                            masterDegreeProofVersionID);

            if (masterDegreeProofVersion == null)
                throw new NonExistingServiceException(
                        "error.exception.masterDegree.nonExistingMasterDegreeProofVersion");

            infoMasterDegreeProofVersion = Cloner
                    .copyIMasterDegreeProofVersion2InfoMasterDegreeProofVersion(masterDegreeProofVersion);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoMasterDegreeProofVersion;
    }
}