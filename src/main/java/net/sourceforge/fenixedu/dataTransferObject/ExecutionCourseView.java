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
/*
 * @(#)ExecutionCourseView.java Created on Nov 5, 2004
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * 
 * @author Luis Cruz
 * @version 1.1, Nov 5, 2004
 * @since 1.1
 * 
 */
public class ExecutionCourseView {

    public static final Comparator<ExecutionCourseView> COMPARATOR_BY_NAME = new Comparator<ExecutionCourseView>() {

        @Override
        public int compare(ExecutionCourseView o1, ExecutionCourseView o2) {
            return o1.getExecutionCourseName().compareTo(o2.getExecutionCourseName());
        }

    };

    private final ExecutionCourse executionCourse;

    public ExecutionCourseView(final ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    private Integer curricularYear;

    private String anotation;

    private String degreeCurricularPlanAnotation;

    public String getAnotation() {
        return anotation;
    }

    public void setAnotation(String anotation) {
        this.anotation = anotation;
    }

    public Integer getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(Integer curricularYear) {
        this.curricularYear = curricularYear;
    }

    public String getDegreeCurricularPlanAnotation() {
        return degreeCurricularPlanAnotation;
    }

    public void setDegreeCurricularPlanAnotation(String degreeCurricularPlanAnotation) {
        this.degreeCurricularPlanAnotation = degreeCurricularPlanAnotation;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public String getExecutionCourseName() {
        return getExecutionCourse().getNome();
    }

    public String getExecutionCourseOID() {
        return getExecutionCourse().getExternalId();
    }

    public Integer getSemester() {
        return getExecutionCourse().getExecutionPeriod().getSemester();
    }

    public String getExecutionPeriodOID() {
        return getExecutionCourse().getExecutionPeriod().getExternalId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ExecutionCourseView) {
            final ExecutionCourseView executionCourseView = (ExecutionCourseView) obj;
            final Integer curricularYear = executionCourseView.getCurricularYear();
            return getExecutionCourse() == executionCourseView.getExecutionCourse() && getCurricularYear().equals(curricularYear);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getExecutionCourse().hashCode();
    }

}
