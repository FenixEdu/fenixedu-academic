package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.HashMap;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class ClosedMonthJustification extends ClosedMonthJustification_Base {

    public ClosedMonthJustification(AssiduousnessClosedMonth assiduousnessClosedMonth, JustificationMotive justificationMotive,
	    Duration duration) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	setJustificationMotive(justificationMotive);
	setJustificationDuration(duration);
	setIsCorrection(Boolean.FALSE);
	setLastModifiedDate(new DateTime());
    }

    public ClosedMonthJustification(EmployeeWorkSheet employeeWorkSheet, ClosedMonth correctionClosedMonth,
	    AssiduousnessClosedMonth assiduousnessClosedMonth, JustificationMotive justificationMotive) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	setJustificationMotive(justificationMotive);
	Duration justificationDuration = employeeWorkSheet.getJustificationsDuration().get(getJustificationMotive());
	setJustificationDuration(justificationDuration == null ? Duration.ZERO : justificationDuration);
	setIsCorrection(Boolean.TRUE);
	setCorrectedOnClosedMonth(correctionClosedMonth);
	setLastModifiedDate(new DateTime());
    }

    public void delete() {
	removeRootDomainObject();
	removeJustificationMotive();
	removeAssiduousnessClosedMonth();
	removeCorrectedOnClosedMonth();
	deleteDomainObject();
    }

    public boolean hasEqualValues(EmployeeWorkSheet employeeWorkSheet) {
	return employeeWorkSheet.getJustificationsDuration().get(getJustificationMotive()) != null
		&& (getJustificationDuration()
			.equals(employeeWorkSheet.getJustificationsDuration().get(getJustificationMotive())));
    }

    public void correct(EmployeeWorkSheet employeeWorkSheet) {
	Duration justificationDuration = employeeWorkSheet.getJustificationsDuration().get(getJustificationMotive());
	setJustificationDuration(justificationDuration == null ? Duration.ZERO : justificationDuration);
	setIsCorrection(Boolean.TRUE);
	setLastModifiedDate(new DateTime());
    }

    public ClosedMonthJustification getOldClosedMonthJustification() {
	ClosedMonthJustification oldClosedMonthJustification = null;
	if (getIsCorrection()) {
	    for (ClosedMonthJustification closedMonthJustification : getAssiduousnessClosedMonth()
		    .getAllClosedMonthJustifications()) {
		if ((!closedMonthJustification.equals(this))
			&& closedMonthJustification.getJustificationMotive().equals(getJustificationMotive())
			&& (oldClosedMonthJustification == null || ((!oldClosedMonthJustification.getIsCorrection()) || (closedMonthJustification
				.getIsCorrection() && closedMonthJustification.getCorrectedOnClosedMonth().getClosedYearMonth()
				.isAfter(oldClosedMonthJustification.getCorrectedOnClosedMonth().getClosedYearMonth()))

			))) {
		    oldClosedMonthJustification = closedMonthJustification;
		}
	    }
	}
	return oldClosedMonthJustification;
    }

    public int getJustificationDays(HashMap<String, Duration> pastJustificationsDurations) {
	Duration pastDurationToDiscount = Duration.ZERO;
	AssiduousnessStatusHistory assiduousnessStatusHistory = getAssiduousnessClosedMonth().getAssiduousnessStatusHistory();
	String giafCode = getJustificationMotive().getGiafCode(assiduousnessStatusHistory);
	Duration pastDuration = pastJustificationsDurations.get(giafCode);
	int scheduleHours = assiduousnessStatusHistory.getAssiduousness().getAverageWorkTimeDuration(
		getAssiduousnessClosedMonth().getBeginDate(), getAssiduousnessClosedMonth().getEndDate()).toPeriod(
		PeriodType.dayTime()).getHours();
	if (pastDuration != null) {
	    Period pastToDiscount = Period.hours(pastDuration.toPeriod().getHours() % scheduleHours).withMinutes(
		    pastDuration.toPeriod().getMinutes());

	    pastDurationToDiscount = pastToDiscount.toDurationFrom(new DateMidnight());
	}
	pastDurationToDiscount = pastDurationToDiscount.plus(getJustificationDuration());
	return pastDurationToDiscount.toPeriod().getHours() / scheduleHours;
    }

}
