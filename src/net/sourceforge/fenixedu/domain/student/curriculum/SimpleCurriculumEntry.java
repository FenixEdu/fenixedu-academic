package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;

public abstract class SimpleCurriculumEntry extends CurriculumEntry {

    private final DomainReference<CurricularCourse> curricularCourseDomainReference;

    public SimpleCurriculumEntry(final CurricularCourse curricularCourse) {
        super();
        this.curricularCourseDomainReference = new DomainReference<CurricularCourse>(curricularCourse);
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourseDomainReference.getObject();
    }

}