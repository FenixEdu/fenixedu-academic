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
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ChangeMasterDegreeProof extends Service {

    public void run(IUserView userView, Integer studentCurricularPlanID, Date proofDate,
            Date thesisDeliveryDate, MasterDegreeClassification finalResult,
            Integer attachedCopiesNumber, List<Integer> teacherJuriesNumbers,
            List<Integer> externalJuriesIDs) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) sp
                .getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class,
                        studentCurricularPlanID);

        MasterDegreeThesis storedMasterDegreeThesis = sp.getIPersistentMasterDegreeThesis()
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

        MasterDegreeProofVersion storedMasterDegreeProofVersion = sp
                .getIPersistentMasterDegreeProofVersion().readActiveByStudentCurricularPlan(
                        studentCurricularPlan);
        if (storedMasterDegreeProofVersion != null) {
            storedMasterDegreeProofVersion.setCurrentState(new State(State.INACTIVE));
        }

        Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        Employee employee = sp.getIPersistentEmployee().readByPerson(person.getIdInternal().intValue());

        List<Teacher> teacherJuries = (List<Teacher>) sp.getIPersistentTeacher().readByNumbers(
                teacherJuriesNumbers);
        List<ExternalPerson> externalJuries = (List<ExternalPerson>) sp.getIPersistentExternalPerson()
                .readByIDs(externalJuriesIDs);

        DomainFactory.makeMasterDegreeProofVersion(
                storedMasterDegreeThesis, employee, new Date(), proofDate, thesisDeliveryDate,
                finalResult, attachedCopiesNumber, new State(State.ACTIVE), teacherJuries,
                externalJuries);

    }

}
