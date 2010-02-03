package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Months;

public class AssiduousnessStatusHistory extends AssiduousnessStatusHistory_Base {

    public AssiduousnessStatusHistory(Assiduousness assiduousness, AssiduousnessStatus assiduousnessStatus, LocalDate beginDate,
	    LocalDate endDate, DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setAssiduousnessStatus(assiduousnessStatus);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

    public AssiduousnessStatusHistory(Assiduousness assiduousness, AssiduousnessStatus assiduousnessStatus, LocalDate beginDate,
	    LocalDate endDate, Employee modifiedBy) {
	if (endDate != null) {
	    if (!endDate.isAfter(beginDate)) {
		throw new DomainException("error.invalidDateInterval");
	    }
	}
	AssiduousnessStatusHistory lastAssiduousnessStatusHistory = assiduousness.getLastAssiduousnessStatusHistory();
	if (lastAssiduousnessStatusHistory != null) {
	    if (lastAssiduousnessStatusHistory.getAssiduousnessStatus().equals(assiduousnessStatus)) {
		throw new DomainException("error.sameAssiduousnessStatus");
	    }

	    if (lastAssiduousnessStatusHistory.getEndDate() == null) {
		if (!beginDate.isAfter(ClosedMonth.getLastClosedLocalDate())) {
		    throw new DomainException("error.datesInclosedMonth");
		}
		lastAssiduousnessStatusHistory.setEndDate(beginDate.minusDays(1));
	    }
	}

	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setAssiduousnessStatus(assiduousnessStatus);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);

    }

    public void editAssiduousnessStatusHistory(LocalDate endDate, Employee modifiedBy) {
	if (endDate != null) {
	    LocalDate lastLocalDateClosed = ClosedMonth.getLastClosedLocalDate();
	    if (endDate.isBefore(lastLocalDateClosed)) {
		throw new DomainException("error.datesInclosedMonth");
	    }
	    if (!endDate.isAfter(getBeginDate())) {
		throw new DomainException("error.invalidDateInterval");
	    }
	}
	setEndDate(endDate);
    }

    public boolean isEditable() {
	return getAssiduousness().getLastAssiduousnessStatusHistory().getIdInternal().equals(getIdInternal())
		&& (getEndDate() == null || !getEndDate().isBefore(ClosedMonth.getLastClosedLocalDate()));
    }

    public boolean isDeletable() {
	return getAssiduousness().getLastAssiduousnessStatusHistory().equals(this)
		&& !getBeginDate().isBefore(ClosedMonth.getLastClosedLocalDate());
    }

    private boolean canBeDeleted() {
	return getAssiduousnessClosedMonths().isEmpty();
    }

    public void delete() {
	if (canBeDeleted()) {
	    removeRootDomainObject();
	    removeAssiduousness();
	    removeAssiduousnessStatus();
	    removeModifiedBy();
	    deleteDomainObject();
	}
    }

    public Duration getSheculeWeightedAverage(LocalDate beginDate, LocalDate endDate) {
	Duration averageWorkPeriodDuration = Duration.ZERO;
	if (beginDate.isBefore(getBeginDate())) {
	    beginDate = getBeginDate();
	}
	if (getEndDate() != null && endDate.isAfter(getEndDate())) {
	    endDate = getEndDate();
	}
	for (LocalDate currentMonth = beginDate; currentMonth.isBefore(endDate); currentMonth = currentMonth.plusMonths(1)) {
	    Duration thisMonthWorkPeriodAverage = Duration.ZERO;
	    LocalDate currentMonthLastDay = new LocalDate(currentMonth.getYear(), currentMonth.getMonthOfYear(), currentMonth
		    .dayOfMonth().getMaximumValue());
	    List<Schedule> schedules = getAssiduousness().getSchedules(currentMonth, currentMonthLastDay);
	    if (schedules.size() != 0) {
		for (Schedule schedule : schedules) {
		    thisMonthWorkPeriodAverage = thisMonthWorkPeriodAverage.plus(schedule.getAverageWorkPeriodDuration());
		}
		thisMonthWorkPeriodAverage = new Duration(thisMonthWorkPeriodAverage.getMillis() / schedules.size());
	    }
	    averageWorkPeriodDuration = averageWorkPeriodDuration.plus(thisMonthWorkPeriodAverage);
	}
	averageWorkPeriodDuration = new Duration(averageWorkPeriodDuration.getMillis()
		/ (Months.monthsBetween(beginDate, endDate).getMonths() + 1));
	return averageWorkPeriodDuration;
    }

    @Override
    public List<AssiduousnessClosedMonth> getAssiduousnessClosedMonths() {
	Map<ClosedMonth, AssiduousnessClosedMonth> assiduousnessClosedMonths = new HashMap<ClosedMonth, AssiduousnessClosedMonth>();
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : super.getAssiduousnessClosedMonths()) {
	    AssiduousnessClosedMonth assiduousnessClosedMonthFromMap = assiduousnessClosedMonths.get(assiduousnessClosedMonth
		    .getClosedMonth());
	    if (assiduousnessClosedMonthFromMap == null
		    || (assiduousnessClosedMonth.getIsCorrection() && (!assiduousnessClosedMonthFromMap.getIsCorrection()))
		    || (assiduousnessClosedMonth.getIsCorrection() && assiduousnessClosedMonthFromMap.getIsCorrection() && assiduousnessClosedMonth
			    .getCorrectedOnClosedMonth().getClosedYearMonth().isAfter(
				    assiduousnessClosedMonthFromMap.getCorrectedOnClosedMonth().getClosedYearMonth()))) {
		assiduousnessClosedMonths.put(assiduousnessClosedMonth.getClosedMonth(), assiduousnessClosedMonth);
	    }
	}
	return new ArrayList<AssiduousnessClosedMonth>(assiduousnessClosedMonths.values());
    }
}
