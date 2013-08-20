/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.Pair;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod {

    @Service
    public static HashMap<String, Pair<Integer, String>> run(String[] degreeCurricularPlansIDs, String executionPeriodID) {
        final ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodID);
        if (executionSemester == null) {
            throw new DomainException("error.selection.noPeriod");
        }
        final Set<String> existentsExecutionCoursesSiglas = readExistingExecutionCoursesSiglas(executionSemester);

        if (degreeCurricularPlansIDs.length == 0) {
            throw new DomainException("error.selection.noDegree");
        }

        HashMap<String, Pair<Integer, String>> numberExecutionCoursesPerDCP = new HashMap<String, Pair<Integer, String>>();
        for (final String degreeCurricularPlanID : degreeCurricularPlansIDs) {
            int numberExecutionCourses = 0;
            StringBuilder curricularCodes = new StringBuilder();
            final DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);
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
                        final ExecutionCourse executionCourse =
                                new ExecutionCourse(curricularCourse.getName(), sigla, executionSemester, null);
                        executionCourse.createSite();
                        curricularCourse.addAssociatedExecutionCourses(executionCourse);
                        numberExecutionCourses++;
                        curricularCodes.append(curricularCourse.getAcronym() + ", ");
                    }
                }
            }
            if (curricularCodes.length() > 0) {
                curricularCodes.setLength(curricularCodes.length() - 2); // trim last ", "
            }
            numberExecutionCoursesPerDCP.put(degreeCurricularPlanID,
                    new Pair<Integer, String>(Integer.valueOf(numberExecutionCourses), curricularCodes.toString()));
        }

        return numberExecutionCoursesPerDCP;
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
            while (existentsExecutionCoursesSiglas.contains((sigla + "-" + ++suffix).toUpperCase())) {
                ;
            }
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