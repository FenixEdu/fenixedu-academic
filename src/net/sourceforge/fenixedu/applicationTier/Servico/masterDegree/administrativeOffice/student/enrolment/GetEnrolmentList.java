/*
 * Created on 14/Mar/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.CurricularCourseType;
import net.sourceforge.fenixedu.util.EnrollmentState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
			if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
				continue;				
			}
			
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
			if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
				continue;				
			}
			
            if (!enrolment.getCurricularCourse().getType()
                    .equals(CurricularCourseType.P_TYPE_COURSE_OBJ)) {
                InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                        .newInfoFromDomain(enrolment);
                result.add(infoEnrolment);
            }
        }

        return result;
    }

    public List run(Integer studentCurricularPlanID, EnrollmentState enrollmentState,
            Boolean pTypeEnrolments) throws FenixServiceException, Exception {

        if (!pTypeEnrolments.booleanValue()) {
            return this.run(studentCurricularPlanID, enrollmentState);
        }

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        // Read the list
        List enrolmentList = sp.getIPersistentEnrolment()
                .readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlanID,
                        enrollmentState);

        // clone
        List result = new ArrayList();
        for (Iterator iter = enrolmentList.iterator(); iter.hasNext();) {
            IEnrollment enrollment = (IEnrollment) iter.next();
			if (enrollment instanceof EnrolmentInExtraCurricularCourse) {
				continue;				
			}
			
            InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod
                    .newInfoFromDomain(enrollment);
            result.add(infoEnrolment);
        }

        return result;
    }

}