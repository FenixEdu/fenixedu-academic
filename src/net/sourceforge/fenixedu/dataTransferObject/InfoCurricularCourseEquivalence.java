package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ICurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;

public class InfoCurricularCourseEquivalence extends InfoObject {

	private InfoCurricularCourse infoOldCurricularCourse;
	private InfoCurricularCourse infoEquivalentCurricularCourse;
	private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    public void copyFromDomain(final ICurricularCourseEquivalence curricularCourseEquivalence) {
        super.copyFromDomain(curricularCourseEquivalence);
        if (curricularCourseEquivalence != null) {
        	setInfoOldCurricularCourse(InfoCurricularCourseWithInfoDegreeCurricularPlan.newInfoFromDomain(curricularCourseEquivalence.getOldCurricularCourse()));
        	setInfoEquivalentCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourseEquivalence.getEquivalentCurricularCourse()));
        }
    }

    public static InfoCurricularCourseEquivalence newInfoFromDomain(final ICurricularCourseEquivalence curricularCourseEquivalence) {
    	final InfoCurricularCourseEquivalence infoCurricularCourseEquivalence;

    	if (curricularCourseEquivalence != null) {
    		infoCurricularCourseEquivalence = new InfoCurricularCourseEquivalence();
    		infoCurricularCourseEquivalence.copyFromDomain(curricularCourseEquivalence);
    	} else {
    		infoCurricularCourseEquivalence = null;
    	}

    	return infoCurricularCourseEquivalence;
    }

	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
	}
	public void setInfoDegreeCurricularPlan(
			InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
	}
	public InfoCurricularCourse getInfoEquivalentCurricularCourse() {
		return infoEquivalentCurricularCourse;
	}
	public void setInfoEquivalentCurricularCourse(
			InfoCurricularCourse infoEquivalentCurricularCourse) {
		this.infoEquivalentCurricularCourse = infoEquivalentCurricularCourse;
	}
	public InfoCurricularCourse getInfoOldCurricularCourse() {
		return infoOldCurricularCourse;
	}
	public void setInfoOldCurricularCourse(
			InfoCurricularCourse infoOldCurricularCourse) {
		this.infoOldCurricularCourse = infoOldCurricularCourse;
	}

}