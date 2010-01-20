package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.CorrectionType;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.MissingClocking;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class RegularizationMonthFactory implements Serializable, FactoryExecutor {

    private YearMonth yearMonth;

    private SortedMap<LocalDate, RegularizationDayBean> regularizationMap = new TreeMap<LocalDate, RegularizationDayBean>();

    private JustificationMotive justificationMotive;

    private Assiduousness assiduousness;

    private Employee modifiedBy;

    private CorrectionType correctionType;

    public RegularizationMonthFactory(YearMonth yearMonth, Assiduousness assiduousness, Employee modifiedBy) {
	setAssiduousness(assiduousness);
	setModifiedBy(modifiedBy);
	LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	int endDay = beginDate.dayOfMonth().getMaximumValue();
	LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, endDay);
	setYearMonth(yearMonth);
	LocalDate lowerBeginDate = beginDate.minusDays(8);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = assiduousness.getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = assiduousness.getClockingsMap(workScheduleMap, init, end);
	setRegularizations(clockingsMap, beginDate, endDate);
	setCorrectionType(CorrectionType.REGULARIZATION);
    }

    public void setRegularizations(HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap, LocalDate beginDate,
	    LocalDate endDate) {
	for (LocalDate thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
	    regularizationMap.put(thisDay, new RegularizationDayBean(thisDay, clockingsMap.get(thisDay)));
	}
    }

    public Object execute() {
	for (RegularizationDayBean regularizationDayBean : getRegularizationMap().values()) {
	    for (LocalTime timeOfDay : regularizationDayBean.getTimeClockingsToFill()) {
		if (timeOfDay != null) {
		    DateTime missingClockingDateTime = regularizationDayBean.getDate().toDateTime(timeOfDay);
		    new MissingClocking(getAssiduousness(), missingClockingDateTime, getJustificationMotive(), getModifiedBy());
		}
	    }
	}
	return null;
    }

    public YearMonth getYearMonth() {
	return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
	this.yearMonth = yearMonth;
    }

    private DateTime getEnd(LocalDate endDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime end = endDate.toDateTime(Assiduousness.defaultEndWorkDay);
	WorkSchedule endWorkSchedule = workScheduleMap.get(endDate);
	if (endWorkSchedule != null) {
	    end = endDate.toDateTime(endWorkSchedule.getWorkScheduleType().getWorkTime()).plus(
		    endWorkSchedule.getWorkScheduleType().getWorkTimeDuration());
	    if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
		end = end.plusDays(2);
	    }
	}
	return end;
    }

    private DateTime getInit(LocalDate lowerBeginDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
	WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
	if (beginWorkSchedule != null) {
	    init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
	}
	return init;
    }

    public SortedMap<LocalDate, RegularizationDayBean> getRegularizationMap() {
	return regularizationMap;
    }

    public Collection<RegularizationDayBean> getRegularizationList() {
	return getRegularizationMap().values();
    }

    public Assiduousness getAssiduousness() {
	return assiduousness;
    }

    public void setAssiduousness(Assiduousness assiduousness) {
	if (assiduousness != null) {
	    this.assiduousness = assiduousness;
	}
    }

    public Employee getModifiedBy() {
	return modifiedBy;
    }

    public void setModifiedBy(Employee modifiedBy) {
	if (modifiedBy != null) {
	    this.modifiedBy = modifiedBy;
	}
    }

    public JustificationMotive getJustificationMotive() {
	return justificationMotive;
    }

    public void setJustificationMotive(JustificationMotive justificationMotive) {
	if (justificationMotive != null) {
	    this.justificationMotive = justificationMotive;
	}
    }

    public CorrectionType getCorrectionType() {
	return correctionType;
    }

    public void setCorrectionType(CorrectionType correctionType) {
	this.correctionType = correctionType;
    }
}
