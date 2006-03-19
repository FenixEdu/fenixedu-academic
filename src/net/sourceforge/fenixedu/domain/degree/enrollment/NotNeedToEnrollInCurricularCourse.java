package net.sourceforge.fenixedu.domain.degree.enrollment;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author David Santos in Jun 17, 2004
 */

public class NotNeedToEnrollInCurricularCourse extends NotNeedToEnrollInCurricularCourse_Base {

    public NotNeedToEnrollInCurricularCourse() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
	
	public void delete() {
		removeStudentCurricularPlan();
		removeCurricularCourse();
		deleteDomainObject();
	}

}
