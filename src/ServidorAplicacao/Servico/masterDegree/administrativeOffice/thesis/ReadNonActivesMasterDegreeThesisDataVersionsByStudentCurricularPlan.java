package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ReadNonActivesMasterDegreeThesisDataVersionsByStudentCurricularPlan implements IService {

    public List run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException {
        List infoMasterDegreeThesisDataVersions = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlan studentCurricularPlan = Cloner
                    .copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);
            List masterDegreeThesisDataVersions = sp.getIPersistentMasterDegreeThesisDataVersion()
                    .readNotActivesVersionsByStudentCurricularPlan(studentCurricularPlan);

            if (masterDegreeThesisDataVersions.isEmpty() == false) {
                infoMasterDegreeThesisDataVersions = Cloner
                        .copyListIMasterDegreeThesisDataVersion2ListInfoMasterDegreeThesisDataVersion(masterDegreeThesisDataVersions);
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoMasterDegreeThesisDataVersions;
    }
}