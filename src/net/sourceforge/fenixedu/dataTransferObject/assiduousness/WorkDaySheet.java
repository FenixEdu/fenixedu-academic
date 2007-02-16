package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WorkDaySheet implements Serializable {
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");

    YearMonthDay date;

    WorkSchedule workSchedule;

    String workScheduleAcronym;

    Period balanceTime;

    Duration unjustifiedTime;

    Duration unjustifiedTimeWithoutBalanceDiscount;

    Duration complementaryWeeklyRest;

    Duration holidayRest;

    Duration weeklyRest;

    String notes;

    List<AssiduousnessRecord> assiduousnessRecords;

    String clockings;

    List<Leave> leaves;

    Timeline timeline;

    Boolean irregular;

    public WorkDaySheet() {
        setBalanceTime(Duration.ZERO.toPeriod());
        setUnjustifiedTime(Duration.ZERO);
    }

    public WorkDaySheet(YearMonthDay day, WorkSchedule workSchedule,
            List<AssiduousnessRecord> clockings, List<Leave> list) {
        setBalanceTime(Duration.ZERO.toPeriod());
        setUnjustifiedTime(Duration.ZERO);
        setDate(day);
        setWorkSchedule(workSchedule);
        setLeaves(list);
        setAssiduousnessRecords(clockings);
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public Period getBalanceTime() {
        return balanceTime;
    }

    public void setBalanceTime(Period balanceTime) {
        this.balanceTime = balanceTime;
    }

    public YearMonthDay getDate() {
        return date;
    }

    public void setDate(YearMonthDay date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void addNote(String note) {
        if (notes != null && notes.length() != 0) {
            this.notes = notes.concat(" / ");
        } else if (notes == null) {
            notes = new String();
        }
        this.notes = notes.concat(note);
    }

    public Duration getUnjustifiedTime() {
        return unjustifiedTime;
    }

    public void setUnjustifiedTime(Duration unjustifiedTime) {
        this.unjustifiedTime = unjustifiedTime;
    }

    public String getWorkScheduleAcronym() {
        return workScheduleAcronym;
    }

    public void setWorkScheduleAcronym(String workScheduleAcronym) {
        this.workScheduleAcronym = workScheduleAcronym;
    }

    public String getDateFormatted() {
        if (getDate() == null) {
            return "";
        }
        return getDate().toString("dd/MM/yyyy");
    }

    public String getBalanceTimeFormatted() {
        Period balancePeriod = getBalanceTime();
        StringBuffer result = new StringBuffer();
        result.append(balancePeriod.getHours());
        result.append(":");
        if (balancePeriod.getMinutes() > -10 && balancePeriod.getMinutes() < 10) {
            result.append("0");
        }
        if (balancePeriod.getMinutes() < 0) {
            result.append((-balancePeriod.getMinutes()));
            if (!result.toString().startsWith("-")) {
                result = new StringBuffer("-").append(result);
            }
        } else {
            result.append(balancePeriod.getMinutes());
        }
        return result.toString();
    }

    public String getUnjustifiedTimeFormatted() {
        Period unjustifiedPeriod = getUnjustifiedTime().toPeriod();
        StringBuffer result = new StringBuffer();
        result.append(unjustifiedPeriod.getHours());
        result.append(":");
        if (unjustifiedPeriod.getMinutes() > -10 && unjustifiedPeriod.getMinutes() < 10) {
            result.append("0");
        }

        result.append(unjustifiedPeriod.getMinutes());

        return result.toString();
    }

    public String getClockingsFormatted() {
        return clockings;
    }

    public String getWeekDay() {
        if (getDate() == null) {
            return "";
        }
        ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
                LanguageUtils.getLocale());
        return bundle.getString(WeekDay.fromJodaTimeToWeekDay(getDate().toDateTimeAtMidnight())
                .toString()
                + "_ACRONYM");
    }

    public void setAssiduousnessRecords(final List<AssiduousnessRecord> assiduousnessRecords) {
        this.assiduousnessRecords = assiduousnessRecords;
        final StringBuilder result = new StringBuilder();
        if (assiduousnessRecords != null) {
            for (final AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
                final TimeOfDay timeOfDay = assiduousnessRecord.getDate().toTimeOfDay();
                if (result.length() != 0) {
                    result.append(", ");
                }
                result.append(fmt.print(timeOfDay));
            }
        }
        clockings = " " + result.toString();
    }

    public List<AssiduousnessRecord> getAssiduousnessRecords() {
        return assiduousnessRecords;
    }

    public List<Leave> getLeaves() {
        if (leaves == null) {
            setLeaves(new ArrayList<Leave>());
        }
        return leaves;
    }

    public void setLeaves(List<Leave> leaves) {
        this.leaves = leaves;
    }

    public void addLeaves(List<Leave> list) {
        getLeaves().addAll(list);
    }

    public WorkSchedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(WorkSchedule workSchedule) {
        this.workSchedule = workSchedule;
    }

    public Duration getComplementaryWeeklyRest() {
        if (complementaryWeeklyRest == null) {
            return Duration.ZERO;
        }
        return complementaryWeeklyRest;
    }

    public void setComplementaryWeeklyRest(Duration complementaryWeeklyRest) {
        this.complementaryWeeklyRest = complementaryWeeklyRest;
    }

    public Duration getWeeklyRest() {
        if (weeklyRest == null) {
            return Duration.ZERO;
        }
        return weeklyRest;
    }

    public void setWeeklyRest(Duration weeklyRest) {
        this.weeklyRest = weeklyRest;
    }

    public void discountBalanceLeaveInFixedPeriod(List<Leave> balanceLeaveList) {
        setUnjustifiedTimeWithoutBalanceDiscount(getUnjustifiedTime());
        Duration balance = Duration.ZERO;
        for (Leave balanceLeave : balanceLeaveList) {
            balance = balance.plus(balanceLeave.getDuration());
        }
        Duration newFixedPeriodAbsence = getUnjustifiedTime().minus(balance);
        if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
            setUnjustifiedTime(Duration.ZERO);
        } else {
            setUnjustifiedTime(newFixedPeriodAbsence);
        }
    }

    public Duration getHolidayRest() {
        return holidayRest;
    }

    public void setHolidayRest(Duration holidayRest) {
        this.holidayRest = holidayRest;
    }

    public Duration getUnjustifiedTimeWithoutBalanceDiscount() {
        return unjustifiedTimeWithoutBalanceDiscount;
    }

    public void setUnjustifiedTimeWithoutBalanceDiscount(Duration unjustifiedTimeWithoutBalanceDiscount) {
        this.unjustifiedTimeWithoutBalanceDiscount = unjustifiedTimeWithoutBalanceDiscount;
    }

    public Boolean getIrregular() {
        return irregular == null ? false : irregular;
    }

    public void setIrregular(Boolean irregular) {
        this.irregular = irregular;
    }

    public void setIrregularDay(Boolean irregular) {
        this.irregular = irregular;
        ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
                LanguageUtils.getLocale());
        addNote(bundle.getString("label.irregular"));
    }

    public Duration getLeaveDuration(final YearMonthDay thisDay, final WorkSchedule workSchedule,
            final Leave leave) {
        Duration leaveDuration = Duration.ZERO;
        if (!getIrregular()) {
            Duration overlapsDuration = Duration.ZERO;

            Interval interval = workSchedule.getWorkScheduleType().getNormalWorkPeriod()
                    .getNotWorkingPeriod(thisDay);
            if (interval != null
                    && (getTimeline() == null || ((!interval.contains(leave.getDate()) || getTimeline()
                            .hasWorkingPointBeforeLeave(leave)) && (!interval.contains(leave
                            .getEndDate()) || getTimeline().hasWorkingPointAfterLeave(leave)))

                    )) {
                Interval overlaps = interval.overlap(leave.getTotalInterval());
                if (overlaps != null) {
                    overlapsDuration = overlaps.toDuration();
                }
            }

            if ((leave.getDuration().minus(overlapsDuration)).isLongerThan(workSchedule
                    .getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration())) {
                leaveDuration = leaveDuration.plus(workSchedule.getWorkScheduleType()
                        .getNormalWorkPeriod().getWorkPeriodDuration());
            } else {
                leaveDuration = leaveDuration.plus(leave.getDuration().minus(overlapsDuration));
            }
        }
        return leaveDuration;
    }

}
