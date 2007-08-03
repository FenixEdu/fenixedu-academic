/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.text.StyledEditorKit.BoldAction;

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
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers.ISectionMenuSlotContentRenderer;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class SearchStudentByCriteria extends Service {

//    public List<StudentCurricularPlan> run(final ExecutionDegreeListBean executionDegreeBean,
//	    final ListInformationBean ingressionInformationBean) {
//
//	return getStudentByDegree(executionDegreeBean, ingressionInformationBean);
//
//    }
//
//    private List<StudentCurricularPlan> getStudentByDegree(ExecutionDegreeListBean executionDegreeBean,
//	    ListInformationBean ingressionInformationBean) {
//
//	final List<StudentCurricularPlan> registrations = new ArrayList<StudentCurricularPlan>();
//
//		final List<RegistrationAgreement> registrationAgreement = ingressionInformationBean
//				.getRegistrationAgreement();
//		final List<RegistrationStateType> registrationStateType = ingressionInformationBean
//				.getRegistrationStateType();
//		final Boolean equivalence = ingressionInformationBean
//				.getWith_equivalence();
//		if (executionDegreeBean.getDegreeCurricularPlan() != null) {
//
//			 if
//			 (ingressionInformationBean.getRegistrationAgreement().isEmpty()
//			 && ingressionInformationBean.getRegistrationStateType()
//			 .isEmpty()
//			 && executionDegreeBean.getExecutionYear() == null
//			 && ingressionInformationBean.getWith_equivalence().equals(
//			 false)) {
//			
//			 registrations.addAll(executionDegreeBean
//			 .getDegreeCurricularPlan().getStudentCurricularPlans());
//			
//			
//			 return registrations;
//			 }
//			for (final StudentCurricularPlan studentCurricularPlan : executionDegreeBean
//					.getDegreeCurricularPlan().getStudentCurricularPlans()) {
//				if (studentCurricularPlan.getRegistration() != null) {
//
//					if (executionDegreeBean.getExecutionYear() != null) {
//						final ExecutionYear executionYear = ExecutionYear
//								.readExecutionYearByName(executionDegreeBean
//										.getExecutionYear().getYear());
//						if (!studentCurricularPlan.getRegistration()
//								.hasAnyEnrolmentsIn(executionYear)) {
//							continue;
//						} else {
//							if (!registrationAgreement.isEmpty()) {
//
//								if (!registrationAgreement
//										.contains(studentCurricularPlan
//												.getRegistration()
//												.getStudentCurricularPlan(
//														executionYear)
//												.getRegistration()
//												.getRegistrationAgreement())) {
//									continue;
//								}
//							}
//
//							if (!registrationStateType.isEmpty()) {
//
//								if (!registrationStateType
//										.contains(studentCurricularPlan
//												.getRegistration()
//												.getStateInDate(
//														studentCurricularPlan
//																.getRegistration()
//																.getStudentCurricularPlan(
//																		executionYear)
//																.getStartDateYearMonthDay()
//																.toDateTimeAtCurrentTime())
//												.getStateType())) {
//									continue;
//								}
//							}
//							if (!equivalence.equals(false)) {
//
//								if (!studentCurricularPlan
//										.getHasAnyEquivalences()) {
//									continue;
//								}
//							}
//
//							registrations.add(studentCurricularPlan
//									.getRegistration()
//									.getStudentCurricularPlan(executionYear));
//						}
//
//					} else {
//
//						if (!registrationAgreement.isEmpty()) {
//
//							if (!registrationAgreement
//									.contains(studentCurricularPlan
//											.getRegistration()
//											.getStudentCurricularPlan(
//													executionDegreeBean
//															.getDegreeCurricularPlan())
//											.getRegistration()
//											.getRegistrationAgreement())) {
//								continue;
//							}
//						}
//
//						if (!registrationStateType.isEmpty()) {
//
//							if (!registrationStateType
//									.contains(studentCurricularPlan
//											.getRegistration()
//											.getStudentCurricularPlan(
//													executionDegreeBean
//															.getDegreeCurricularPlan())
//											.getRegistration()
//											.getActiveStateType())) {
//								continue;
//							}
//						}
//
//						if (!equivalence.equals(false)) {
//
//							if (!studentCurricularPlan.getHasAnyEquivalences()) {
//								continue;
//							}
//						}
//						registrations.add(studentCurricularPlan);
//
//					}
//				}
//			}
//		} else {
//			
//
////			 if(ingressionInformationBean.getRegistrationAgreement().isEmpty()
////			 && ingressionInformationBean.getRegistrationStateType()
////			 .isEmpty()
////			 && executionDegreeBean.getExecutionYear() == null
////			 && ingressionInformationBean.getWith_equivalence().equals(
////			 false)) {
////
////			 registrations.addAll(executionDegreeBean
////			 .getDegree().getLastStudentCurricularPlans());
////			
////			
////			 return registrations;
////			 }
//			 
//			 System.out.println("total alunos "
//					 + executionDegreeBean.getDegree()
//					 .getLastStudentCurricularPlans().size());
//			 
//			for (final StudentCurricularPlan sPlan : executionDegreeBean
//					.getDegree().getLastStudentCurricularPlans()) {
//				if (sPlan.getRegistration() != null) {
//					if (executionDegreeBean.getExecutionYear() != null) {
//						final ExecutionYear executionYear = ExecutionYear
//								.readExecutionYearByName(executionDegreeBean
//										.getExecutionYear().getYear());
//						if (!sPlan.getRegistration().hasAnyEnrolmentsIn(
//								executionYear)) {
//							continue;
//						} else {
//							if (!registrationAgreement.isEmpty()) {
//
//								if (!registrationAgreement
//										.contains(sPlan.getRegistration()
//												.getStudentCurricularPlan(
//														executionYear)
//												.getRegistration()
//												.getRegistrationAgreement())) {
//									continue;
//								}
//							}
//							if (!registrationStateType.isEmpty()) {
//
//								if (!registrationStateType
//										.contains(sPlan.getRegistration()
//												.getStudentCurricularPlan(
//														executionYear)
//												.getRegistration()
//												.getActiveStateType())) {
//									continue;
//								}
//							}
//
//							if (!equivalence.equals(false)) {
//
//								if (!sPlan.getHasAnyEquivalences()) {
//									continue;
//								}
//							}
//							registrations.add(sPlan.getRegistration()
//									.getStudentCurricularPlan(executionYear));
//
//						}
//					} else {
//
//						if (!registrationAgreement.isEmpty()) {
//
//							if (!registrationAgreement
//									.contains(sPlan
//											.getRegistration()
//											.getLastStudentDegreeCurricularPlansByDegree(
//													executionDegreeBean
//															.getDegree())
//											.getRegistration()
//											.getRegistrationAgreement())) {
//								continue;
//
//							}
//						}
//						if (!registrationStateType.isEmpty()) {
//
//							if (!registrationStateType
//									.contains(sPlan
//											.getRegistration()
//											.getLastStudentDegreeCurricularPlansByDegree(
//													executionDegreeBean
//															.getDegree())
//											.getRegistration()
//											.getActiveStateType())) {
//								continue;
//							}
//
//						}
//						if (!equivalence.equals(false)) {
//						
//							if (!sPlan.getHasAnyEquivalences()) {
//								continue;
//							}
//						}
//						registrations.add(sPlan);
//
//					}
//
//				}
//			
//				registrations.add(sPlan);
//			}
//		    }
//
//		
//		Collections.sort(registrations, new BeanComparator(
//		"registration.number"));
//		Collections.reverse(registrations);
//		
//		return registrations;
//	}
//

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
