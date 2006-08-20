/*
 * Created on 18/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class ReadStudentsWithEnrollmentInCurrentSemester extends Service {

    public ReadStudentsWithEnrollmentInCurrentSemester() {
    }

    public List run(Integer fromNumber, Integer toNumber) throws ExcepcaoPersistencia {
        List infoStudentList = new ArrayList();
        List degreeNames = new ArrayList();
        List allStudentsData = new ArrayList();

        List studentsList = Registration.readAllStudentsBetweenNumbers(fromNumber, toNumber);

        for (int iter = 0; iter < studentsList.size(); iter++) {
            Registration student = (Registration) studentsList.get(iter);
            // TODO se ele está inscrito no semestre actual é porque já pagou as
            // propinas...
            if (student.getPayedTuition().booleanValue() && studentHasEnrollments(student)) {
                InfoStudent infoStudentWithInfoPerson = InfoStudent
                        .newInfoFromDomain(student);
                infoStudentList.add(infoStudentWithInfoPerson);
                degreeNames.add(student.getActiveStudentCurricularPlan().getDegreeCurricularPlan()
                        .getDegree().getNome());
            }
        }

        allStudentsData.add(infoStudentList);
        allStudentsData.add(degreeNames);
        return allStudentsData;
    }

    private boolean studentHasEnrollments(Registration student) throws ExcepcaoPersistencia {

        ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();

        List<Enrolment> enrollments = student.getActiveStudentCurricularPlan()
                .getEnrolmentsByExecutionPeriod(executionPeriod);

        for (int iter = 0; iter < enrollments.size(); iter++) {
            Enrolment enrollment = (Enrolment) enrollments.get(iter);
            if (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)
                    || enrollment.getEnrollmentState().equals(EnrollmentState.TEMPORARILY_ENROLLED)
                    || enrollment.getEnrollmentState().equals(EnrollmentState.ENROLLED))
                return true;
        }
        return false;
    }

}