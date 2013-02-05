/*
 * Created on Dec 21, 2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadCoursesInformation extends FenixService {

    public List<InfoSiteCourseInformation> run(final ExecutionDegree executionDegree, final Boolean basic,
            final String executionYearString) {
        final ExecutionYear executionYear;
        if (executionYearString == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = ExecutionYear.readExecutionYearByName(executionYearString);
        }

        final List<Professorship> professorships;
        if (executionDegree == null) {
            final List<ExecutionDegree> executionDegrees =
                    ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear.getYear(), DegreeType.DEGREE,
                            DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_MASTER_DEGREE,
                            DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
            final List<DegreeCurricularPlan> degreeCurricularPlans = getDegreeCurricularPlans(executionDegrees);
            final ExecutionYear executionDegreesExecutionYear =
                    degreeCurricularPlans.isEmpty() ? null : executionDegrees.get(0).getExecutionYear();

            if (basic == null) {
                professorships =
                        Professorship.readByDegreeCurricularPlansAndExecutionYear(degreeCurricularPlans,
                                executionDegreesExecutionYear);
            } else {
                professorships =
                        Professorship.readByDegreeCurricularPlansAndExecutionYearAndBasic(degreeCurricularPlans,
                                executionDegreesExecutionYear, basic);
            }
        } else {
            if (basic == null) {
                professorships =
                        Professorship.readByDegreeCurricularPlanAndExecutionYear(executionDegree.getDegreeCurricularPlan(),
                                executionDegree.getExecutionYear());
            } else {
                professorships =
                        Professorship.readByDegreeCurricularPlanAndExecutionYearAndBasic(
                                executionDegree.getDegreeCurricularPlan(), executionDegree.getExecutionYear(), basic);
            }
        }

        final Set<ExecutionCourse> executionCourses = new HashSet<ExecutionCourse>();
        for (final Professorship professorship : professorships) {
            executionCourses.add(professorship.getExecutionCourse());
        }

        final List<InfoSiteCourseInformation> result = new ArrayList<InfoSiteCourseInformation>();
        for (final ExecutionCourse executionCourse : executionCourses) {
            result.add(new InfoSiteCourseInformation(executionCourse, executionYear));
        }

        return result;
    }

    private List<DegreeCurricularPlan> getDegreeCurricularPlans(final List<ExecutionDegree> executionDegrees) {
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();

        for (final ExecutionDegree executionDegree : executionDegrees) {
            result.add(executionDegree.getDegreeCurricularPlan());
        }

        return result;
    }

}
