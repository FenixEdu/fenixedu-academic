/*
 * @(#)ExecutionCourseView.java Created on Nov 7, 2004
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.SchoolClass;

/**
 *
 * @author Luis Cruz
 *
 */
public class ClassView {

    private final SchoolClass schoolClass;

    public ClassView(final SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getClassName() {
        return schoolClass.getNome();
    }

    public Integer getClassOID() {
        return schoolClass.getIdInternal();
    }

    public Integer getCurricularYear() {
        return schoolClass.getAnoCurricular();
    }

    public Integer getSemester() {
        return schoolClass.getExecutionPeriod().getSemester();
    }

    public Integer getDegreeCurricularPlanID() {
        return schoolClass.getExecutionDegree().getDegreeCurricularPlan().getIdInternal();
    }

    public String getDegreeInitials() {
        return schoolClass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla();
    }

    public Integer getExecutionPeriodOID() {
        return schoolClass.getExecutionPeriod().getIdInternal();
    }

    public String getNameDegreeCurricularPlan() {
        return schoolClass.getExecutionDegree().getDegreeCurricularPlan().getName();
    }

}
