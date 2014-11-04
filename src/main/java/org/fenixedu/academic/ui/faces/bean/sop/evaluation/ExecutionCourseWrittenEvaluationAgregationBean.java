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
package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;

public class ExecutionCourseWrittenEvaluationAgregationBean {

    public static final Comparator<ExecutionCourseWrittenEvaluationAgregationBean> COMPARATOR_BY_EXECUTION_COURSE_CODE_AND_CURRICULAR_YEAR =
            new Comparator<ExecutionCourseWrittenEvaluationAgregationBean>() {

                @Override
                public int compare(ExecutionCourseWrittenEvaluationAgregationBean o1,
                        ExecutionCourseWrittenEvaluationAgregationBean o2) {
                    final int c = o1.getExecutionCourse().getSigla().compareTo(o2.getExecutionCourse().getSigla());
                    return c == 0 ? o1.getCurricularYear().compareTo(o2.getCurricularYear()) : c;
                }

            };

    private final ExecutionCourse executionCourse;
    private final Integer curricularYear;
    private final Collection<WrittenEvaluation> writtenEvaluations;

    public ExecutionCourseWrittenEvaluationAgregationBean(Integer curricularYear, ExecutionCourse executionCourse,
            Set<WrittenEvaluation> writtenEvaluations) {
        this.curricularYear = curricularYear;
        this.executionCourse = executionCourse;
        this.writtenEvaluations = writtenEvaluations;
    }

    public Integer getCurricularYear() {
        return curricularYear;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public Collection<WrittenEvaluation> getWrittenEvaluations() {
        return writtenEvaluations;
    }

}
