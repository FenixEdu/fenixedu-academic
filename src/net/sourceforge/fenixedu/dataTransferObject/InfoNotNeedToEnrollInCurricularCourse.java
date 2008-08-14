/**
 * Jul 26, 2005
 */
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InfoNotNeedToEnrollInCurricularCourse extends InfoObject {

    private InfoCurricularCourse infoCurricularCourse;

    private InfoStudentCurricularPlan studentCurricularPlan;

    public InfoNotNeedToEnrollInCurricularCourse() {
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
	return infoCurricularCourse;
    }

    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
	this.infoCurricularCourse = infoCurricularCourse;
    }

    public InfoStudentCurricularPlan getStudentCurricularPlan() {
	return studentCurricularPlan;
    }

    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = studentCurricularPlan;
    }

    public void copyFromDomain(NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse) {
	super.copyFromDomain(notNeedToEnrollInCurricularCourse);

	if (notNeedToEnrollInCurricularCourse != null) {
	    setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(notNeedToEnrollInCurricularCourse
		    .getCurricularCourse()));
	    setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(notNeedToEnrollInCurricularCourse
		    .getStudentCurricularPlan()));
	}
    }

    public static InfoNotNeedToEnrollInCurricularCourse newInfoFromDomain(
	    NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse) {
	InfoNotNeedToEnrollInCurricularCourse infoNotNeedToEnrollInCurricularCourse = null;
	if (notNeedToEnrollInCurricularCourse != null) {
	    infoNotNeedToEnrollInCurricularCourse = new InfoNotNeedToEnrollInCurricularCourse();
	    infoNotNeedToEnrollInCurricularCourse.copyFromDomain(notNeedToEnrollInCurricularCourse);
	}
	return infoNotNeedToEnrollInCurricularCourse;
    }

}
