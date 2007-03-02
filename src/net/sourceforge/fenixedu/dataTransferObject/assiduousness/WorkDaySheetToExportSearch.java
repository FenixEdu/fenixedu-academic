package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class WorkDaySheetToExportSearch implements Serializable {

    private YearMonthDay beginDate;

    private YearMonthDay endDate;

    private Integer costCenter;

    private String scheduleAcronym;

    private DomainReference<WorkScheduleType> workScheduleType;

    private String justificationMotiveAcronym;

    private DomainReference<JustificationMotive> justificationMotive;

    public WorkDaySheetToExportSearch() {
	this.endDate = new YearMonthDay();
	this.beginDate = new YearMonthDay(endDate.getYear(), endDate.getMonthOfYear(), 01);
    }

    public WorkDaySheetToExportSearch(YearMonth yearMonth) {
	this.beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	int endDay = beginDate.dayOfMonth().getMaximumValue();
	if (yearMonth.getYear() == new YearMonthDay().getYear()
		&& yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
	    endDay = new YearMonthDay().getDayOfMonth();
	}
	this.endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, endDay);
    }

    public List<Assiduousness> getAssiduousnesses() {
	HashMap<Assiduousness, List<Leave>> levesMap = setLeavesMap();
	List<Assiduousness> result = new ArrayList<Assiduousness>();
	for (Assiduousness assiduousness : RootDomainObject.getInstance().getAssiduousnesss()) {
	    if (assiduousness.isStatusActive(beginDate, endDate) && satisfiedCostCenter(assiduousness)
		    && satisfiedScheduleAcronym(assiduousness) && levesMap.get(assiduousness) != null) {
		result.add(assiduousness);
	    }
	}
	return result;
    }

    private boolean satisfiedScheduleAcronym(Assiduousness assiduousness) {
	if (getScheduleAcronym() != null && getScheduleAcronym().length() != 0) {
	    HashMap<YearMonthDay, WorkSchedule> workSchedulesMap = assiduousness
		    .getWorkSchedulesBetweenDates(beginDate, endDate);
	    for (WorkSchedule workSchedule : workSchedulesMap.values()) {
		if (workSchedule != null
			&& workSchedule.getWorkScheduleType().getAcronym().equalsIgnoreCase(
				getScheduleAcronym())) {
		    return true;
		}
	    }
	    return false;
	}
	return true;
    }

    private boolean satisfiedCostCenter(Assiduousness assiduousness) {
	if (getCostCenter() != null) {
	    Unit unit = assiduousness.getEmployee().getLastWorkingPlace(beginDate, endDate);
	    if (unit != null && unit.getCostCenterCode().equals(getCostCenter())) {
		return true;
	    }
	    return false;
	}
	return true;
    }

    public HashMap<Assiduousness, List<Leave>> setLeavesMap() {
	HashMap<Assiduousness, List<Leave>> levesMap = new HashMap<Assiduousness, List<Leave>>();
	if (getJustificationMotiveAcronym() != null && getJustificationMotiveAcronym().length() != 0) {
	    Interval interval = new Interval(beginDate.toDateTimeAtMidnight(),
		    Assiduousness.defaultEndWorkDay.toDateTime(endDate.toDateMidnight()));
	    for (AssiduousnessRecord assiduousnessRecord : RootDomainObject.getInstance()
		    .getAssiduousnessRecords()) {
		if (assiduousnessRecord.isLeave()
			&& !assiduousnessRecord.isAnulated()
			&& ((Leave) assiduousnessRecord).getJustificationMotive().getAcronym().equals(
				getJustificationMotiveAcronym())) {
		    Interval leaveInterval = new Interval(assiduousnessRecord.getDate(),
			    ((Leave) assiduousnessRecord).getEndDate().plusSeconds(1));
		    if (leaveInterval.overlaps(interval)) {
			List<Leave> leavesList = levesMap.get(assiduousnessRecord.getAssiduousness());
			if (leavesList == null) {
			    leavesList = new ArrayList<Leave>();
			}
			leavesList.add((Leave) assiduousnessRecord);
			levesMap.put(assiduousnessRecord.getAssiduousness(), leavesList);
		    }
		}
	    }
	}
	return levesMap;
    }

    public YearMonthDay getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(YearMonthDay beginDate) {
	this.beginDate = beginDate;
    }

    public Integer getCostCenter() {
	return costCenter;
    }

    public void setCostCenter(Integer costCenter) {
	this.costCenter = costCenter;
    }

    public YearMonthDay getEndDate() {
	return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
	this.endDate = endDate;
    }

    public String getScheduleAcronym() {
	return scheduleAcronym;
    }

    public void setScheduleAcronym(String scheduleAcronym) {
	this.scheduleAcronym = scheduleAcronym;
    }

    public WorkScheduleType getWorkScheduleType() {
	return workScheduleType == null ? null : workScheduleType.getObject();
    }

    public void setWorkScheduleType(WorkScheduleType workScheduleType) {
	if (workScheduleType != null) {
	    this.workScheduleType = new DomainReference<WorkScheduleType>(workScheduleType);
	} else {
	    this.workScheduleType = null;
	}
    }

    public JustificationMotive getJustificationMotive() {
	return justificationMotive == null ? null : justificationMotive.getObject();
    }

    public void setJustificationMotive(JustificationMotive justificationMotive) {
	if (justificationMotive != null) {
	    this.justificationMotive = new DomainReference<JustificationMotive>(justificationMotive);
	} else {
	    this.justificationMotive = null;
	}
    }

    public String getJustificationMotiveAcronym() {
	return justificationMotiveAcronym;
    }

    public void setJustificationMotiveAcronym(String justificationMotiveAcronym) {
	this.justificationMotiveAcronym = justificationMotiveAcronym;
    }

}
