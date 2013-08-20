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

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public String getClassName() {
        return getSchoolClass().getNome();
    }

    public String getClassOID() {
        return getSchoolClass().getExternalId();
    }

    public Integer getCurricularYear() {
        return getSchoolClass().getAnoCurricular();
    }

    public Integer getSemester() {
        return getSchoolClass().getExecutionPeriod().getSemester();
    }

    public String getDegreeCurricularPlanID() {
        return getSchoolClass().getExecutionDegree().getDegreeCurricularPlan().getExternalId();
    }

    public String getDegreeInitials() {
        return getSchoolClass().getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla();
    }

    public String getExecutionPeriodOID() {
        return getSchoolClass().getExecutionPeriod().getExternalId();
    }

    public String getNameDegreeCurricularPlan() {
        return getSchoolClass().getExecutionDegree().getDegreeCurricularPlan().getName();
    }

}
