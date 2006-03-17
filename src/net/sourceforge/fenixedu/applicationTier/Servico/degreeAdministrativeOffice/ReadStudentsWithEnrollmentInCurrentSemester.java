/*
 * Created on 18/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;

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

        IPersistentStudent pStudent = persistentSupport.getIPersistentStudent();
        List studentsList = pStudent.readAllBetweenNumbers(fromNumber, toNumber);

        for (int iter = 0; iter < studentsList.size(); iter++) {
            Student student = (Student) studentsList.get(iter);
            //TODO se ele está inscrito no semestre actual é porque já pagou as
            // propinas...
            if (student.getPayedTuition().booleanValue() && studentHasEnrollments(student)) {
                InfoStudent infoStudentWithInfoPerson = InfoStudentWithInfoPerson
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

    /**
     * @param student
     * @param persistentSupport
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private boolean studentHasEnrollments(Student student)
            throws ExcepcaoPersistencia {
        IPersistentExecutionPeriod pExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        ExecutionPeriod executionPeriod = pExecutionPeriod.readActualExecutionPeriod();

        
        List<Enrolment> enrollments = student.getActiveStudentCurricularPlan().getEnrolmentsByExecutionPeriod(executionPeriod); 

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