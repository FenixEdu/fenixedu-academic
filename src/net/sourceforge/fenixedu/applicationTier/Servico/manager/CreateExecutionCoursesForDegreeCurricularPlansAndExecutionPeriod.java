/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod extends Service {

    public void run(Integer[] degreeCurricularPlansIDs, Integer executionPeriodID)
            throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        final Set<String> existentsExecutionCoursesSiglas = readExistingExecutionCoursesSiglas(executionPeriod);
        for (final Integer degreeCurricularPlanID : degreeCurricularPlansIDs) {
            final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
            final List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();
            for (final CurricularCourse curricularCourse : curricularCourses) {
                if (curricularCourse.getActiveDegreeModuleScopesInExecutionPeriod(executionPeriod).isEmpty()) {
                    continue;
                }
                if (curricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod).isEmpty()) {
                    final String originalCode = getCodeForCurricularCourse(curricularCourse);
                    if (originalCode != null) {
                        final String sigla = getUniqueSigla(existentsExecutionCoursesSiglas, originalCode);
                        final ExecutionCourse executionCourse = new ExecutionCourse(curricularCourse.getName(), sigla, executionPeriod);
                        executionCourse.createSite();
                        curricularCourse.addAssociatedExecutionCourses(executionCourse);
                    }
		}
            }
        }
    }

    private String getCodeForCurricularCourse(final CurricularCourse curricularCourse) {
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

    private String getUniqueSigla(Set<String> existentsExecutionCoursesSiglas, String sigla) {
        if (existentsExecutionCoursesSiglas.contains(sigla.toUpperCase())) {
            int suffix = 1;
            while (existentsExecutionCoursesSiglas.contains((sigla + "-" + ++suffix).toUpperCase()));
            sigla = sigla + "-" + suffix;
        }
        existentsExecutionCoursesSiglas.add(sigla.toUpperCase());

        return sigla;
    }

    private Set<String> readExistingExecutionCoursesSiglas(final ExecutionPeriod executionPeriod) {
        final Set<String> existingExecutionCoursesSiglas = new HashSet<String>();
        for (ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCoursesSet()) {
            existingExecutionCoursesSiglas.add(executionCourse.getSigla().toUpperCase());
        }
        return existingExecutionCoursesSiglas;
    }

}
