package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.List;
import org.joda.time.Duration;
import org.joda.time.YearMonthDay;

public class DailyBalance {
    
	private YearMonthDay date;
	private Duration workedOnNormalWorkPeriod;
    private Duration fixedPeriodAbsence;
    private Duration lunchBreak;
    private Boolean irregular; // corresponde ao A do verbete - anomalia
    private Boolean justification; // corresponde ao J do verbete - justificacao
    private Boolean missingClocking; // indica se houve missing clocking TODO ver isto melhor
    private int overtime; // corresponde ao X do  verbete - horas extraordinarias
    private String comment;
    
    private WorkSchedule workSchedule;
//    private List<AssiduousnessRecord> clockingList;
//	private List<Leave> leaveList;
	
    public DailyBalance() {
        super();
    }

    public DailyBalance(YearMonthDay date, WorkSchedule workSchedule) {
    		this();
    		setDate(date);
    		setWorkSchedule(workSchedule);
    		setComment(null);
    		setIrregular(false);
    		setJustification(false);
    		setOvertime(0);
    		setFixedPeriodAbsence(Duration.ZERO);
    		setWorkedOnNormalWorkPeriod(Duration.ZERO);
    		setLunchBreak(Duration.ZERO);
    }
    
    
//	public List<AssiduousnessRecord> getClockingList() {
//		return clockingList;
//	}

//	// Assigns clockingList to Daily Balance's clockingList. If the list size's is odd sets the irregular (Anomalia) flag;
//	public void setClockingList(List<AssiduousnessRecord> clockingList) {
//		if (clockingList != null) {
//			if (isOdd(clockingList.size())) {
//				setIrregular(true);
//			}
//			this.clockingList = clockingList;
//		}
//	}
    
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
    
//	public List<Leave> getLeaveList() {
//		return leaveList;
//	}
//
//	// Assigns a List of leaves to LeaveList and if there are leaves in the list sets justification to true;
//	public void setLeaveList(List<Leave> leaveList) {
//		if (leaveList != null) {
//			if (leaveList.size() > 0) {
//				setJustification(true);
//			}
//			this.leaveList = leaveList;
//		}
//	}

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
        Duration normalWorkPeriodBalance = getWorkedOnNormalWorkPeriod().minus(getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration());
        Duration normalWorkPeriodAbsence = Duration.ZERO.minus(this.getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration());
//        System.out.println("absence: " + normalWorkPeriodAbsence.toPeriod().toString());
        if (normalWorkPeriodBalance.isShorterThan(normalWorkPeriodAbsence)) {
            System.out.println(normalWorkPeriodBalance.toPeriod().toString() + " e menor que " + normalWorkPeriodAbsence.toPeriod().toString());
            return normalWorkPeriodAbsence;
        } else {
//            System.out.println("wnwp" + getWorkedOnNormalWorkPeriod());
            return getWorkedOnNormalWorkPeriod().minus(getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration());
        }
    }
	
//	// put this on Utils class
//    public boolean isOdd(int number) {
//    		if (number % 2 != 0) {
//    			return true;
//    		}
//    		return false;
//    }
    
}
