/*
 * @(#)ExecutionCourseView.java Created on Nov 7, 2004
 * 
 */
package DataBeans;

import java.io.Serializable;

/**
 *
 * @author Luis Cruz
 *
 */
public class ClassView implements Serializable {

    private Integer classOID;
    private String className;
    private Integer semester;
    private Integer curricularYear;

    private Integer degreeCurricularPlanID;
    private String degreeInitials;
    private String nameDegreeCurricularPlan;
    private Integer executionPeriodOID;

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public Integer getClassOID() {
        return classOID;
    }
    public void setClassOID(Integer classOID) {
        this.classOID = classOID;
    }
    public Integer getCurricularYear() {
        return curricularYear;
    }
    public void setCurricularYear(Integer curricularYear) {
        this.curricularYear = curricularYear;
    }
    public Integer getSemester() {
        return semester;
    }
    public void setSemester(Integer semester) {
        this.semester = semester;
    }
    public Integer getDegreeCurricularPlanID() {
        return degreeCurricularPlanID;
    }
    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }
    public String getDegreeInitials() {
        return degreeInitials;
    }
    public void setDegreeInitials(String degreeInitials) {
        this.degreeInitials = degreeInitials;
    }
    public Integer getExecutionPeriodOID() {
        return executionPeriodOID;
    }
    public void setExecutionPeriodOID(Integer executionPeriodOID) {
        this.executionPeriodOID = executionPeriodOID;
    }
    public String getNameDegreeCurricularPlan() {
        return nameDegreeCurricularPlan;
    }
    public void setNameDegreeCurricularPlan(String nameDegreeCurricularPlan) {
        this.nameDegreeCurricularPlan = nameDegreeCurricularPlan;
    }
}
