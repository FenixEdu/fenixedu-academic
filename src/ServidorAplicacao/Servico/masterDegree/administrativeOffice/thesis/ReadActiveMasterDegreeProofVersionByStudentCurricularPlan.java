package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeProofVersion;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ReadActiveMasterDegreeProofVersionByStudentCurricularPlan implements IService {

    public InfoMasterDegreeProofVersion run(InfoStudentCurricularPlan infoStudentCurricularPlan)
            throws FenixServiceException {
        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlan studentCurricularPlan = Cloner
                    .copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

            IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                    .getInstance();
            IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory
                    .getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());

            if (!masterDegreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan))
                throw new ScholarshipNotFinishedServiceException(
                        "error.exception.masterDegree.scholarshipNotFinished");

            IMasterDegreeProofVersion masterDegreeProofVersion = sp
                    .getIPersistentMasterDegreeProofVersion().readActiveByStudentCurricularPlan(
                            studentCurricularPlan);

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