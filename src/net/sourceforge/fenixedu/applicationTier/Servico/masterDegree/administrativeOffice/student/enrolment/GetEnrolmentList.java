package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

public class GetEnrolmentList extends Service {

    public List<InfoEnrolment> run(Integer studentCurricularPlanID, EnrollmentState enrollmentState) {

	final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
	for (final Enrolment enrolment : getStudentCurricularPlan(studentCurricularPlanID)
		.getEnrolmentsByState(enrollmentState)) {
	    if (enrolment.isExtraCurricular()) {
		continue;
	    }
	    if (!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
		result.add(InfoEnrolment.newInfoFromDomain(enrolment));
	    }
	}
	return result;
    }

    public List<InfoEnrolment> run(Integer studentCurricularPlanID) {
	final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
	for (final Enrolment enrolment : getStudentCurricularPlan(studentCurricularPlanID)
		.getEnrolments()) {
	    if (enrolment.isExtraCurricular()) {
		continue;
	    }
	    if (!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
		result.add(InfoEnrolment.newInfoFromDomain(enrolment));
	    }
	}
	return result;
    }

    public List<InfoEnrolment> run(Integer studentCurricularPlanID, EnrollmentState enrollmentState,
	    Boolean pTypeEnrolments) {

	if (!pTypeEnrolments.booleanValue()) {
	    return this.run(studentCurricularPlanID, enrollmentState);
	}

	final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();

	for (final Enrolment enrolment : getStudentCurricularPlan(studentCurricularPlanID)
		.getEnrolmentsByState(enrollmentState)) {
	    if (enrolment.isExtraCurricular()) {
		continue;
	    }
	    result.add(InfoEnrolment.newInfoFromDomain(enrolment));
	}
	return result;
    }

    private StudentCurricularPlan getStudentCurricularPlan(Integer studentCurricularPlanID) {
	return rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);
    }

}
