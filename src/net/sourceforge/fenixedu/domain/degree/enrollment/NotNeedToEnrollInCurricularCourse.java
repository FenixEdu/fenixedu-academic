package net.sourceforge.fenixedu.domain.degree.enrollment;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author David Santos in Jun 17, 2004
 */

public class NotNeedToEnrollInCurricularCourse extends NotNeedToEnrollInCurricularCourse_Base {

    public NotNeedToEnrollInCurricularCourse() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public NotNeedToEnrollInCurricularCourse(CurricularCourse curricularCourse,
	    StudentCurricularPlan studentCurricularPlan) {
	this();
	setCurricularCourse(curricularCourse);
	setStudentCurricularPlan(studentCurricularPlan);
    }

    public void delete() {
	removeStudentCurricularPlan();
	removeCurricularCourse();
	deleteDomainObject();
    }

}
