package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import DataBeans.InfoStudentCurricularPlan;
import Dominio.IMasterDegreeThesisDataVersion;
import Dominio.IStudentCurricularPlan;
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
public class ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan implements IService {

    public InfoMasterDegreeThesisDataVersion run(InfoStudentCurricularPlan infoStudentCurricularPlan)
            throws FenixServiceException {
        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            //CLONER
            //            IStudentCurricularPlan studentCurricularPlan = Cloner
            //                    .copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);
            IStudentCurricularPlan studentCurricularPlan = InfoStudentCurricularPlan
                    .newDomainFromInfo(infoStudentCurricularPlan);
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = sp
                    .getIPersistentMasterDegreeThesisDataVersion().readActiveByStudentCurricularPlan(
                            studentCurricularPlan);

            if (masterDegreeThesisDataVersion == null)
                throw new NonExistingServiceException(
                        "error.exception.masterDegree.nonExistingMasterDegreeThesis");

            //CLONER
            //            infoMasterDegreeThesisDataVersion = Cloner
            //                    .copyIMasterDegreeThesisDataVersion2InfoMasterDegreeThesisDataVersion(masterDegreeThesisDataVersion);
            infoMasterDegreeThesisDataVersion = InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis
                    .newInfoFromDomain(masterDegreeThesisDataVersion);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoMasterDegreeThesisDataVersion;
    }
}