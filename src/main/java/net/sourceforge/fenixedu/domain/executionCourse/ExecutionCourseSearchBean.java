/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.executionCourse;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class ExecutionCourseSearchBean implements Serializable {

    private ExecutionSemester executionPeriodDomainReference;
    private ExecutionDegree executionDegreeDomainReference;

    public ExecutionCourseSearchBean() {
        super();
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegreeDomainReference;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegreeDomainReference = executionDegree;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionPeriodDomainReference;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionPeriodDomainReference = executionSemester;
    }

    public Collection<ExecutionCourse> search(final Collection<ExecutionCourse> result) {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final ExecutionDegree executionDegree = getExecutionDegree();
        if (executionSemester == null || executionDegree == null) {
            return null;
        }
        for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
            if (matchesCriteria(executionSemester, executionDegree, executionCourse)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public Collection<ExecutionCourse> search() {
        final Set<ExecutionCourse> executionCourses =
                new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        return search(executionCourses);

    }

    protected boolean matchesCriteria(final ExecutionSemester executionSemester, final ExecutionDegree executionDegree,
            final ExecutionCourse executionCourse) {
        return matchExecutionPeriod(executionSemester, executionCourse) && matchExecutionDegree(executionDegree, executionCourse);
    }

    private boolean matchExecutionPeriod(final ExecutionSemester executionSemester, final ExecutionCourse executionCourse) {
        return executionSemester == executionCourse.getExecutionPeriod();
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
