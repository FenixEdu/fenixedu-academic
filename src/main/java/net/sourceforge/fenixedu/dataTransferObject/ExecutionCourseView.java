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
