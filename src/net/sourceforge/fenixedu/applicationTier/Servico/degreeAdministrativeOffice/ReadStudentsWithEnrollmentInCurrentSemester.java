/*
 * Created on 18/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class ReadStudentsWithEnrollmentInCurrentSemester implements IService {

    public ReadStudentsWithEnrollmentInCurrentSemester() {
    }

    public List run(Integer fromNumber, Integer toNumber) throws ExcepcaoPersistencia {

        ISuportePersistente sp = null;

        List infoStudentList = new ArrayList();
        List degreeNames = new ArrayList();
        List allStudentsData = new ArrayList();

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent pStudent = sp.getIPersistentStudent();
        List studentsList = pStudent.readAllBetweenNumbers(fromNumber, toNumber);

        for (int iter = 0; iter < studentsList.size(); iter++) {
            IStudent student = (IStudent) studentsList.get(iter);
            //TODO se ele está inscrito no semestre actual é porque já pagou as
            // propinas...
            if (student.getPayedTuition().booleanValue() && studentHasEnrollments(student, sp)) {
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
     * @param sp
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private boolean studentHasEnrollments(IStudent student, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        IPersistentEnrollment pEnrollment = sp.getIPersistentEnrolment();
        IPersistentExecutionPeriod pExecutionPeriod = sp.getIPersistentExecutionPeriod();
        IExecutionPeriod executionPeriod = pExecutionPeriod.readActualExecutionPeriod();

        List enrollments = pEnrollment.readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
                student.getActiveStudentCurricularPlan().getIdInternal(), executionPeriod.getIdInternal());

        for (int iter = 0; iter < enrollments.size(); iter++) {
            IEnrolment enrollment = (IEnrolment) enrollments.get(iter);
            if (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)
                    || enrollment.getEnrollmentState().equals(EnrollmentState.TEMPORARILY_ENROLLED)
                    || enrollment.getEnrollmentState().equals(EnrollmentState.ENROLLED))
                return true;
        }
        return false;
    }

}