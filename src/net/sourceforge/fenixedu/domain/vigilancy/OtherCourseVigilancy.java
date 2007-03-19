package net.sourceforge.fenixedu.domain.vigilancy;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class OtherCourseVigilancy extends OtherCourseVigilancy_Base {

    public OtherCourseVigilancy() {
	super();
    }

    @Override
    public int getPoints() {

	if (this.getWrittenEvaluation() == null) {
	    throw new DomainException("vigilancy.error.InvalidConvokeNoEvaluationAvailable");
	}

	DateTime currentDate = new DateTime();
	if (currentDate.isBefore(this.getBeginDate()))
	    return this.POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN;

	if (isDismissed()) {
	    return getAssociatedVigilantGroup().getPointsForDismissed();
	}
	if (!isActive()) {
	    return getAssociatedVigilantGroup().getPointsForDisconvoked();
	}
	if (this.getAttendedToConvoke())
	    return getAssociatedVigilantGroup().getPointsForConvoked();
	if (!hasPointsAttributed()) {
	    // no vigilancy has been yet setted to attended so maybe the
	    // coordinator did not yet filled the report. Let's just give
	    // 0 points yet.
	    return this.POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN;
	}

	return getAssociatedVigilantGroup().getPointsForMissing();
    }

    public OtherCourseVigilancy(WrittenEvaluation writtenEvaluation) {
	this();
	this.setWrittenEvaluation(writtenEvaluation);
	this.setConfirmed(false);
	this.setActive(true);
	this.setAttendedToConvoke(false);
    }

    @Override
    public void setConfirmed(Boolean confirmed) {
	if (isSelfAccessing()) {
	    super.setConfirmed(confirmed);
	} else {
	    throw new DomainException("vigilancy.error.notAuthorized");
	}
    }

    public boolean isConfirmed() {
	return getConfirmed();
    }
}
