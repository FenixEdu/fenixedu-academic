/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(Integer[] degreeCurricularPlansIDs, Integer executionPeriodID) {
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
	final Set<String> existentsExecutionCoursesSiglas = readExistingExecutionCoursesSiglas(executionSemester);
	for (final Integer degreeCurricularPlanID : degreeCurricularPlansIDs) {
	    final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject
		    .readDegreeCurricularPlanByOID(degreeCurricularPlanID);
	    final List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();
	    for (final CurricularCourse curricularCourse : curricularCourses) {

		if (curricularCourse.isOptionalCurricularCourse()
			|| curricularCourse.getActiveDegreeModuleScopesInExecutionPeriod(executionSemester).isEmpty()) {
		    continue;
		}
		if (curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester).isEmpty()) {
		    final String originalCode = getCodeForCurricularCourse(curricularCourse);
		    if (originalCode != null) {
			final String sigla = getUniqueSigla(existentsExecutionCoursesSiglas, originalCode);
			final ExecutionCourse executionCourse = new ExecutionCourse(curricularCourse.getName(), sigla,
				executionSemester, null);
			executionCourse.createSite();
			curricularCourse.addAssociatedExecutionCourses(executionCourse);
		    }
		}
	    }
	}
    }

    private static String getCodeForCurricularCourse(final CurricularCourse curricularCourse) {
	if (curricularCourse.getAcronym() != null) {
	    return curricularCourse.getAcronym();
	}
	final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
	if (competenceCourse != null) {
	    return competenceCourse.getCode();
	}
	if (curricularCourse.getCode() != null) {
	    return curricularCourse.getCode();
	}
	return null;
    }

    public static String getUniqueSigla(Set<String> existentsExecutionCoursesSiglas, String sigla) {
	if (existentsExecutionCoursesSiglas.contains(sigla.toUpperCase())) {
	    int suffix = 1;
	    while (existentsExecutionCoursesSiglas.contains((sigla + "-" + ++suffix).toUpperCase()))
		;
	    sigla = sigla + "-" + suffix;
	}
	existentsExecutionCoursesSiglas.add(sigla.toUpperCase());

	return sigla;
    }

    private static Set<String> readExistingExecutionCoursesSiglas(final ExecutionSemester executionSemester) {
	final Set<String> existingExecutionCoursesSiglas = new HashSet<String>();
	for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
	    existingExecutionCoursesSiglas.add(executionCourse.getSigla().toUpperCase());
	}
	return existingExecutionCoursesSiglas;
    }

}