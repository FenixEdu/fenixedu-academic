package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.YearMonthDay;

public class WorkDaySheetToExportSearch implements Serializable {

    private YearMonthDay beginDate;

    private YearMonthDay endDate;

    private Integer costCenter;

    private String scheduleAcronym;

    private DomainReference<WorkScheduleType> workScheduleType;

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
	List<Assiduousness> result = new ArrayList<Assiduousness>();
	for (Assiduousness assiduousness : RootDomainObject.getInstance().getAssiduousnesss()) {
	    if (assiduousness.isStatusActive(beginDate, endDate) && satisfiedCostCenter(assiduousness)
		    && satisfiedScheduleAcronym(assiduousness)) {
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

}
