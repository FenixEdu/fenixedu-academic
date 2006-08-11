/*
 * @(#)ExecutionCourseView.java Created on Nov 5, 2004
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * 
 * @author Luis Cruz
 * @version 1.1, Nov 5, 2004
 * @since 1.1
 * 
 */
public class ExecutionCourseView {

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

    public String getExecutionCourseName() {
        return executionCourse.getNome();
    }
    public Integer getExecutionCourseOID() {
        return executionCourse.getIdInternal();
    }
    public Integer getSemester() {
        return executionCourse.getExecutionPeriod().getSemester();
    }
    public Integer getExecutionPeriodOID() {
        return executionCourse.getExecutionPeriod().getIdInternal();
    }

}
