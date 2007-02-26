package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.CorrectionType;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.MissingClocking;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class RegularizationMonthFactory implements Serializable, FactoryExecutor {

    private YearMonth yearMonth;

    private SortedMap<YearMonthDay, RegularizationDayBean> regularizationMap = new TreeMap<YearMonthDay, RegularizationDayBean>();

    private DomainReference<JustificationMotive> justificationMotive;

    private DomainReference<Assiduousness> assiduousness;

    private DomainReference<Employee> modifiedBy;
    
    private CorrectionType correctionType;

    public RegularizationMonthFactory(YearMonth yearMonth, Assiduousness assiduousness,
            Employee modifiedBy) {
        setAssiduousness(assiduousness);
        setModifiedBy(modifiedBy);
        YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
                yearMonth.getMonth().ordinal() + 1, 01);
        int endDay = beginDate.dayOfMonth().getMaximumValue();
        YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
                endDay);
        YearMonthDay lowerBeginDate = beginDate.minusDays(8);
        HashMap<YearMonthDay, WorkSchedule> workScheduleMap = assiduousness
                .getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
        DateTime init = getInit(lowerBeginDate, workScheduleMap);
        DateTime end = getEnd(endDate, workScheduleMap);
        HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = assiduousness.getClockingsMap(
                workScheduleMap, init, end);
        setRegularizations(clockingsMap, beginDate, endDate);
        setCorrectionType(CorrectionType.REGULARIZATION);
    }

    public void setRegularizations(HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap,
            YearMonthDay beginDate, YearMonthDay endDate) {
        for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            regularizationMap
                    .put(thisDay, new RegularizationDayBean(thisDay, clockingsMap.get(thisDay)));
        }
    }

    public Object execute() {
        for (RegularizationDayBean regularizationDayBean : getRegularizationMap().values()) {
            for (TimeOfDay timeOfDay : regularizationDayBean.getTimeClockingsToFill()) {
                if (timeOfDay != null) {
                    DateTime missingClockingDateTime = regularizationDayBean.getDate().toDateTime(
                            timeOfDay);
                    new MissingClocking(getAssiduousness(), getJustificationMotive(),
                            missingClockingDateTime, getModifiedBy());
                }
            }
        }
        return null;
    }

    private List<DateTime> getClockingsDateTime(RegularizationDayBean regularizationDayBean) {
        List<DateTime> dateTimeList = new ArrayList<DateTime>();
        for (TimeOfDay timeOfDay : regularizationDayBean.getTimeClockingsToFill()) {
            dateTimeList.add(regularizationDayBean.getDate().toDateTime(timeOfDay));
        }
        return dateTimeList;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    private DateTime getEnd(YearMonthDay endDate, HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
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

    private DateTime getInit(YearMonthDay lowerBeginDate,
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
        DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
        WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
        if (beginWorkSchedule != null) {
            init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
        }
        return init;
    }

    public SortedMap<YearMonthDay, RegularizationDayBean> getRegularizationMap() {
        return regularizationMap;
    }

    public Collection<RegularizationDayBean> getRegularizationList() {
        return getRegularizationMap().values();
    }
    
    public Assiduousness getAssiduousness() {
        return assiduousness == null ? null : assiduousness.getObject();
    }

    public void setAssiduousness(Assiduousness assiduousness) {
        if (assiduousness != null) {
            this.assiduousness = new DomainReference<Assiduousness>(assiduousness);
        }
    }

    public Employee getModifiedBy() {
        return modifiedBy == null ? null : modifiedBy.getObject();
    }

    public void setModifiedBy(Employee modifiedBy) {
        if (modifiedBy != null) {
            this.modifiedBy = new DomainReference<Employee>(modifiedBy);
        }
    }

    public JustificationMotive getJustificationMotive() {
        return justificationMotive == null ? null : justificationMotive.getObject();
    }

    public void setJustificationMotive(JustificationMotive justificationMotive) {
        if (justificationMotive != null) {
            this.justificationMotive = new DomainReference<JustificationMotive>(justificationMotive);
        }
    }

    public CorrectionType getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(CorrectionType correctionType) {
        this.correctionType = correctionType;
    }
}
