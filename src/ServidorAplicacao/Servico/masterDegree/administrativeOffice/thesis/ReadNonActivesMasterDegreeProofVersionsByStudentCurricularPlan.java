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
public class ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan implements IService {

    public List run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException {
        List infoMasterDegreeProofVersions = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlan studentCurricularPlan = Cloner
                    .copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

            List masterDegreeProofVersions = sp.getIPersistentMasterDegreeProofVersion()
                    .readNotActiveByStudentCurricularPlan(studentCurricularPlan);

            if (masterDegreeProofVersions.isEmpty() == false) {
                infoMasterDegreeProofVersions = Cloner
                        .copyListIMasterDegreeProofVersion2ListInfoMasterDegreeProofVersion(masterDegreeProofVersions);
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoMasterDegreeProofVersions;
    }
}