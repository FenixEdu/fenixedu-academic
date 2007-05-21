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
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class SearchStudentByCriteria extends Service {

	public List<StudentCurricularPlan> run(
			final ExecutionDegreeListBean executionDegreeBean,
			final ListInformationBean ingressionInformationBean) {

		return getStudentByDegree(executionDegreeBean,
				ingressionInformationBean);

	}

	private List<StudentCurricularPlan> getStudentByDegree(
			ExecutionDegreeListBean executionDegreeBean,
			ListInformationBean ingressionInformationBean) {
		final List<StudentCurricularPlan> registrations = new ArrayList<StudentCurricularPlan>();
	
		if (executionDegreeBean.getDegreeCurricularPlan() != null) {
			for (final StudentCurricularPlan studentCurricularPlan : executionDegreeBean
					.getDegreeCurricularPlan().getStudentCurricularPlans()) {

				if (ingressionInformationBean.getRegistrationAgreement() == null
						&& ingressionInformationBean.getRegistrationStateType() == null
						&& executionDegreeBean.getExecutionYear() == null) {
					registrations.add(studentCurricularPlan);
					continue;
				}

				if (executionDegreeBean.getExecutionYear() != null) {
					final ExecutionYear executionYear = ExecutionYear
							.readExecutionYearByName(executionDegreeBean
									.getExecutionYear().getYear());
					if (!studentCurricularPlan.getRegistration()
							.hasAnyEnrolmentsIn(executionYear)) {
						continue;
					}
				}
				if (ingressionInformationBean.getRegistrationAgreement() != null) {
					
					if (!studentCurricularPlan.getRegistration()
							.getRegistrationAgreement().equals(
									ingressionInformationBean
											.getRegistrationAgreement())) {
						continue;
					}
				}

				if (ingressionInformationBean.getRegistrationStateType() != null) {

					if (!studentCurricularPlan.getRegistration()
							.getActiveStateType().equals(
									ingressionInformationBean
											.getRegistrationStateType())) {
						continue;
					}
				}

				registrations.add(studentCurricularPlan);

			}
		} else {
			for (final DegreeCurricularPlan dcp : executionDegreeBean
					.getDegree().getDegreeCurricularPlans()) {

				for (final StudentCurricularPlan sPlan : dcp
						.getStudentCurricularPlans()) {
					if (!registrations.contains(sPlan)) {
						if (ingressionInformationBean.getRegistrationAgreement() != null) {
							
							if (!sPlan.getRegistration()
									.getRegistrationAgreement().equals(
											ingressionInformationBean
											.getRegistrationAgreement())) {
								continue;
							}
						}
						
						if (ingressionInformationBean.getRegistrationStateType() != null) {
							
							if (!sPlan.getRegistration()
									.getActiveStateType().equals(
											ingressionInformationBean
											.getRegistrationStateType())) {
								continue;
							}
						}
						registrations.add(sPlan);
					}

				}

			}

		}
		Collections.sort(registrations, new BeanComparator(
				"registration.number"));
		Collections.reverse(registrations);
		return registrations;
	}

	public List<Enrolment> run(
			final ExecutionDegreeListBean executionDegreeBean,
			final Integer curricularCourseId, final Integer semester) {
		if (curricularCourseId != null) {
			return getStudentByCurricularCourse(executionDegreeBean, Integer
					.valueOf(curricularCourseId), semester);

		}
		return new ArrayList<Enrolment>();
	}

	private List<Enrolment> getStudentByCurricularCourse(
			final ExecutionDegreeListBean executionDegreeBean,
			final Integer curricularCourseId, final Integer semester) {

		final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
				.readDegreeModuleByOID(curricularCourseId);
		final ExecutionYear executionYear = rootDomainObject
				.readExecutionYearByOID(executionDegreeBean.getExecutionYear()
						.getIdInternal());
		final ExecutionPeriod executionPeriod = executionYear
				.readExecutionPeriodForSemester(semester);

		final List<Enrolment> studentCurricularPlans = new ArrayList<Enrolment>();
		for (final Enrolment enrolment : curricularCourse
				.getEnrolmentsByExecutionPeriod(executionPeriod)) {

			studentCurricularPlans.add(enrolment);
		}
		Collections.sort(studentCurricularPlans, new BeanComparator(
				"studentCurricularPlan.registration.number"));
		return studentCurricularPlans;
	}

}
