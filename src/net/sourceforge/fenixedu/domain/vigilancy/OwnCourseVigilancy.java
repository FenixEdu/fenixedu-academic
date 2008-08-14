package net.sourceforge.fenixedu.domain.vigilancy;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;

public class OwnCourseVigilancy extends OwnCourseVigilancy_Base {

    public OwnCourseVigilancy() {
	super();
    }

    public OwnCourseVigilancy(WrittenEvaluation writtenEvaluation) {
	this();
	this.setWrittenEvaluation(writtenEvaluation);
	super.initStatus();
    }

    @Override
    public int getEstimatedPoints() {
	return getAssociatedVigilantGroup().getPointsForTeacher();
    }

    @Override
    public int getPoints() {
	if (isActive() && !isStatusUndefined()) {
	    return isAttended() ? getAssociatedVigilantGroup().getPointsForTeacher()
		    : (isDismissed() ? getAssociatedVigilantGroup().getPointsForDismissedTeacher()
			    : (hasPointsAttributed() ? getAssociatedVigilantGroup().getPointsForMissingTeacher()
				    : this.POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN));
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
