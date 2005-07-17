package net.sourceforge.fenixedu.domain.degree.enrollment;


/**
 * @author David Santos in Jun 17, 2004
 */

public class NotNeedToEnrollInCurricularCourse extends NotNeedToEnrollInCurricularCourse_Base {

    public NotNeedToEnrollInCurricularCourse() {
        super();
    }
	
	public void delete() {
		removeStudentCurricularPlan();
		removeCurricularCourse();
		deleteDomainObject();
	}

}
