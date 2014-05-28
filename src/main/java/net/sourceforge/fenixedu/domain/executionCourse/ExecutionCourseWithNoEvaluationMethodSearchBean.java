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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class ExecutionCourseWithNoEvaluationMethodSearchBean implements Serializable {

    private ExecutionSemester executionSemester;

    private List<DegreeType> degreeTypes;

    private int withEvaluationMethod = 0;

    private int withoutEvaluationMethod = 0;

    private int total = 0;

    public ExecutionCourseWithNoEvaluationMethodSearchBean() {
        super();
    }

    public Set getSearchResult() {
        withEvaluationMethod = 0;
        withoutEvaluationMethod = 0;
        total = 0;

        final ExecutionSemester executionSemester = getExecutionPeriod();
        final List<DegreeType> degreeTypes = getDegreeTypes();
        if (executionSemester == null || degreeTypes == null) {
            return null;
        }
        final Set<ExecutionCourse> executionCourses =
                new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
            if (isLecturedIn(executionCourse, degreeTypes)) {
                total++;
                if (hasEvaluationMethod(executionCourse)) {
                    withEvaluationMethod++;
                } else {
                    withoutEvaluationMethod++;
                    executionCourses.add(executionCourse);
                }
            }
        }
        return executionCourses;
    }

    private boolean hasEvaluationMethod(final ExecutionCourse executionCourse) {
        final String evaluationMethodText = executionCourse.getEvaluationMethodText();
        final String evaluationMethodTextEn = executionCourse.getEvaluationMethodTextEn();
        return (evaluationMethodText != null && evaluationMethodText.length() > 0)
                || (evaluationMethodTextEn != null && evaluationMethodTextEn.length() > 0);
    }

    private boolean isLecturedIn(final ExecutionCourse executionCourse, final List<DegreeType> degreeTypes) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final Degree degree = curricularCourse.getDegree();
            final DegreeType degreeType = degree.getDegreeType();
            if (degreeTypes.contains(degreeType)) {
                return true;
            }
        }
        return false;
    }

    public List<DegreeType> getDegreeTypes() {
        return degreeTypes;
    }

    public void setDegreeTypes(List<DegreeType> degreeTypes) {
        this.degreeTypes = degreeTypes;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public int getTotal() {
        return total;
    }

    public int getWithEvaluationMethod() {
        return withEvaluationMethod;
    }

    public int getWithoutEvaluationMethod() {
        return withoutEvaluationMethod;
    }

}
