package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;

public class EditExternalCurricularCourseBean extends CreateExternalCurricularCourseBean {

    private ExternalCurricularCourse externalCurricularCourse;

    public EditExternalCurricularCourseBean(final ExternalCurricularCourse externalCurricularCourse) {
        setParentUnit(externalCurricularCourse.getUnit());
        setName(externalCurricularCourse.getName());
        setCode(externalCurricularCourse.getCode());
        setEnrolStudent(false);
        setExternalEnrolmentBean(null);

        setExternalCurricularCourse(externalCurricularCourse);
    }

    public ExternalCurricularCourse getExternalCurricularCourse() {
        return this.externalCurricularCourse;
    }

    public void setExternalCurricularCourse(final ExternalCurricularCourse externalCurricularCourse) {
        this.externalCurricularCourse = externalCurricularCourse;
    }

}
