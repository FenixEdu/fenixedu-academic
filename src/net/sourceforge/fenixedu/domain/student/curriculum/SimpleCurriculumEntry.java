package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.CurricularCourse;

abstract public class SimpleCurriculumEntry extends CurriculumEntry {

    private final CurricularCourse curricularCourseDomainReference;

    public SimpleCurriculumEntry(final CurricularCourse curricularCourse) {
        super();
        this.curricularCourseDomainReference = curricularCourse;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourseDomainReference;
    }

}
