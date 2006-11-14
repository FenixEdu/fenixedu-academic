package net.sourceforge.fenixedu.domain.executionCourse;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class ExecutionCourseSearchBean implements Serializable {

    private DomainReference<ExecutionPeriod> executionPeriodDomainReference;
    private DomainReference<ExecutionDegree> executionDegreeDomainReference;

    public ExecutionCourseSearchBean() {
        super();
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegreeDomainReference == null ?
                null : executionDegreeDomainReference.getObject();
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegreeDomainReference = executionDegree == null ?
                null : new DomainReference<ExecutionDegree>(executionDegree);
    }

    public ExecutionPeriod getExecutionPeriod() {
        return executionPeriodDomainReference == null ?
                null : executionPeriodDomainReference.getObject();
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
        this.executionPeriodDomainReference = executionPeriod == null ?
                null : new DomainReference<ExecutionPeriod>(executionPeriod);
    }

    public Collection<ExecutionCourse> search(final Collection<ExecutionCourse> result) {
        final ExecutionPeriod executionPeriod = getExecutionPeriod();
        final ExecutionDegree executionDegree = getExecutionDegree();
        if (executionPeriod == null || executionDegree == null) {
            return null;
        }
        for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCoursesSet()) {
            if (matchesCriteria(executionPeriod, executionDegree, executionCourse)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public Collection<ExecutionCourse> search() {
        final Set<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        return search(executionCourses);
        
    }

    private boolean matchesCriteria(final ExecutionPeriod executionPeriod, final ExecutionDegree executionDegree,
            final ExecutionCourse executionCourse) {
        return matchExecutionPeriod(executionPeriod, executionCourse) && matchExecutionDegree(executionDegree, executionCourse);
    }

    private boolean matchExecutionPeriod(final ExecutionPeriod executionPeriod, final ExecutionCourse executionCourse) {
        return executionPeriod == executionCourse.getExecutionPeriod();
    }

    private boolean matchExecutionDegree(final ExecutionDegree executionDegree, final ExecutionCourse executionCourse) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            if (degreeCurricularPlan == executionDegree.getDegreeCurricularPlan()) {
                return true;
            }
        }
        return false;
    }

}
