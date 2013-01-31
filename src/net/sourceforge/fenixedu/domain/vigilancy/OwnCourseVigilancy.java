package net.sourceforge.fenixedu.domain.vigilancy;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;

import org.joda.time.DateTime;

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

		DateTime currentDate = new DateTime();
		if (currentDate.isBefore(this.getBeginDate())) {
			return POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN;
		}

		if (isActive() && !isStatusUndefined()) {
			return isAttended() ? getAssociatedVigilantGroup().getPointsForTeacher() : (isDismissed() ? getAssociatedVigilantGroup()
					.getPointsForDismissedTeacher() : (getWrittenEvaluation().getVigilantsReport() ? getAssociatedVigilantGroup()
					.getPointsForMissingTeacher() : this.POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN));
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
