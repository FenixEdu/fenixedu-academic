package net.sourceforge.fenixedu.domain.enrolment;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class OptionalDegreeModuleToEnrol extends DegreeModuleToEnrol {

    private DomainReference<CurricularCourse> curricularCourse;

    public OptionalDegreeModuleToEnrol(CurriculumGroup curriculumGroup, Context context,
	    CurricularCourse curricularCourse) {
	super(curriculumGroup, context);
	setCurricularCourse(curricularCourse);

    }

    public CurricularCourse getCurricularCourse() {
	return (this.curricularCourse != null) ? this.curricularCourse.getObject() : null;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
	this.curricularCourse = (curricularCourse != null) ? new DomainReference<CurricularCourse>(
		curricularCourse) : null;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof OptionalDegreeModuleToEnrol) {
	    return super.equals(obj)
		    && ((OptionalDegreeModuleToEnrol) obj).getCurricularCourse() == getCurricularCourse();
	}

	return false;
    }

    public String getKey() {
	return super.getKey() + getCurricularCourse().getClass().getName() + ":"
		+ getCurricularCourse().getIdInternal();
    }
}
