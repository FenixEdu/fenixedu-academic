/*
 * Created on 14/Mar/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentList extends Service {

	public List run(Integer studentCurricularPlanID, EnrollmentState enrollmentState)
			throws FenixServiceException, Exception {

		List enrolmentList = null;

		// Read the list

		enrolmentList = persistentSupport.getIPersistentEnrolment()
				.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlanID,
						enrollmentState);

		List result = new ArrayList();
		Iterator iterator = enrolmentList.iterator();

		while (iterator.hasNext()) {
			Enrolment enrolment = (Enrolment) iterator.next();
			if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
				continue;
			}

			if (!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
				InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod
						.newInfoFromDomain(enrolment);
				result.add(infoEnrolment);
			}
		}

		return result;
	}

	public List run(Integer studentCurricularPlanID) throws FenixServiceException, Exception {

		List enrolmentList = null;

		// Read the list

		StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentObject.readByOID(StudentCurricularPlan.class,
						studentCurricularPlanID);
		enrolmentList = studentCurricularPlan.getEnrolments();

		List result = new ArrayList();
		Iterator iterator = enrolmentList.iterator();

		while (iterator.hasNext()) {
			Enrolment enrolment = (Enrolment) iterator.next();
			if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
				continue;
			}

			if (!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
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

		// Read the list
		List enrolmentList = persistentSupport.getIPersistentEnrolment()
				.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlanID,
						enrollmentState);

		// clone
		List result = new ArrayList();
		for (Iterator iter = enrolmentList.iterator(); iter.hasNext();) {
			Enrolment enrolment = (Enrolment) iter.next();
			if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
				continue;
			}

			InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod
					.newInfoFromDomain(enrolment);
			result.add(infoEnrolment);
		}

		return result;
	}

}