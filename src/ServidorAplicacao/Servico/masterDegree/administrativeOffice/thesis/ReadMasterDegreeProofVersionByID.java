package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeProofVersion;
import Dominio.MasterDegreeProofVersion;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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