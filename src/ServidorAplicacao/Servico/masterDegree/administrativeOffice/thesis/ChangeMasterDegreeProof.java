package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IEmployee;
import Dominio.IMasterDegreeProofVersion;
import Dominio.IMasterDegreeThesis;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.MasterDegreeProofVersion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.MasterDegreeClassification;
import Util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ChangeMasterDegreeProof implements IService {

    /**
     * The actor of this class.
     */
    public ChangeMasterDegreeProof() {
    }

    public void run(IUserView userView, InfoStudentCurricularPlan infoStudentCurricularPlan,
            Date proofDate, Date thesisDeliveryDate, MasterDegreeClassification finalResult,
            Integer attachedCopiesNumber, List infoTeacherJuries, List infoExternalPersonExternalJuries)
            throws FenixServiceException {
        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlan studentCurricularPlan = Cloner
                    .copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

            IMasterDegreeThesis storedMasterDegreeThesis = sp.getIPersistentMasterDegreeThesis()
                    .readByStudentCurricularPlan(studentCurricularPlan);
            if (storedMasterDegreeThesis == null) {
                throw new NonExistingServiceException(
                        "error.exception.masterDegree.nonExistentMasterDegreeThesis");
            }
            IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                    .getInstance();
            IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory
                    .getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());

            if (!masterDegreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan)) {
                throw new ScholarshipNotFinishedServiceException(
                        "error.exception.masterDegree.scholarshipNotFinished");
            }

            IMasterDegreeProofVersion storedMasterDegreeProofVersion = sp
                    .getIPersistentMasterDegreeProofVersion().readActiveByStudentCurricularPlan(
                            studentCurricularPlan);
            if (storedMasterDegreeProofVersion != null) {
                sp.getIPersistentMasterDegreeProofVersion().simpleLockWrite(
                        storedMasterDegreeProofVersion);
                storedMasterDegreeProofVersion.setCurrentState(new State(State.INACTIVE));
            }

            IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = sp.getIPersistentEmployee().readByPerson(
                    person.getIdInternal().intValue());
            List teacherJuries = Cloner.copyListInfoTeacher2ListITeacher(infoTeacherJuries);
            List externalJuries = Cloner
                    .copyListInfoExternalPerson2ListIExternalPerson(infoExternalPersonExternalJuries);

            IMasterDegreeProofVersion masterDegreeProofVersion = new MasterDegreeProofVersion(
                    storedMasterDegreeThesis, employee, new Timestamp(new Date().getTime()), proofDate,
                    thesisDeliveryDate, finalResult, attachedCopiesNumber, new State(State.ACTIVE),
                    teacherJuries, externalJuries);
            sp.getIPersistentMasterDegreeProofVersion().simpleLockWrite(masterDegreeProofVersion);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}