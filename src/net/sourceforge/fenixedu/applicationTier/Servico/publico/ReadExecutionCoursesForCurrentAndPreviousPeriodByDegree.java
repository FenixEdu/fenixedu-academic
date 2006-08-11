package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree extends Service {

    public List<ExecutionCourseView> run(Integer degreeOID) {
        List<ExecutionCourseView> result = new ArrayList<ExecutionCourseView>();

        ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        ExecutionPeriod previousExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

        Degree degree = rootDomainObject.readDegreeByOID(degreeOID);

        Set<String> processedExecutionCourses = new HashSet<String>();
        for (DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
            for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
                for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                    ExecutionPeriod executionPeriodFromExecutionCourse = executionCourse.getExecutionPeriod();

                    if (executionPeriodFromExecutionCourse.equals(currentExecutionPeriod)
                            || (previousExecutionPeriod != null && executionPeriodFromExecutionCourse.equals(previousExecutionPeriod))) {
                        
                        for (CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                            String key = generateExecutionCourseKey(executionCourse,curricularCourseScope);

                            if (!processedExecutionCourses.contains(key)) {
                                ExecutionCourseView executionCourseView = new ExecutionCourseView(executionCourse);
                                executionCourseView.setCurricularYear(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
                                executionCourseView.setAnotation(curricularCourseScope.getAnotation());
                                executionCourseView.setDegreeCurricularPlanAnotation(degreeCurricularPlan.getAnotation());
                                
                                processedExecutionCourses.add(key);
                                result.add(executionCourseView);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    private String generateExecutionCourseKey(ExecutionCourse executionCourse, CurricularCourseScope curricularCourseScope) {
        StringBuilder key = new StringBuilder();

        key.append(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
        key.append('-');
        key.append(executionCourse.getIdInternal());

        return key.toString();
    }

}
