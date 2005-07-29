package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangeDegree implements IService {

    public void run(final String employeeUsername, final Integer studentNumber,
            final Integer executionDegreeToChangeTo, final Set<Integer> enrolementsToTransferIds,
            final Set<Integer> enrolementsToMaintainIds, final Set<Integer> enrolementsToDeleteIds,
            final Date newStudentCurricularPlanStartDate)
            throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();
        final IPersistentExecutionDegree persistentExecutionDegree = persistentSupport
                .getIPersistentExecutionDegree();
        final IPersistentEnrollment persistentEnrollment = persistentSupport.getIPersistentEnrolment();

        final IPerson personEmployee = persistentPerson.lerPessoaPorUsername(employeeUsername);
        final IEmployee employee = personEmployee.getEmployee();

        final IStudentCurricularPlan currentActiveStudentCurricularPlan = persistentStudentCurricularPlan
                .readActiveStudentCurricularPlan(studentNumber, DegreeType.DEGREE);
        currentActiveStudentCurricularPlan.setCurrentState(StudentCurricularPlanState.INCOMPLETE);

        final IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                ExecutionDegree.class, executionDegreeToChangeTo);
        final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        final IStudentCurricularPlan newActiveStudentCurricularPlan = degreeCurricularPlan
                .getNewStudentCurricularPlan();
        newActiveStudentCurricularPlan.setBranch(null);
        newActiveStudentCurricularPlan.setClassification(Double.valueOf(0));
        newActiveStudentCurricularPlan.setCompletedCourses(0);
        newActiveStudentCurricularPlan.setCreditsInSecundaryArea(0);
        newActiveStudentCurricularPlan.setCreditsInSpecializationArea(0);
        newActiveStudentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE);
        newActiveStudentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);
        newActiveStudentCurricularPlan.setEmployee(employee);
        newActiveStudentCurricularPlan.setGivenCredits(Double.valueOf(0));
        newActiveStudentCurricularPlan.setMasterDegreeThesis(null);
        newActiveStudentCurricularPlan.setObservations(null);
        newActiveStudentCurricularPlan.setSecundaryBranch(null);
        newActiveStudentCurricularPlan.setSpecialization(null);
        newActiveStudentCurricularPlan.setStartDate(newStudentCurricularPlanStartDate);
        newActiveStudentCurricularPlan.setStudent(currentActiveStudentCurricularPlan.getStudent());
        newActiveStudentCurricularPlan.setWhen(Calendar.getInstance().getTime());

        for (final Integer enrolementID : enrolementsToTransferIds) {
            final IEnrolment enrolement = (IEnrolment) persistentEnrollment.readByOID(Enrolment.class,
                    enrolementID);
            enrolement.setStudentCurricularPlan(newActiveStudentCurricularPlan);
        }

        for (final Integer enrolementID : enrolementsToDeleteIds) {
            final IEnrolment enrolment = (IEnrolment) persistentEnrollment.readByOID(Enrolment.class,
                    enrolementID);
            enrolment.delete();
        }

    }

}