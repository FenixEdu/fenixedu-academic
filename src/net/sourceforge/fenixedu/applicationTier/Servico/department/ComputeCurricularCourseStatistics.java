/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ComputeCurricularCourseStatistics extends Service {

    public String run(Integer degreeCurricularPlanID, Integer executionYearID,
	    RegistrationAgreement agreement) {

	ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
	DegreeCurricularPlan degreeCurricularPlan = rootDomainObject
		.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
	List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();

	Formatter result = new Formatter();

	for (CurricularCourse curricularCourse : curricularCourses) {

	    List<ExecutionPeriod> executionPeriods = executionYear.getExecutionPeriods();
	    for (ExecutionPeriod executionPeriod : executionPeriods) {

		// Get Scopes
		// List<CurricularCourseScope> scopes = curricularCourse
		// .getActiveScopesInExecutionPeriod(executionPeriod);

		List<DegreeModuleScope> scopes = curricularCourse
			.getActiveDegreeModuleScopesInExecutionPeriod(executionPeriod);

		if (scopes.isEmpty()) {
		    continue;
		}

		// Get max Year and its Semester
		int year = 0;
		int semester = 0;
		// for (CurricularCourseScope scope : scopes) {
		// Integer scopeYear =
		// scope.getCurricularSemester().getCurricularYear().getYear();
		// if (scopeYear > year) {
		// year = scopeYear;
		// semester = scope.getCurricularSemester().getSemester();
		// }
		// }

		for (DegreeModuleScope scope : scopes) {
		    Integer scopeYear = scope.getCurricularYear();
		    if (scopeYear > year) {
			year = scopeYear;
			semester = scope.getCurricularSemester();
		    }
		}

		// Get Execution Course
		List<ExecutionCourse> executionCourses = curricularCourse
			.getExecutionCoursesByExecutionPeriod(executionPeriod);
		// if (executionCourses.isEmpty() || executionCourses.size() >
		// 1) {
		// // Houston, we have a problem...!!
		// continue;
		// }
		// ExecutionCourse executionCourse = executionCourses.get(0);

		if (executionCourses.size() == 1) {
		    // Organize enrolments by DegreeCurricularPlans
		    Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap = organizeEnrolmentsByDCP(
			    curricularCourse, executionPeriod, agreement, null);

		    calculateEnrolmentsForDCP(result, curricularCourse, executionPeriod, year, semester,
			    executionCourses.iterator().next(), enrolmentsMap);
		} else {
		    for (ExecutionCourse executionCourse : executionCourses) {
			// Organize enrolments by DegreeCurricularPlans
			Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap = organizeEnrolmentsByDCP(
				curricularCourse, executionPeriod, agreement, executionCourse);

			calculateEnrolmentsForDCP(result, curricularCourse, executionPeriod, year,
				semester, executionCourse, enrolmentsMap);
		    }
		}

	    }
	}

	return result.toString();
    }

    private void calculateEnrolmentsForDCP(Formatter result, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, int year, int semester, ExecutionCourse executionCourse,
	    Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap) {
	// Calculate enrolments for each DegreeCurricularPlan
	for (DegreeCurricularPlan enrolmentDCP : enrolmentsMap.keySet()) {

	    int firstEnrolledCount = 0;
	    int secondEnrolledCount = 0;

	    Collection<Enrolment> dcpEnrolments = enrolmentsMap.get(enrolmentDCP);
	    for (Enrolment enrolment : dcpEnrolments) {
		switch (enrolment.getNumberOfTotalEnrolmentsInThisCourse(executionPeriod)) {

		case 1:
		    firstEnrolledCount++;
		    break;
		case 2:
		    secondEnrolledCount++;
		    break;
		default:
		    break;
		}
	    }

	    // Add to result
	    result.format("%s\t%s\t%d\t%s\t%d\t%s\t%d\t%d\t%d\t%d\t%d\n", curricularCourse.getCode(),
		    curricularCourse.getName(), executionCourse.getIdInternal(), executionCourse
			    .getSigla(), enrolmentDCP.getIdInternal(), enrolmentDCP.getName(), semester,
		    year, firstEnrolledCount, secondEnrolledCount, dcpEnrolments.size());
	}
    }

    private Map<DegreeCurricularPlan, Collection<Enrolment>> organizeEnrolmentsByDCP(
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    RegistrationAgreement agreement, ExecutionCourse executionCourse) {
	Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap = new HashMap<DegreeCurricularPlan, Collection<Enrolment>>();

	List<Enrolment> enrolments = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);
	for (Enrolment enrolment : enrolments) {

	    if (executionCourse != null
		    && enrolment.getAttendsByExecutionCourse(executionCourse) == null) {
		continue;
	    }

	    final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();

	    if (agreement != null
		    && studentCurricularPlan.getRegistration().getRegistrationAgreement() != agreement) {
		continue;
	    }

	    DegreeCurricularPlan studentDCP = studentCurricularPlan.getDegreeCurricularPlan();
	    Collection<Enrolment> dcpEnrolments = enrolmentsMap.get(studentDCP);
	    if (dcpEnrolments == null) {
		dcpEnrolments = new ArrayList<Enrolment>();
		enrolmentsMap.put(studentDCP, dcpEnrolments);
	    }
	    dcpEnrolments.add(enrolment);

	}
	return enrolmentsMap;
    }

}
