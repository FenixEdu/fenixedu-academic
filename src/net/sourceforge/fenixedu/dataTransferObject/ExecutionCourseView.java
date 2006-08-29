/*
 * @(#)ExecutionCourseView.java Created on Nov 5, 2004
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * 
 * @author Luis Cruz
 * @version 1.1, Nov 5, 2004
 * @since 1.1
 * 
 */
public class ExecutionCourseView {

    private final DomainReference<ExecutionCourse> executionCourseDomainReference; 

    public ExecutionCourseView(final ExecutionCourse executionCourse) {
        executionCourseDomainReference = new DomainReference<ExecutionCourse>(executionCourse);
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
        return executionCourseDomainReference == null ? null : executionCourseDomainReference.getObject();
    }

    public String getExecutionCourseName() {
        return getExecutionCourse().getNome();
    }
    public Integer getExecutionCourseOID() {
        return getExecutionCourse().getIdInternal();
    }
    public Integer getSemester() {
        return getExecutionCourse().getExecutionPeriod().getSemester();
    }
    public Integer getExecutionPeriodOID() {
        return getExecutionCourse().getExecutionPeriod().getIdInternal();
    }

}
