package net.sourceforge.fenixedu.dataTransferObject.academicAdministration;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class SearchStudentsByCurricularCourseParametersBean implements Serializable {

    private ExecutionYear executionYear;

    private DegreeCurricularPlan degreeCurricularPlan;

    private CurricularCourse curricularCourse;

    private Set<Degree> administratedDegrees;

    public SearchStudentsByCurricularCourseParametersBean() {
    }

    public SearchStudentsByCurricularCourseParametersBean(Set<Degree> administratedDegrees) {
        this.administratedDegrees = administratedDegrees;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public SortedSet<Degree> getAdministratedDegrees() {
        final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        result.addAll(administratedDegrees);
        return result;
    }

    public SortedSet<DegreeCurricularPlan> getAvailableDegreeCurricularPlans() {
        final SortedSet<DegreeCurricularPlan> result =
                new TreeSet<DegreeCurricularPlan>(
                        DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);
        if (executionYear != null) {
            final Set<Degree> degrees = administratedDegrees;
            for (DegreeCurricularPlan plan : executionYear.getDegreeCurricularPlans()) {
                if (degrees.contains(plan.getDegree())) {
                    result.add(plan);
                }
            }
        }
        return result;
    }
}
