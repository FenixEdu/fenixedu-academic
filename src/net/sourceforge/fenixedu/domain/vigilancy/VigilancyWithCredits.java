package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class VigilancyWithCredits extends VigilancyWithCredits_Base {
	private static final int POINTS_WON_FOR_ATTENDING_CONVOKE = 1;

	private static final int POINTS_WON_FOR_MISSING_CONVOKE = -2;

	private static final int POINTS_WON_FOR_NON_ATTENDING_CONVOKE = 0;

	private static final int POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN = 0;

	public VigilancyWithCredits() {
		super();
	}

	public VigilancyWithCredits(WrittenEvaluation writtenEvaluation) {
		this();
		this.setWrittenEvaluation(writtenEvaluation);
		this.setConfirmed(false);
		this.setActive(true);
		this.setAttendedToConvoke(false);
	}

	/*
	 * DO NOT USE THIS METHOD: it's for testing porpose only! It overrides the
	 * setters logic. So do not use it!
	 */

	private VigilancyWithCredits(Boolean confirmed, Boolean active,
			Boolean attended, WrittenEvaluation writtenEvaluation) {
		super();
		super.setConfirmed(confirmed);
		super.setActive(active);

		super.setAttendedToConvoke(attended);
		super.setWrittenEvaluation(writtenEvaluation);

	}

	@Override
	public int getPoints() {
		
		
		if (this.getWrittenEvaluation() == null) {
			throw new DomainException(
					"vigilancy.error.InvalidConvokeNoEvaluationAvailable");
		}
		
		DateTime currentDate = new DateTime();
		if (currentDate.isBefore(this.getBeginDate()))
			return this.POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN;

		if (!this.getActive())
			return this.POINTS_WON_FOR_NON_ATTENDING_CONVOKE;
		if (this.getAttendedToConvoke())
			return this.POINTS_WON_FOR_ATTENDING_CONVOKE;
		if(!atLeastOneVigilancyWithCreditsIsAttended()) {
			// no vigilancy has been yet setted to attended so maybe the 
			// coordinator did not yet filled the report. Let's just give
			// 0 points yet.
			return this.POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN;
		}
		
		return this.POINTS_WON_FOR_MISSING_CONVOKE;
	}

	@Override
	public void setActive(Boolean bool) {
		YearMonthDay examDate = this.getWrittenEvaluation()
				.getDayDateYearMonthDay();
		YearMonthDay currentDate = new YearMonthDay();

		if (examDate.isAfter(currentDate)) {
			super.setActive(bool);
		} else {
			throw new DomainException("vigilancy.error.cannotChangeActive");
		}
	}

	@Override
	public void setAttendedToConvoke(Boolean bool) {
		YearMonthDay date = this.getWrittenEvaluation()
				.getDayDateYearMonthDay();
		YearMonthDay currentDate = new YearMonthDay();

		if (!bool) {
			super.setAttendedToConvoke(false);
		} else {
			if (bool && currentDate.isAfter(date)) {
				super.setAttendedToConvoke(true);
			} else {
				throw new DomainException(
						"vigilancy.error.cannotChangeAttended");
			}
		}
	}

	/*
	 * Used by JSP displayConvokes.jsp in order to know if has to show link or
	 * not. If renderers get more flexible this method might be discard (which
	 * is good!)
	 */
	public boolean isNotConfirmed() {
		return this.getConfirmed() == false;
	}

	public boolean isAttended() {
		return this.getAttendedToConvoke();
	}

	public boolean isNotAttended() {
		return !this.getAttendedToConvoke();
	}

	public boolean isActive() {
		return this.getActive();
	}

	public boolean isNotActive() {
		return !this.getActive();
	}

	private boolean atLeastOneVigilancyWithCreditsIsAttended() {
		List<VigilancyWithCredits> vigilancies = this.getWrittenEvaluation().getVigilancysWithCredits();
		for(VigilancyWithCredits vigilancy : vigilancies) {
			if(vigilancy.isAttended()) return true;
		}
		
		return false;
	}
	
}
