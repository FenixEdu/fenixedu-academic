package net.sourceforge.fenixedu.domain.vigilancy;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;

public class OwnCourseVigilancy extends OwnCourseVigilancy_Base {

    public OwnCourseVigilancy() {
	super();
    }

    public OwnCourseVigilancy(WrittenEvaluation writtenEvaluation) {
	this();
	this.setWrittenEvaluation(writtenEvaluation);
	this.setActive(true);
	this.setAttendedToConvoke(false);
    }

    @Override
    public int getPoints() {
	if (isActive()) {
	    return isAttended() ? getAssociatedVigilantGroup().getPointsForTeacher()
		    : (isDismissed() ? getAssociatedVigilantGroup().getPointsForDismissed() : getAssociatedVigilantGroup().getPointsForMissing());
	}
	return getAssociatedVigilantGroup().getPointsForDisconvoked();

    }

    public Boolean getConfirmed() {
	return Boolean.TRUE;
    }

    public boolean isConfirmed() {
	return getConfirmed();
    }
}
