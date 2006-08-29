/*
 * @(#)ExecutionCourseView.java Created on Nov 7, 2004
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.SchoolClass;

/**
 *
 * @author Luis Cruz
 *
 */
public class ClassView {

    private final DomainReference<SchoolClass> schoolClassDomainReference;

    public ClassView(final SchoolClass schoolClass) {
        schoolClassDomainReference = new DomainReference<SchoolClass>(schoolClass);
    }

    public SchoolClass getSchoolClass() {
        return schoolClassDomainReference == null ? null : schoolClassDomainReference.getObject();
    }

    public String getClassName() {
        return getSchoolClass().getNome();
    }

    public Integer getClassOID() {
        return getSchoolClass().getIdInternal();
    }

    public Integer getCurricularYear() {
        return getSchoolClass().getAnoCurricular();
    }

    public Integer getSemester() {
        return getSchoolClass().getExecutionPeriod().getSemester();
    }

    public Integer getDegreeCurricularPlanID() {
        return getSchoolClass().getExecutionDegree().getDegreeCurricularPlan().getIdInternal();
    }

    public String getDegreeInitials() {
        return getSchoolClass().getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla();
    }

    public Integer getExecutionPeriodOID() {
        return getSchoolClass().getExecutionPeriod().getIdInternal();
    }

    public String getNameDegreeCurricularPlan() {
        return getSchoolClass().getExecutionDegree().getDegreeCurricularPlan().getName();
    }

}
