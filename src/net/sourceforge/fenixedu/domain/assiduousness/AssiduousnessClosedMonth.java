package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.NumberUtils;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class AssiduousnessClosedMonth extends AssiduousnessClosedMonth_Base {

    public AssiduousnessClosedMonth(Assiduousness assiduousness, ClosedMonth closedMonth,
	    Duration balance, Duration totalComplementaryWeeklyRestBalance,
	    Duration totalWeeklyRestBalance, Duration holidayRest, Duration balanceToDiscount,
	    double vacations, double tolerance, double article17, double article66) {
	setRootDomainObject(RootDomainObject.getInstance());
	setBalance(balance);
	setBalanceToDiscount(balanceToDiscount);
	setAssiduousness(assiduousness);
	setClosedMonth(closedMonth);
	setSaturdayBalance(totalComplementaryWeeklyRestBalance);
	setSundayBalance(totalWeeklyRestBalance);
	setHolidayBalance(holidayRest);
	setVacations(vacations);
	setTolerance(tolerance);
	setArticle17(article17);
	setArticle66(article66);

	setAllUnjustifiedAndAccumulatedArticle66();
    }

    public HashMap<Integer, Duration> getPastJustificationsDurations() {
	HashMap<Integer, Duration> pastJustificationsDurations = new HashMap<Integer, Duration>();
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : getAssiduousness()
		.getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(
		    DateTimeFieldType.year()) == getClosedMonth().getClosedYearMonth().get(
		    DateTimeFieldType.year())
		    && assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(
			    DateTimeFieldType.monthOfYear()) < getClosedMonth().getClosedYearMonth()
			    .get(DateTimeFieldType.monthOfYear())) {
		for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth
			.getClosedMonthJustifications()) {
		    YearMonthDay day = new YearMonthDay(assiduousnessClosedMonth.getClosedMonth()
			    .getClosedYearMonth().get(DateTimeFieldType.year()),
			    assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(
				    DateTimeFieldType.monthOfYear()), 1);
		    Integer code = closedMonthJustification.getJustificationMotive().getGiafCode(
			    assiduousnessClosedMonth.getAssiduousness(), day);
		    Duration duration = pastJustificationsDurations.get(code);
		    if (duration == null) {
			duration = Duration.ZERO;
		    }
		    duration = duration.plus(closedMonthJustification.getJustificationDuration());
		    pastJustificationsDurations.put(code, duration);
		}
	    }
	}
	return pastJustificationsDurations;
    }

    private double getTotalUnjustifiedPercentage(YearMonthDay beginDate, YearMonthDay endDate) {
	double unjustified = 0;
	Duration balanceWithoutDiscount = getBalance().plus(getBalanceToDiscount());
	if (balanceWithoutDiscount.isLongerThan(Duration.ZERO)) {
	    unjustified = getUnjustifiedPercentage(getAssiduousnessExtraWorks());
	} else if (balanceWithoutDiscount.isShorterThan(Duration.ZERO)) {
	    Duration unjustifiedTotalDuration = Duration.ZERO;
	    for (AssiduousnessExtraWork extraWork : getAssiduousnessExtraWorks()) {
		unjustifiedTotalDuration = unjustifiedTotalDuration.plus(extraWork.getUnjustified());
	    }
	    List<Schedule> schedules = getAssiduousness().getSchedules(beginDate, endDate);
	    Duration averageWorkTimeDuration = Duration.ZERO;
	    for (Schedule schedule : schedules) {
		averageWorkTimeDuration = averageWorkTimeDuration.plus(schedule
			.getAverageWorkPeriodDuration());
	    }
	    averageWorkTimeDuration = new Duration(averageWorkTimeDuration.getMillis()
		    / schedules.size());
	    long balanceToProcess = Math.abs(balanceWithoutDiscount.getMillis()) > unjustifiedTotalDuration
		    .getMillis() ? Math.abs(balanceWithoutDiscount.getMillis())
		    : unjustifiedTotalDuration.getMillis();
	    long balanceAfterTolerance = balanceToProcess - Assiduousness.IST_TOLERANCE_TIME.getMillis();
	    if (balanceAfterTolerance > 0) {
		unjustified = (double) balanceAfterTolerance
			/ (double) averageWorkTimeDuration.getMillis();
	    }
	}
	return unjustified;
    }

    private double getUnjustifiedPercentage(List<AssiduousnessExtraWork> assiduousnessExtraWorks) {
	double unjustified = 0;
	long tempIstTorelanceTime = Assiduousness.IST_TOLERANCE_TIME.getMillis();
	for (AssiduousnessExtraWork extraWork : assiduousnessExtraWorks) {
	    if (extraWork.getUnjustified().isLongerThan(Duration.ZERO)) {
		long unjustifiedAfterTolerance = extraWork.getUnjustified().getMillis()
			- tempIstTorelanceTime;
		if (unjustifiedAfterTolerance > 0) {
		    unjustified += ((double) unjustifiedAfterTolerance / (double) extraWork
			    .getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration()
			    .getMillis());
		}
		tempIstTorelanceTime = unjustifiedAfterTolerance > 0 ? 0 : tempIstTorelanceTime
			- extraWork.getUnjustified().getMillis();
	    }
	}
	return unjustified;
    }

    public void delete() {
	removeRootDomainObject();
	removeAssiduousness();
	List<AssiduousnessExtraWork> assiduousnessExtraWorks = new ArrayList<AssiduousnessExtraWork>(
		getAssiduousnessExtraWorks());
	for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
	    getAssiduousnessExtraWorks().remove(assiduousnessExtraWork);
	    assiduousnessExtraWork.delete();
	}
	List<ClosedMonthJustification> closedMonthJustifications = new ArrayList<ClosedMonthJustification>(
		getClosedMonthJustifications());
	for (ClosedMonthJustification closedMonthJustification : closedMonthJustifications) {
	    getClosedMonthJustifications().remove(closedMonthJustification);
	    closedMonthJustification.delete();
	}
	deleteDomainObject();
    }

    public void setAllUnjustifiedAndAccumulatedArticle66() {
	// TODO quando estiver tudo normalizado, chamar o ultimo mes fechado e
	// tornar este método privado
	AssiduousnessClosedMonth lastAssiduousnessClosedMonth = getPreviousAssiduousnessClosedMonth();
	double unjustified = 0;
	double a66 = 0;
	double previousAccumulatedA66 = 0;
	double previousA66 = 0;
	double previousUnjustified = 0;

	if (lastAssiduousnessClosedMonth != null) {
	    previousAccumulatedA66 = lastAssiduousnessClosedMonth.getAccumulatedArticle66();
	    previousA66 = lastAssiduousnessClosedMonth.getArticle66();
	    previousUnjustified = lastAssiduousnessClosedMonth.getAccumulatedUnjustified();
	}
	YearMonthDay beginDate = new YearMonthDay(getClosedMonth().getClosedYearMonth().get(
		DateTimeFieldType.year()), getClosedMonth().getClosedYearMonth().get(
		DateTimeFieldType.monthOfYear()), 01);
	YearMonthDay endDate = new YearMonthDay(getClosedMonth().getClosedYearMonth().get(
		DateTimeFieldType.year()), getClosedMonth().getClosedYearMonth().get(
		DateTimeFieldType.monthOfYear()), beginDate.dayOfMonth().getMaximumValue());
	unjustified = getTotalUnjustifiedPercentage(beginDate, endDate);
	unjustified = NumberUtils.formatDoubleWithoutRound(unjustified, 1);

	boolean isToProcess = false;

	// only for permanent staff
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousness()
		.getStatusBetween(beginDate, endDate)) {
	    String status = assiduousnessStatusHistory.getAssiduousnessStatus().getDescription();
	    if (!status.equalsIgnoreCase("Contrato a termo certo")) {
		isToProcess = true;
		break;
	    }
	}
	if (isToProcess && getArticle66() < Assiduousness.MAX_A66_PER_MONTH
		&& (previousAccumulatedA66 + previousA66) < Assiduousness.MAX_A66_PER_YEAR
		&& unjustified > 0) {
	    double remaining = Assiduousness.MAX_A66_PER_MONTH - getArticle66();
	    if (unjustified <= remaining) {
		a66 = unjustified;
		unjustified = 0;
	    } else {
		unjustified -= remaining;
		a66 = remaining;
	    }
	}
	a66 = NumberUtils.formatDoubleWithoutRound(a66, 1);
	setAccumulatedArticle66(previousAccumulatedA66 + a66);
	setAccumulatedUnjustified(previousUnjustified + unjustified);

	int countUnjustifiedLeaves = 0;
	for (Leave leave : getAssiduousness().getLeaves(beginDate, endDate)) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("FINJUST")) {
		countUnjustifiedLeaves++;
	    }
	}
	setUnjustifiedDays(countUnjustifiedLeaves);
    }

    public AssiduousnessClosedMonth getPreviousAssiduousnessClosedMonth() {
	Partial partial = getClosedMonth().getClosedYearMonth();
	int previousMonth = partial.get(DateTimeFieldType.monthOfYear()) - 1;
	if (previousMonth <= 0) {
	    return null;
	}
	ClosedMonth previousClosedMonth = ClosedMonth.getClosedMonth(new YearMonth(partial
		.get(DateTimeFieldType.year()), previousMonth));
	if (previousClosedMonth != null) {
	    AssiduousnessClosedMonth assiduousnessClosedMonth = previousClosedMonth
		    .getAssiduousnessClosedMonth(getAssiduousness());
	    if (assiduousnessClosedMonth != null) {
		return assiduousnessClosedMonth;
	    }
	}
	return null;
    }

    public Duration getTotalNightBalance() {
	Duration result = Duration.ZERO;
	for (AssiduousnessExtraWork assiduousnessExtraWork : getAssiduousnessExtraWorks()) {
	    result = result.plus(assiduousnessExtraWork.getNightBalance());
	}
	return result;
    }
}
