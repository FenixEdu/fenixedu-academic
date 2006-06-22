package net.sourceforge.fenixedu.domain.assiduousness;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import java.util.List;

public class DailyBalance {

    private YearMonthDay date;

    private Duration workedOnNormalWorkPeriod;

    private Duration fixedPeriodAbsence;

    private Duration lunchBreak;

    private Boolean irregular; // corresponde ao A do verbete - anomalia

    private Boolean justification; // corresponde ao J do verbete - justificacao

    private Boolean missingClocking; // indica se houve missing clocking TODO ver isto melhor

    private int overtime; // corresponde ao X do verbete - horas extraordinarias

    private String comment;

    private WorkSchedule workSchedule;

    // duracao da compensacao
    private Duration totalBalance;

    public DailyBalance() {
        super();
    }

    public DailyBalance(YearMonthDay date, WorkSchedule workSchedule) {
        this();
        setDate(date);
        setWorkSchedule(workSchedule);
        setComment(null); // necessario?
        setIrregular(false);
        setJustification(false);
        setOvertime(0);
        setFixedPeriodAbsence(Duration.ZERO);
        setWorkedOnNormalWorkPeriod(Duration.ZERO);
        setLunchBreak(Duration.ZERO);
        setTotalBalance(Duration.ZERO);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public YearMonthDay getDate() {
        return date;
    }

    public void setDate(YearMonthDay date) {
        this.date = date;
    }

    public Duration getFixedPeriodAbsence() {
        return fixedPeriodAbsence;
    }

    public void setFixedPeriodAbsence(Duration fixedPeriodAbsence) {
        this.fixedPeriodAbsence = fixedPeriodAbsence;
    }

    public Boolean getIrregular() {
        return irregular;
    }

    public void setIrregular(Boolean irregular) {
        this.irregular = irregular;
    }

    public Boolean getJustification() {
        return justification;
    }

    public void setJustification(Boolean justification) {
        this.justification = justification;
    }

    public Boolean getMissingClocking() {
        return missingClocking;
    }

    public void setMissingClocking(Boolean missingClocking) {
        this.missingClocking = missingClocking;
    }

    public void setTotalBalance(Duration totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Duration getTotalBalance() {
        return totalBalance;
    }

    public Duration getLunchBreak() {
        return lunchBreak;
    }

    public void setLunchBreak(Duration lunchBreak) {
        this.lunchBreak = lunchBreak;
    }

    public Duration getWorkedOnNormalWorkPeriod() {
        return workedOnNormalWorkPeriod;
    }

    public void setWorkedOnNormalWorkPeriod(Duration workedOnNormalWorkPeriod) {
        if (workedOnNormalWorkPeriod.isShorterThan(Duration.ZERO)) {
            this.workedOnNormalWorkPeriod = Duration.ZERO;
        } else {
            this.workedOnNormalWorkPeriod = workedOnNormalWorkPeriod;
        }
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public WorkSchedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(WorkSchedule workSchedule) {
        this.workSchedule = workSchedule;
    }

    public Duration getNormalWorkPeriodBalance() {
        Period normalWorkedPeriod = getWorkedOnNormalWorkPeriod().toPeriod();
        normalWorkedPeriod = normalWorkedPeriod.minusSeconds(normalWorkedPeriod.getSeconds());
        Duration normalWorkPeriodBalance = new Duration(normalWorkedPeriod.toDurationFrom(new DateTime()
                .toDateMidnight())).minus(getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod()
                .getWorkPeriodDuration());
        Duration normalWorkPeriodAbsence = Duration.ZERO.minus(this.getWorkSchedule()
                .getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration());
        if (normalWorkPeriodBalance.isShorterThan(normalWorkPeriodAbsence)) {
            return normalWorkPeriodAbsence;
        } else {
            return normalWorkPeriodBalance;
        }
    }

    public void discountBalanceLeaveInFixedPeriod(List<Leave> balanceLeaveList) {
        Duration balance = Duration.ZERO;
        for (Leave balanceLeave : balanceLeaveList) {
            balance = balance.plus(balanceLeave.getDuration());
        }
        Duration newFixedPeriodAbsence = getFixedPeriodAbsence().minus(balance);
        if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
            setFixedPeriodAbsence(Duration.ZERO);
        } else {
            setFixedPeriodAbsence(newFixedPeriodAbsence);
        }
    }

}
