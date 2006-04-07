package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.List;
import org.joda.time.Duration;
import org.joda.time.YearMonthDay;

public class DailyBalance {
    
	private YearMonthDay date;
    	private Duration normalWorkPeriod1Balance;
    private Duration normalWorkPeriod2Balance;
    private Duration fixedPeriodAbsence;
    private Duration lunchBreak;
    private Boolean irregular; // corresponde ao A do verbete - anomalia
    private Boolean justification; // corresponde ao J do verbete - justificacao
    private int overtime; // corresponde ao X do verbete - horas extraordinarias
    private String comment;
    
    private WorkSchedule workSchedule;
    private List<Clocking> clockingList;
	private List<Leave> leaveList;
	
    public DailyBalance() {
        super();
    }

    public DailyBalance(YearMonthDay date) {
    		this();
    		setDate(date);
    		setWorkSchedule(null);
    		setClockingList(null);
    		setLeaveList(null);
    		setComment(null);
    		setIrregular(false);
    		setJustification(false);
    		setOvertime(0);
    		
    		setFixedPeriodAbsence(Duration.ZERO);
    		setNormalWorkPeriod1Balance(Duration.ZERO);
    		setNormalWorkPeriod2Balance(Duration.ZERO);
    		setLunchBreak(Duration.ZERO);
    }
    
    
	public List<Clocking> getClockingList() {
		return clockingList;
	}

	// Assigns clockingList to Daily Balance's clockingList. If the list size's is odd sets the irregular (Anomalia) flag;
	public void setClockingList(List<Clocking> clockingList) {
		if (clockingList != null) {
			if (isOdd(clockingList.size())) {
				setIrregular(true);
			}
			this.clockingList = clockingList;
		}
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

	public List<Leave> getLeaveList() {
		return leaveList;
	}

	// Assigns a List of leaves to LeaveList and if there are leaves in the list sets justification to true;
	public void setLeaveList(List<Leave> leaveList) {
		if (leaveList != null) {
			if (leaveList.size() > 0) {
				setJustification(true);
			}
			this.leaveList = leaveList;
		}
	}

	public Duration getLunchBreak() {
		return lunchBreak;
	}

	public void setLunchBreak(Duration lunchBreak) {
		this.lunchBreak = lunchBreak;
	}

	public Duration getNormalWorkPeriod1Balance() {
		return normalWorkPeriod1Balance;
	}

	public void setNormalWorkPeriod1Balance(Duration normalWorkPeriod1Balance) {
		this.normalWorkPeriod1Balance = normalWorkPeriod1Balance;
	}

	public Duration getNormalWorkPeriod2Balance() {
		return normalWorkPeriod2Balance;
	}

	public void setNormalWorkPeriod2Balance(Duration normalWorkPeriod2Balance) {
		this.normalWorkPeriod2Balance = normalWorkPeriod2Balance;
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
    
	
	
	
    public boolean isOdd(int number) {
    		if (number % 2 != 0) {
    			return true;
    		}
    		return false;
    }
    
    
}
