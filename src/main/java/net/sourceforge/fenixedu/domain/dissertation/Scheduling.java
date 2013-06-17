package net.sourceforge.fenixedu.domain.dissertation;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class Scheduling extends Scheduling_Base {
    
    public  Scheduling() {
        super();
        setAllowSimultaneousCoorientationAndCompanion(false);
        setAttributionByTeachers(false);
        setAllowCandaciesOnlyForStudentsWithADissertationEnrolment(false);
        setCurrentProposalNumber(1);
        setMinimumNumberOfStudents(Integer.valueOf(1));
        setMaximumNumberOfStudents(Integer.valueOf(1));
    }

	private List<DissertationCandidacy> dissertationCandidacies = null;

	public List<DissertationCandidacy> getDissertationCandidacies() {
		return dissertationCandidacies;
	}

	public void setDissertationCandidacies(
			List<DissertationCandidacy> dissertationCandidacies) {
		this.dissertationCandidacies = dissertationCandidacies;
	}
    
    public Date getStartOfProposalPeriod() {
        if (this.getStartOfProposalPeriodDate() != null && this.getStartOfProposalPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getStartOfProposalPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getStartOfProposalPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }
    
    public DateTime getStartOfProposalPeriodDateTime() {
        final YearMonthDay startYearMonthDay = getStartOfProposalPeriodDateYearMonthDay();
        final HourMinuteSecond startHourMinuteSecond = getStartOfProposalPeriodTimeHourMinuteSecond();
        return toDateTime(startYearMonthDay, startHourMinuteSecond);
    }
    
    private DateTime toDateTime(final YearMonthDay yearMonthDay, final HourMinuteSecond hourMinuteSecond) {
        return yearMonthDay == null || hourMinuteSecond == null ? null : new DateTime(yearMonthDay.getYear(),
                yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(), hourMinuteSecond.getHour(),
                hourMinuteSecond.getMinuteOfHour(), hourMinuteSecond.getSecondOfMinute(), 0);
    }
    
    public java.util.Date getStartOfProposalPeriodDate() {
        org.joda.time.YearMonthDay ymd = getStartOfProposalPeriodDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }
    
    public java.util.Date getStartOfProposalPeriodTime() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getStartOfProposalPeriodTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }
    
    public DateTime getEndOfProposalPeriodDateTime() {
        final YearMonthDay startYearMonthDay = getEndOfProposalPeriodDateYearMonthDay();
        final HourMinuteSecond startHourMinuteSecond = getEndOfProposalPeriodTimeHourMinuteSecond();
        return toDateTime(startYearMonthDay, startHourMinuteSecond);
    }
    
    public Date getEndOfProposalPeriod() {
        if (this.getEndOfProposalPeriodDate() != null && this.getEndOfProposalPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getEndOfProposalPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getEndOfProposalPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }
    
    public java.util.Date getEndOfProposalPeriodDate() {
        org.joda.time.YearMonthDay ymd = getEndOfProposalPeriodDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }
    
    public java.util.Date getEndOfProposalPeriodTime() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEndOfProposalPeriodTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

}
