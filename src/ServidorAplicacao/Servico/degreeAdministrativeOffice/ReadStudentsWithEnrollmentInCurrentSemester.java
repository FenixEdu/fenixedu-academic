/*
 * Created on 18/Set/2004
 *
 */
package ServidorAplicacao.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentWithInfoPerson;
import Dominio.IEnrollment;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrollmentState;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class ReadStudentsWithEnrollmentInCurrentSemester implements IService {

    public ReadStudentsWithEnrollmentInCurrentSemester() {
    }

    public List run(Integer fromNumber, Integer toNumber) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = null;

        List infoStudentList = new ArrayList();
        List degreeNames = new ArrayList();
        List allStudentsData = new ArrayList();

        sp = SuportePersistenteOJB.getInstance();
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
                student.getActiveStudentCurricularPlan(), executionPeriod);

        for (int iter = 0; iter < enrollments.size(); iter++) {
            IEnrollment enrollment = (IEnrollment) enrollments.get(iter);
            if (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)
                    || enrollment.getEnrollmentState().equals(EnrollmentState.TEMPORARILY_ENROLED)
                    || enrollment.getEnrollmentState().equals(EnrollmentState.ENROLLED))
                return true;
        }
        return false;
    }

}