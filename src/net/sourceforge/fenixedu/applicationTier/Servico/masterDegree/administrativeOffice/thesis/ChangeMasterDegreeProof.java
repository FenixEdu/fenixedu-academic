package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.MasterDegreeClassification;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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

            IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
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