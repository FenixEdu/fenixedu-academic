package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ChangeMasterDegreeProof implements IService {

    public void run(IUserView userView, Integer studentCurricularPlanID, Date proofDate,
            Date thesisDeliveryDate, MasterDegreeClassification finalResult,
            Integer attachedCopiesNumber, List<Integer> teacherJuriesNumbers,
            List<Integer> externalJuriesIDs) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) sp
                .getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class,
                        studentCurricularPlanID);

        IMasterDegreeThesis storedMasterDegreeThesis = sp.getIPersistentMasterDegreeThesis()
                .readByStudentCurricularPlan(studentCurricularPlanID);
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
            sp.getIPersistentMasterDegreeProofVersion().simpleLockWrite(storedMasterDegreeProofVersion);
            storedMasterDegreeProofVersion.setCurrentState(new State(State.INACTIVE));
        }

        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        IEmployee employee = sp.getIPersistentEmployee().readByPerson(person.getIdInternal().intValue());

        List<ITeacher> teacherJuries = (List<ITeacher>) sp.getIPersistentTeacher().readByNumbers(
                teacherJuriesNumbers);
        List<IExternalPerson> externalJuries = (List<IExternalPerson>) sp.getIPersistentExternalPerson()
                .readByIDs(externalJuriesIDs);

        IMasterDegreeProofVersion masterDegreeProofVersion = DomainFactory.makeMasterDegreeProofVersion(
                storedMasterDegreeThesis, employee, new Date(), proofDate, thesisDeliveryDate,
                finalResult, attachedCopiesNumber, new State(State.ACTIVE), teacherJuries,
                externalJuries);

    }

}
