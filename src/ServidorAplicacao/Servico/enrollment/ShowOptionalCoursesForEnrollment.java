/*
 * Created on Jul 22, 2004
 *
 */
package ServidorAplicacao.Servico.enrollment;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriodWithInfoExecutionYear;
import DataBeans.InfoStudentCurricularPlanWithInfoStudent;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrollmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *  
 */
public class ShowOptionalCoursesForEnrollment extends
        ShowAvailableCurricularCoursesWithoutEnrollmentPeriod implements IService {

    public ShowOptionalCoursesForEnrollment() {
    }

    public InfoStudentEnrollmentContext run(Integer studentId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = persistentSuport.getIPersistentStudent();

            IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentId);
            IExecutionPeriod executionPeriod = getExecutionPeriod(null);
            IStudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();

            InfoStudentEnrollmentContext infoStudentEnrollmentContext = new InfoStudentEnrollmentContext();
            infoStudentEnrollmentContext
                    .setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudent
                            .newInfoFromDomain(studentCurricularPlan));
            infoStudentEnrollmentContext.setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear
                    .newInfoFromDomain(executionPeriod));
            infoStudentEnrollmentContext
                    .setStudentCurrentSemesterInfoEnrollments(getStudentEnrollmentsWithStateEnrolledInExecutionPeriod(
                            studentCurricularPlan, executionPeriod));
            infoStudentEnrollmentContext
                    .setCurricularCourses2Enroll(getInfoCurricularCoursesToEnrollFromCurricularCourses(
                            studentCurricularPlan, executionPeriod, studentCurricularPlan
                                    .getOptionalCurricularCoursesToEnroll(executionPeriod)));
            return infoStudentEnrollmentContext;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}