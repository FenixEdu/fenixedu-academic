package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatus;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.MissingClocking;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class AssiduousnessExportChoices implements Serializable {

    public enum AssiduousnessExportChoicesDatesType {
	MONTHS, DATES;
    }

    private AssiduousnessExportChoicesDatesType assiduousnessExportChoicesDatesType;

    private Boolean canChooseDateType;

    private Boolean chooseYear;

    private YearMonth yearMonth;

    private YearMonthDay beginDate;

    private YearMonthDay endDate;

    private Integer costCenter;

    private String scheduleAcronym;

    private DomainReference<WorkScheduleType> workScheduleType;

    private String justificationMotiveAcronym;

    private DomainReference<JustificationMotive> justificationMotive;

    private DomainReference<AssiduousnessStatus> assiduousnessStatus;

    private String assiduousnessStatusDescription;

    private String action;

    public AssiduousnessExportChoices(String action) {
	this.action = action;
	this.endDate = new YearMonthDay();
	this.beginDate = new YearMonthDay(endDate.getYear(), endDate.getMonthOfYear(), 01);
	this.yearMonth = new YearMonth(endDate);
	setCanChooseDateType(false);
	setChooseYear(false);
    }

    public List<Assiduousness> getAssiduousnesses() {
	setYearMonth();
	HashMap<Assiduousness, List<Justification>> justificationsMap = setJustificationMap();
	List<Assiduousness> result = new ArrayList<Assiduousness>();
	for (Assiduousness assiduousness : RootDomainObject.getInstance().getAssiduousnesss()) {
	    if (assiduousness.isStatusActive(beginDate, endDate) && satisfiedCostCenter(assiduousness)
		    && satisfiedScheduleAcronym(assiduousness) && satisfiedJustification(justificationsMap, assiduousness)
		    && satisfiedStatus(assiduousness)) {
		result.add(assiduousness);
	    }
	}
	return result;
    }

    public boolean satisfiedAll(Assiduousness assiduousness) {
	setYearMonth();
	return (assiduousness.isStatusActive(beginDate, endDate) && satisfiedCostCenter(assiduousness)
		&& satisfiedScheduleAcronym(assiduousness) && satisfiedStatus(assiduousness));

    }

    private boolean satisfiedJustification(HashMap<Assiduousness, List<Justification>> justificationsMap,
	    Assiduousness assiduousness) {
	if (!StringUtils.isEmpty(getJustificationMotiveAcronym())) {
	    if (justificationsMap.values().isEmpty()) {
		return false;
	    }
	    return justificationsMap.get(assiduousness) != null;
	}
	return true;
    }

    private boolean satisfiedStatus(Assiduousness assiduousness) {
	if (!StringUtils.isEmpty(getAssiduousnessStatusDescription())) {
	    List<AssiduousnessStatusHistory> assiduousnessStatusList = assiduousness.getStatusBetween(getBeginDate(),
		    getEndDate());
	    for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusList) {
		if ((getAssiduousnessStatus() != null && assiduousnessStatusHistory.getAssiduousnessStatus() == getAssiduousnessStatus())
			|| getAssiduousnessStatusDescription().equalsIgnoreCase(
				assiduousnessStatusHistory.getAssiduousnessStatus().getDescription())) {
		    return true;
		}
	    }
	    return false;
	}
	return true;
    }

    private boolean satisfiedScheduleAcronym(Assiduousness assiduousness) {
	if (getScheduleAcronym() != null && getScheduleAcronym().length() != 0) {
	    HashMap<YearMonthDay, WorkSchedule> workSchedulesMap = assiduousness.getWorkSchedulesBetweenDates(beginDate, endDate);
	    for (WorkSchedule workSchedule : workSchedulesMap.values()) {
		if (workSchedule != null
			&& workSchedule.getWorkScheduleType().getAcronym().equalsIgnoreCase(getScheduleAcronym())) {
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

    public HashMap<Assiduousness, List<Justification>> setJustificationMap() {
	HashMap<Assiduousness, List<Justification>> justificationsMap = new HashMap<Assiduousness, List<Justification>>();
	if (!StringUtils.isEmpty(getJustificationMotiveAcronym())) {
	    Interval interval = new Interval(beginDate.toDateTimeAtMidnight(), Assiduousness.defaultEndWorkDay.toDateTime(endDate
		    .toDateMidnight()));
	    for (AssiduousnessRecord assiduousnessRecord : RootDomainObject.getInstance().getAssiduousnessRecords()) {
		if (assiduousnessRecord.isLeave()
			&& !assiduousnessRecord.isAnulated()
			&& ((Leave) assiduousnessRecord).getJustificationMotive().getAcronym().equals(
				getJustificationMotiveAcronym())) {
		    Interval leaveInterval = new Interval(assiduousnessRecord.getDate(), ((Leave) assiduousnessRecord)
			    .getEndDate().plusSeconds(1));
		    if (leaveInterval.overlaps(interval)) {
			List<Justification> justificationsList = justificationsMap.get(assiduousnessRecord.getAssiduousness());
			if (justificationsList == null) {
			    justificationsList = new ArrayList<Justification>();
			}
			justificationsList.add((Justification) assiduousnessRecord);
			justificationsMap.put(assiduousnessRecord.getAssiduousness(), justificationsList);
		    }
		} else if (assiduousnessRecord.isMissingClocking()
			&& !assiduousnessRecord.isAnulated()
			&& ((MissingClocking) assiduousnessRecord).getJustificationMotive().getAcronym().equals(
				getJustificationMotiveAcronym()) && interval.contains(assiduousnessRecord.getDate())) {
		    List<Justification> justificationsList = justificationsMap.get(assiduousnessRecord.getAssiduousness());
		    if (justificationsList == null) {
			justificationsList = new ArrayList<Justification>();
		    }
		    justificationsList.add((Justification) assiduousnessRecord);
		    justificationsMap.put(assiduousnessRecord.getAssiduousness(), justificationsList);
		}
	    }
	}
	return justificationsMap;
    }

    public HashMap<Assiduousness, List<Justification>> getAllJustificationMap() {
	HashMap<Assiduousness, List<Justification>> justificationsMap = new HashMap<Assiduousness, List<Justification>>();
	Interval interval = new Interval(beginDate.toDateTimeAtMidnight(), Assiduousness.defaultEndWorkDay.toDateTime(endDate
		.toDateMidnight()));
	for (AssiduousnessRecord assiduousnessRecord : RootDomainObject.getInstance().getAssiduousnessRecords()) {
	    if (assiduousnessRecord.isLeave()
		    && !assiduousnessRecord.isAnulated()
		    && (StringUtils.isEmpty(getJustificationMotiveAcronym()) || ((Leave) assiduousnessRecord)
			    .getJustificationMotive().getAcronym().equals(getJustificationMotiveAcronym()))) {
		Interval leaveInterval = new Interval(assiduousnessRecord.getDate(), ((Leave) assiduousnessRecord).getEndDate()
			.plusSeconds(1));
		if (leaveInterval.overlaps(interval)) {
		    List<Justification> justificationsList = justificationsMap.get(assiduousnessRecord.getAssiduousness());
		    if (justificationsList == null) {
			justificationsList = new ArrayList<Justification>();
		    }
		    justificationsList.add((Justification) assiduousnessRecord);
		    justificationsMap.put(assiduousnessRecord.getAssiduousness(), justificationsList);
		}
	    } else if (assiduousnessRecord.isMissingClocking()
		    && !assiduousnessRecord.isAnulated()
		    && (StringUtils.isEmpty(getJustificationMotiveAcronym()) || ((MissingClocking) assiduousnessRecord)
			    .getJustificationMotive().getAcronym().equals(getJustificationMotiveAcronym()))
		    && interval.contains(assiduousnessRecord.getDate())) {
		List<Justification> justificationsList = justificationsMap.get(assiduousnessRecord.getAssiduousness());
		if (justificationsList == null) {
		    justificationsList = new ArrayList<Justification>();
		}
		justificationsList.add((Justification) assiduousnessRecord);
		justificationsMap.put(assiduousnessRecord.getAssiduousness(), justificationsList);
	    }
	}
	return justificationsMap;
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

    public AssiduousnessStatus getAssiduousnessStatus() {
	return assiduousnessStatus == null ? null : assiduousnessStatus.getObject();
    }

    public void setAssiduousnessStatus(AssiduousnessStatus assiduousnessStatus) {
	if (assiduousnessStatus != null) {
	    this.assiduousnessStatus = new DomainReference<AssiduousnessStatus>(assiduousnessStatus);
	} else {
	    this.assiduousnessStatus = null;
	}
    }

    public String getAssiduousnessStatusDescription() {
	return assiduousnessStatusDescription;
    }

    public void setAssiduousnessStatusDescription(String statusDescription) {
	this.assiduousnessStatusDescription = statusDescription;
    }

    public YearMonth getYearMonth() {
	return yearMonth;
    }

    public void setYearMonth() {
	if (assiduousnessExportChoicesDatesType.equals(AssiduousnessExportChoicesDatesType.MONTHS)) {
	    if (getChooseYear()) {
		this.beginDate = new YearMonthDay(yearMonth.getYear(), 1, 1);
		this.endDate = new YearMonthDay(yearMonth.getYear(), 12, 31);
	    } else {
		this.beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
		int endDay = beginDate.dayOfMonth().getMaximumValue();
		if (yearMonth.getYear() == new YearMonthDay().getYear()
			&& yearMonth.getMonth().getNumberOfMonth() == new YearMonthDay().getMonthOfYear()) {
		    endDay = new YearMonthDay().getDayOfMonth();
		}
		this.endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().getNumberOfMonth(), endDay);
	    }
	} else {
	    yearMonth = new YearMonth(beginDate);
	}

    }

    public AssiduousnessExportChoicesDatesType getAssiduousnessExportChoicesDatesType() {
	return assiduousnessExportChoicesDatesType;
    }

    public void setAssiduousnessExportChoicesDatesType(AssiduousnessExportChoicesDatesType assiduousnessExportChoicesDatesType) {
	this.assiduousnessExportChoicesDatesType = assiduousnessExportChoicesDatesType;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public Boolean getCanChooseDateType() {
	return canChooseDateType;
    }

    public void setCanChooseDateType(Boolean canChooseDateType) {
	this.canChooseDateType = canChooseDateType;
	if (!canChooseDateType) {
	    setAssiduousnessExportChoicesDatesType(AssiduousnessExportChoicesDatesType.MONTHS);
	}
    }

    public Boolean getChooseYear() {
	return chooseYear;
    }

    public void setChooseYear(Boolean chooseYear) {
	this.chooseYear = chooseYear;
    }

}
