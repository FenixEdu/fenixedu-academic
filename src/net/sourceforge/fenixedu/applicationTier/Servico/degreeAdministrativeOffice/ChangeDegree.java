package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangeDegree extends Service {

    public void run(final String employeeUsername, final Integer studentNumber,
            final Integer executionDegreeToChangeTo, final Set<Integer> enrolementsToTransferIds,
            final Set<Integer> enrolementsToMaintainIds, final Set<Integer> enrolementsToDeleteIds,
            final Date newStudentCurricularPlanStartDate)
            throws ExcepcaoPersistencia {

        final Person personEmployee = Person.readPersonByUsername(employeeUsername);
        final Employee employee = personEmployee.getEmployee();

        final Registration student = Registration.readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
        StudentCurricularPlan currentActiveStudentCurricularPlan = null;
        if(student != null) {
        	currentActiveStudentCurricularPlan = student.getActiveStudentCurricularPlan();
        }

        currentActiveStudentCurricularPlan.setCurrentState(StudentCurricularPlanState.INCOMPLETE);

        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeToChangeTo);
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        final StudentCurricularPlan newActiveStudentCurricularPlan = degreeCurricularPlan.getNewStudentCurricularPlan();
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

        final List<Enrolment> enrolments = new ArrayList<Enrolment>(currentActiveStudentCurricularPlan.getEnrolments());
        for (final Enrolment enrolment : enrolments) {
            final Integer enrolmentId = enrolment.getIdInternal();

            if (enrolementsToTransferIds.contains(enrolmentId)) {
                enrolment.setStudentCurricularPlan(newActiveStudentCurricularPlan);
            } else if (enrolementsToDeleteIds.contains(enrolmentId)) {
                enrolment.delete();
            } else if (enrolementsToMaintainIds.contains(enrolmentId)) {
                // nothing to do
            } else {
                // this should never happen
                throw new IllegalArgumentException("Unspecified operation for enrolment: " + enrolmentId);
            }
        }
    }

}