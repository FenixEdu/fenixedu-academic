/*
 * Created on 14/Mar/2003
 *
 */

package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.enrolment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod;
import DataBeans.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import Dominio.IEnrollment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrollmentState;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentList implements IService {

    public List run(Integer studentCurricularPlanID, EnrollmentState enrollmentState)
            throws FenixServiceException, Exception {

        ISuportePersistente sp = null;
        List enrolmentList = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the list

            enrolmentList = sp.getIPersistentEnrolment()
                    .readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlanID,
                            enrollmentState);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        List result = new ArrayList();
        Iterator iterator = enrolmentList.iterator();

        while (iterator.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iterator.next();
            if (!enrolment.getCurricularCourse().getType()
                    .equals(CurricularCourseType.P_TYPE_COURSE_OBJ)) {
                InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod
                        .newInfoFromDomain(enrolment);
                result.add(infoEnrolment);
            }
        }

        return result;
    }

    public List run(Integer studentCurricularPlanID) throws FenixServiceException, Exception {

        ISuportePersistente sp = null;
        List enrolmentList = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the list

            enrolmentList = sp.getIPersistentEnrolment().readAllByStudentCurricularPlan(
                    studentCurricularPlanID);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        List result = new ArrayList();
        Iterator iterator = enrolmentList.iterator();

        while (iterator.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iterator.next();
            if (!enrolment.getCurricularCourse().getType()
                    .equals(CurricularCourseType.P_TYPE_COURSE_OBJ)) {
                InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                        .newInfoFromDomain(enrolment);
                result.add(infoEnrolment);
            }
        }

        return result;
    }

}