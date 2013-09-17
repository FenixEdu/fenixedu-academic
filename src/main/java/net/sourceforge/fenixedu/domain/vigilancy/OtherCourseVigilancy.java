package net.sourceforge.fenixedu.domain.vigilancy;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class OtherCourseVigilancy extends OtherCourseVigilancy_Base {

    public OtherCourseVigilancy() {
        super();
    }

    @Override
    public int getEstimatedPoints() {
        return getAssociatedVigilantGroup().getPointsForConvoked();
    }

    @Override
    public int getPoints() {

        if (this.getWrittenEvaluation() == null) {
            throw new DomainException("vigilancy.error.InvalidConvokeNoEvaluationAvailable");
        }

        DateTime currentDate = new DateTime();
        if (currentDate.isBefore(this.getBeginDate())) {
            return POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN;
        }

        if (!isActive() || isStatusUndefined()) {
            return getAssociatedVigilantGroup().getPointsForDisconvoked();
        }
        if (isDismissed()) {
            return getAssociatedVigilantGroup().getPointsForDismissed();
        }
        if (!getWrittenEvaluation().getVigilantsReport()) {
            return POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN;
        }
        if (this.getAttendedToConvoke()) {
            return getAssociatedVigilantGroup().getPointsForConvoked();
        }

        return getAssociatedVigilantGroup().getPointsForMissing();
    }

    public OtherCourseVigilancy(WrittenEvaluation writtenEvaluation) {
        this();
        super.setWrittenEvaluation(writtenEvaluation);
        super.setConfirmed(false);
        super.initStatus();
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
    @Deprecated
    public boolean hasConfirmed() {
        return getConfirmed() != null;
    }

}
