/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ListInformationBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class SearchStudentByCriteria extends Service {

    public List<StudentCurricularPlan> run(final ExecutionDegreeListBean executionDegreeBean,
	    final ListInformationBean ingressionInformationBean) {

	return getStudentByDegree(executionDegreeBean, ingressionInformationBean);

    }

    private List<StudentCurricularPlan> getStudentByDegree(ExecutionDegreeListBean executionDegreeBean,
	    ListInformationBean ingressionInformationBean) {

	final List<StudentCurricularPlan> registrations = new ArrayList<StudentCurricularPlan>();

	if (executionDegreeBean.getDegreeCurricularPlan() != null) {

	    if (ingressionInformationBean.getRegistrationAgreement() == null
		    && ingressionInformationBean.getRegistrationStateType() == null
		    && executionDegreeBean.getExecutionYear() == null) {

		registrations.addAll(executionDegreeBean.getDegreeCurricularPlan().getStudentCurricularPlans());
		Collections.sort(registrations, new BeanComparator("registration.number"));
		Collections.reverse(registrations);
		return registrations;
	    }

	    for (final StudentCurricularPlan studentCurricularPlan : executionDegreeBean.getDegreeCurricularPlan()
		    .getStudentCurricularPlans()) {

		if (executionDegreeBean.getExecutionYear() != null) {
		    final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionDegreeBean
			    .getExecutionYear().getYear());
		    if (!studentCurricularPlan.getRegistration().hasAnyEnrolmentsIn(executionYear)) {
			continue;
		    } else {
			if (ingressionInformationBean.getRegistrationAgreement() != null) {

			    if (!studentCurricularPlan.getRegistration().getStudentCurricularPlan(executionYear)
				    .getRegistration().getRegistrationAgreement().equals(
					    ingressionInformationBean.getRegistrationAgreement())) {
				continue;
			    }
			}

			if (ingressionInformationBean.getRegistrationStateType() != null) {

			    if (!studentCurricularPlan.getRegistration().getStateInDate(
				    studentCurricularPlan.getRegistration().getStudentCurricularPlan(executionYear)
					    .getStartDateYearMonthDay().toDateTimeAtCurrentTime()).getStateType().equals(
				    ingressionInformationBean.getRegistrationStateType())) {
				continue;
			    }
			}

			registrations.add(studentCurricularPlan.getRegistration().getStudentCurricularPlan(executionYear));
		    }

		} else {

		    if (ingressionInformationBean.getRegistrationAgreement() != null) {

			if (!studentCurricularPlan.getRegistration().getStudentCurricularPlan(
				executionDegreeBean.getDegreeCurricularPlan()).getRegistration().getRegistrationAgreement()
				.equals(ingressionInformationBean.getRegistrationAgreement())) {
			    continue;
			}
		    }

		    if (ingressionInformationBean.getRegistrationStateType() != null) {
			if (!studentCurricularPlan.getRegistration().getStudentCurricularPlan(
				executionDegreeBean.getDegreeCurricularPlan()).getCurrentState().equals(
				StudentCurricularPlanState.PAST)
				|| !studentCurricularPlan.getRegistration().getStudentCurricularPlan(
					executionDegreeBean.getDegreeCurricularPlan()).getCurrentState().equals(
					StudentCurricularPlanState.INCOMPLETE)) {
			    if (!studentCurricularPlan.getRegistration().getStudentCurricularPlan(
				    executionDegreeBean.getDegreeCurricularPlan()).getRegistration().getActiveStateType().equals(
				    ingressionInformationBean.getRegistrationStateType())) {
				continue;
			    }
			} else if (ingressionInformationBean.getRegistrationStateType().equals(
				RegistrationStateType.INTERNAL_ABANDON)
				&& studentCurricularPlan.getRegistration().getStudentCurricularPlan(
					executionDegreeBean.getDegreeCurricularPlan()).getCurrentState().equals(
					StudentCurricularPlanState.INCOMPLETE)
				&& studentCurricularPlan.getRegistration().getStudentCurricularPlan(
					executionDegreeBean.getDegreeCurricularPlan()).getRegistration().getActiveStateType()
					.equals(RegistrationStateType.REGISTERED)) {
			    registrations.add(studentCurricularPlan);
			    continue;
			}
		    }
		    registrations.add(studentCurricularPlan);

		}
	    }
	} else {

	    for (final StudentCurricularPlan sPlan : executionDegreeBean.getDegree().getLastStudentCurricularPlans()) {

		if (executionDegreeBean.getExecutionYear() != null) {
		    final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionDegreeBean
			    .getExecutionYear().getYear());
		    if (!sPlan.getRegistration().hasAnyEnrolmentsIn(executionYear)) {
			continue;
		    } else {
			if (ingressionInformationBean.getRegistrationAgreement() != null) {

			    if (!sPlan.getRegistration().getStudentCurricularPlan(executionYear).getRegistration()
				    .getRegistrationAgreement().equals(ingressionInformationBean.getRegistrationAgreement())) {
				continue;
			    }
			}
			if (ingressionInformationBean.getRegistrationStateType() != null) {
			    if (!sPlan.getRegistration().getStudentCurricularPlan(executionYear).getRegistration()
				    .getActiveStateType().equals(ingressionInformationBean.getRegistrationStateType())) {
				continue;
			    }
			}
			registrations.add(sPlan.getRegistration().getStudentCurricularPlan(executionYear));

		    }
		} else {

		    if (ingressionInformationBean.getRegistrationAgreement() != null) {

			if (!sPlan.getRegistration().getLastStudentDegreeCurricularPlansByDegree(executionDegreeBean.getDegree())
				.getRegistration().getRegistrationAgreement().equals(
					ingressionInformationBean.getRegistrationAgreement())) {
			    continue;

			}
		    }
		    if (ingressionInformationBean.getRegistrationStateType() != null) {
			if (!sPlan.getRegistration().getLastStudentDegreeCurricularPlansByDegree(executionDegreeBean.getDegree())
				.getCurrentState().equals(StudentCurricularPlanState.PAST)
				&& !sPlan.getRegistration().getLastStudentDegreeCurricularPlansByDegree(
					executionDegreeBean.getDegree()).getCurrentState().equals(
					StudentCurricularPlanState.INCOMPLETE)) {
			    if (!sPlan.getRegistration().getLastStudentDegreeCurricularPlansByDegree(
				    executionDegreeBean.getDegree()).getRegistration().getActiveStateType().equals(
				    ingressionInformationBean.getRegistrationStateType())) {
				continue;
			    }
			} else if (!sPlan.getRegistration().getLastStudentDegreeCurricularPlansByDegree(
				executionDegreeBean.getDegree()).getRegistration().getActiveStateType().equals(
				RegistrationStateType.REGISTERED)) {

			    continue;

			}
		    }
		    registrations.add(sPlan);

		}

	    }

	}
	Collections.sort(registrations, new BeanComparator("registration.number"));
	Collections.reverse(registrations);
	return registrations;
    }

    public List<Enrolment> run(final ExecutionDegreeListBean executionDegreeBean, final Integer curricularCourseId,
	    final Integer semester) {
	if (curricularCourseId != null) {
	    return getStudentByCurricularCourse(executionDegreeBean, Integer.valueOf(curricularCourseId), semester);

	}
	return new ArrayList<Enrolment>();
    }

    private List<Enrolment> getStudentByCurricularCourse(final ExecutionDegreeListBean executionDegreeBean,
	    final Integer curricularCourseId, final Integer semester) {

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);
	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionDegreeBean.getExecutionYear()
		.getIdInternal());
	final ExecutionPeriod executionPeriod = executionYear.readExecutionPeriodForSemester(semester);

	final List<Enrolment> studentCurricularPlans = new ArrayList<Enrolment>();
	for (final Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod)) {

	    studentCurricularPlans.add(enrolment);
	}
	Collections.sort(studentCurricularPlans, new BeanComparator("studentCurricularPlan.registration.number"));
	return studentCurricularPlans;
    }

}
