package net.sourceforge.fenixedu.domain.assiduousness;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
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
    }

    public HashMap<JustificationMotive, Duration> getPastJustificationsDurations() {
        HashMap<JustificationMotive, Duration> pastJustificationsDurations = new HashMap<JustificationMotive, Duration>();
        for (AssiduousnessClosedMonth assiduousnessClosedMonth : getAssiduousness()
                .getAssiduousnessClosedMonths()) {
            if (assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(
                    DateTimeFieldType.year()) == getClosedMonth().getClosedYearMonth().get(
                    DateTimeFieldType.year())) {
                for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth
                        .getClosedMonthJustifications()) {
                    Duration duration = pastJustificationsDurations.get(closedMonthJustification);
                    if (duration == null) {
                        duration = Duration.ZERO;
                    }
                    duration = duration.plus(closedMonthJustification.getJustificationDuration());
                    pastJustificationsDurations.put(closedMonthJustification.getJustificationMotive(),
                            duration);
                }
            }
        }
        return pastJustificationsDurations;
    }

    public double getTotalUnjustifiedPercentage() {
        double unjustified = 0;
        if (getBalance().isLongerThan(Duration.ZERO)) {
            unjustified = getUnjustifiedPercentage(getAssiduousnessExtraWorks());
        } else if (getBalance().isShorterThan(Duration.ZERO)) {
            Duration unjustifiedTotalDuration = Duration.ZERO;
            for (AssiduousnessExtraWork extraWork : getAssiduousnessExtraWorks()) {
                unjustifiedTotalDuration = unjustifiedTotalDuration.plus(extraWork.getUnjustified());
            }
            YearMonthDay beginDate = new YearMonthDay(getClosedMonth().getClosedYearMonth().get(
                    DateTimeFieldType.year()), getClosedMonth().getClosedYearMonth().get(
                    DateTimeFieldType.monthOfYear()), 01);
            YearMonthDay endDate = new YearMonthDay(getClosedMonth().getClosedYearMonth().get(
                    DateTimeFieldType.year()), getClosedMonth().getClosedYearMonth().get(
                    DateTimeFieldType.monthOfYear()), beginDate.dayOfMonth().getMaximumValue());
            List<Schedule> schedules = getAssiduousness().getSchedules(beginDate, endDate);
            Duration averageWorkTimeDuration = Duration.ZERO;
            for (Schedule schedule : schedules) {
                averageWorkTimeDuration = averageWorkTimeDuration.plus(schedule
                        .getAverageWorkPeriodDuration());
            }
            averageWorkTimeDuration = new Duration(averageWorkTimeDuration.getMillis()
                    / schedules.size());
            long balanceToProcess = Math.abs(getBalance().getMillis()) > unjustifiedTotalDuration
                    .getMillis() ? Math.abs(getBalance().getMillis()) : unjustifiedTotalDuration
                    .getMillis();
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
        for (AssiduousnessExtraWork extraWork : assiduousnessExtraWorks) {
            if (extraWork.getUnjustified().isLongerThan(Duration.ZERO)) {
                long unjustifiedAfterTolerance = extraWork.getUnjustified().getMillis()
                        - Assiduousness.IST_TOLERANCE_TIME.getMillis();
                if (unjustifiedAfterTolerance > 0) {
                    unjustified += ((double) unjustifiedAfterTolerance / (double) extraWork
                            .getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration()
                            .getMillis());
                }
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
}
