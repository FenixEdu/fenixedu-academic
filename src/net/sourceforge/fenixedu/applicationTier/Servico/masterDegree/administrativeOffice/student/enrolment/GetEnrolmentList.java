/*
 * Created on 14/Mar/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;

import java.util.ArrayList;
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

	public List<InfoEnrolment> run(Integer studentCurricularPlanID, EnrollmentState enrollmentState) throws FenixServiceException, Exception {
        List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
        
		StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);
		List<Enrolment> enrolmentList = studentCurricularPlan.getEnrolmentsByState(enrollmentState);
		for (Enrolment enrolment : enrolmentList) {
			if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
				continue;
			}

			if (!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
				InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod.newInfoFromDomain(enrolment);
				result.add(infoEnrolment);
			}
		}

		return result;
	}

	public List<InfoEnrolment> run(Integer studentCurricularPlanID) throws FenixServiceException, Exception {
		List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();

        StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);
        for (Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
			if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
				continue;
			}

			if (!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
				InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod.newInfoFromDomain(enrolment);
				result.add(infoEnrolment);
			}
		}

		return result;
	}

	public List<InfoEnrolment> run(Integer studentCurricularPlanID, EnrollmentState enrollmentState, Boolean pTypeEnrolments) throws FenixServiceException, Exception {
		if (!pTypeEnrolments.booleanValue()) {
			return this.run(studentCurricularPlanID, enrollmentState);
		}

		List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
		
        StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);
        List<Enrolment> enrolmentList = studentCurricularPlan.getEnrolmentsByState(enrollmentState);
        for (Enrolment enrolment : enrolmentList) {
			if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
				continue;
			}

			InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndEvaluationsAndExecutionPeriod.newInfoFromDomain(enrolment);
			result.add(infoEnrolment);
		}

		return result;
	}

}
