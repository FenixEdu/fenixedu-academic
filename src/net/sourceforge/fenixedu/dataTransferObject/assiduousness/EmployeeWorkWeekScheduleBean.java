package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.assiduousness.Periodicity;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.Chronology;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EmployeeWorkWeekScheduleBean implements Serializable {

    Boolean chooseMonday;

    Boolean chooseTuesday;

    Boolean chooseWednesday;

    Boolean chooseThursday;

    Boolean chooseFriday;

    DomainReference<WorkScheduleType> mondaySchedule;

    DomainReference<WorkScheduleType> tuesdaySchedule;

    DomainReference<WorkScheduleType> wednesdaySchedule;

    DomainReference<WorkScheduleType> thursdaySchedule;

    DomainReference<WorkScheduleType> fridaySchedule;

    Integer workWeekNumber;

    EmployeeScheduleFactory employeeScheduleFactory;

    public EmployeeWorkWeekScheduleBean(WorkSchedule workSchedule,
            EmployeeScheduleFactory employeeScheduleFactory) {
        setWorkWeekNumber(workSchedule.getPeriodicity().getWorkWeekNumber());
        setEmployeeScheduleFactory(employeeScheduleFactory);
        for (WeekDay weekDay : workSchedule.getWorkWeek().getDays()) {
            setWorkScheduleType(workSchedule.getWorkScheduleType(), weekDay);
        }
    }

    public EmployeeWorkWeekScheduleBean(Integer weekNumber,
            EmployeeScheduleFactory employeeScheduleFactory) {
        setEmployeeScheduleFactory(employeeScheduleFactory);
        setWorkWeekNumber(weekNumber);
    }

    public void edit(WorkSchedule workSchedule) {
        for (WeekDay weekDay : workSchedule.getWorkWeek().getDays()) {
            setWorkScheduleType(workSchedule.getWorkScheduleType(), weekDay);
        }
    }

    public boolean getIsEmptyWeek() {
        return getMondaySchedule() == null && getTuesdaySchedule() == null
                && getWednesdaySchedule() == null && getThursdaySchedule() == null
                && getFridaySchedule() == null;
    }

    public WorkWeek getWorkWeekByCheckedBox() {
        List<WeekDay> weekDays = new ArrayList<WeekDay>();
        if (getChooseMonday()) {
            weekDays.add(WeekDay.MONDAY);
        }
        if (getChooseTuesday()) {
            weekDays.add(WeekDay.TUESDAY);
        }
        if (getChooseWednesday()) {
            weekDays.add(WeekDay.WEDNESDAY);
        }
        if (getChooseThursday()) {
            weekDays.add(WeekDay.THURSDAY);
        }
        if (getChooseFriday()) {
            weekDays.add(WeekDay.FRIDAY);
        }
        if (weekDays.size() != 0) {
            WeekDay array[] = {};
            return new WorkWeek(weekDays.toArray(array));
        } else {
            return null;
        }
    }

    public List<WorkSchedule> getWorkSchedules(Periodicity periodicity) {
        List<WorkSchedule> workSchedules = new ArrayList<WorkSchedule>();
        List<WorkScheduleType> differentWorkScheduleTypes = new ArrayList<WorkScheduleType>();
        if (getMondaySchedule() != null || getChooseMonday()) {
            WorkScheduleType workScheduleType = getChooseMonday() ? getEmployeeScheduleFactory()
                    .getChoosenWorkSchedule() : getMondaySchedule();
            differentWorkScheduleTypes.add(workScheduleType);
            WorkWeek workWeek = getWorkWeek(workScheduleType);
            workSchedules.add(new WorkSchedule(workScheduleType, workWeek, periodicity));
        }
        if ((getTuesdaySchedule() != null || getChooseTuesday())
                && !differentWorkScheduleTypes
                        .contains(getChooseTuesday() ? getEmployeeScheduleFactory()
                                .getChoosenWorkSchedule() : getTuesdaySchedule())) {
            WorkScheduleType workScheduleType = getChooseTuesday() ? getEmployeeScheduleFactory()
                    .getChoosenWorkSchedule() : getTuesdaySchedule();
            differentWorkScheduleTypes.add(workScheduleType);
            WorkWeek workWeek = getWorkWeek(workScheduleType);
            workSchedules.add(new WorkSchedule(workScheduleType, workWeek, periodicity));
        }
        if ((getWednesdaySchedule() != null || getChooseWednesday())
                && !differentWorkScheduleTypes
                        .contains(getChooseWednesday() ? getEmployeeScheduleFactory()
                                .getChoosenWorkSchedule() : getWednesdaySchedule())) {
            WorkScheduleType workScheduleType = getChooseWednesday() ? getEmployeeScheduleFactory()
                    .getChoosenWorkSchedule() : getWednesdaySchedule();
            differentWorkScheduleTypes.add(workScheduleType);
            WorkWeek workWeek = getWorkWeek(workScheduleType);
            workSchedules.add(new WorkSchedule(workScheduleType, workWeek, periodicity));
        }
        if ((getThursdaySchedule() != null || getChooseThursday())
                && !differentWorkScheduleTypes
                        .contains(getChooseThursday() ? getEmployeeScheduleFactory()
                                .getChoosenWorkSchedule() : getThursdaySchedule())) {
            WorkScheduleType workScheduleType = getChooseThursday() ? getEmployeeScheduleFactory()
                    .getChoosenWorkSchedule() : getThursdaySchedule();
            differentWorkScheduleTypes.add(workScheduleType);
            WorkWeek workWeek = getWorkWeek(workScheduleType);
            workSchedules.add(new WorkSchedule(workScheduleType, workWeek, periodicity));
        }
        if ((getFridaySchedule() != null || getChooseFriday())
                && !differentWorkScheduleTypes.contains(getChooseFriday() ? getEmployeeScheduleFactory()
                        .getChoosenWorkSchedule() : getFridaySchedule())) {
            WorkScheduleType workScheduleType = getChooseFriday() ? getEmployeeScheduleFactory()
                    .getChoosenWorkSchedule() : getFridaySchedule();
            differentWorkScheduleTypes.add(workScheduleType);
            WorkWeek workWeek = getWorkWeek(workScheduleType);
            workSchedules.add(new WorkSchedule(workScheduleType, workWeek, periodicity));
        }
        return workSchedules;
    }

    private WorkWeek getWorkWeek(WorkScheduleType workScheduleType) {
        List<WeekDay> weekDays = new ArrayList<WeekDay>();
        if ((!getChooseMonday() && getMondaySchedule() != null && getMondaySchedule() == workScheduleType)
                || (getChooseMonday() && getEmployeeScheduleFactory().getChoosenWorkSchedule() == workScheduleType)) {
            weekDays.add(WeekDay.MONDAY);
        }
        if ((!getChooseTuesday() && getTuesdaySchedule() != null && getTuesdaySchedule() == workScheduleType)
                || (getChooseTuesday() && getEmployeeScheduleFactory().getChoosenWorkSchedule() == workScheduleType)) {
            weekDays.add(WeekDay.TUESDAY);
        }
        if ((!getChooseWednesday() && getWednesdaySchedule() != null && getWednesdaySchedule() == workScheduleType)
                || (getChooseWednesday() && getEmployeeScheduleFactory().getChoosenWorkSchedule() == workScheduleType)) {
            weekDays.add(WeekDay.WEDNESDAY);
        }
        if ((!getChooseThursday() && getThursdaySchedule() != null && getThursdaySchedule() == workScheduleType)
                || (getChooseThursday() && getEmployeeScheduleFactory().getChoosenWorkSchedule() == workScheduleType)) {
            weekDays.add(WeekDay.THURSDAY);
        }
        if ((!getChooseFriday() && getFridaySchedule() != null && getFridaySchedule() == workScheduleType)
                || (getChooseFriday() && getEmployeeScheduleFactory().getChoosenWorkSchedule() == workScheduleType)) {
            weekDays.add(WeekDay.FRIDAY);
        }
        if (weekDays.size() != 0) {
            WeekDay array[] = {};
            return new WorkWeek(weekDays.toArray(array));
        } else {
            return null;
        }
    }

    public List<WorkSchedule> getWorkSchedulesForNonDeletedDays(Periodicity periodicity) {
        List<WorkSchedule> workSchedules = new ArrayList<WorkSchedule>();
        List<WorkScheduleType> differentWorkScheduleTypes = new ArrayList<WorkScheduleType>();
        if (getMondaySchedule() != null && !getChooseMonday()) {
            differentWorkScheduleTypes.add(getMondaySchedule());
            WorkWeek workWeek = getWorkWeekForNonDeletedDay(getMondaySchedule());
            workSchedules.add(new WorkSchedule(getMondaySchedule(), workWeek, periodicity));
        }
        if (getTuesdaySchedule() != null && !getChooseTuesday()
                && !differentWorkScheduleTypes.contains(getTuesdaySchedule())) {
            differentWorkScheduleTypes.add(getTuesdaySchedule());
            WorkWeek workWeek = getWorkWeekForNonDeletedDay(getTuesdaySchedule());
            workSchedules.add(new WorkSchedule(getTuesdaySchedule(), workWeek, periodicity));
        }
        if (getWednesdaySchedule() != null && !getChooseWednesday()
                && !differentWorkScheduleTypes.contains(getWednesdaySchedule())) {
            differentWorkScheduleTypes.add(getWednesdaySchedule());
            WorkWeek workWeek = getWorkWeekForNonDeletedDay(getWednesdaySchedule());
            workSchedules.add(new WorkSchedule(getWednesdaySchedule(), workWeek, periodicity));
        }
        if (getThursdaySchedule() != null && !getChooseThursday()
                && !differentWorkScheduleTypes.contains(getThursdaySchedule())) {
            differentWorkScheduleTypes.add(getThursdaySchedule());
            WorkWeek workWeek = getWorkWeekForNonDeletedDay(getThursdaySchedule());
            workSchedules.add(new WorkSchedule(getThursdaySchedule(), workWeek, periodicity));
        }
        if (getFridaySchedule() != null && !getChooseFriday()
                && !differentWorkScheduleTypes.contains(getFridaySchedule())) {
            differentWorkScheduleTypes.add(getFridaySchedule());
            WorkWeek workWeek = getWorkWeekForNonDeletedDay(getFridaySchedule());
            workSchedules.add(new WorkSchedule(getFridaySchedule(), workWeek, periodicity));
        }
        return workSchedules;
    }

    private WorkWeek getWorkWeekForNonDeletedDay(WorkScheduleType workScheduleType) {
        List<WeekDay> weekDays = new ArrayList<WeekDay>();
        if (getMondaySchedule() != null && getMondaySchedule() == workScheduleType && !getChooseMonday()) {
            weekDays.add(WeekDay.MONDAY);
        }
        if (getTuesdaySchedule() != null && getTuesdaySchedule() == workScheduleType
                && !getChooseTuesday()) {
            weekDays.add(WeekDay.TUESDAY);
        }
        if (getWednesdaySchedule() != null && getWednesdaySchedule() == workScheduleType
                && !getChooseWednesday()) {
            weekDays.add(WeekDay.WEDNESDAY);
        }
        if (getThursdaySchedule() != null && getThursdaySchedule() == workScheduleType
                && !getChooseThursday()) {
            weekDays.add(WeekDay.THURSDAY);
        }
        if (getFridaySchedule() != null && getFridaySchedule() == workScheduleType && !getChooseFriday()) {
            weekDays.add(WeekDay.FRIDAY);
        }
        if (weekDays.size() != 0) {
            WeekDay array[] = {};
            return new WorkWeek(weekDays.toArray(array));
        } else {
            return null;
        }
    }

    private void setWorkScheduleType(WorkScheduleType workScheduleType, WeekDay weekDay) {
        switch (weekDay) {
        case MONDAY:
            setMondaySchedule(workScheduleType);
            break;
        case TUESDAY:
            setTuesdaySchedule(workScheduleType);
            break;
        case WEDNESDAY:
            setWendsaySchedule(workScheduleType);
            break;
        case THURSDAY:
            setThursdaySchedule(workScheduleType);
            break;
        case FRIDAY:
            setFridaySchedule(workScheduleType);
            break;
        default:
            throw new DomainException("error.workDay.cannotBe.weekEnd");
        }
    }

    public void checkAllWeek() {
        setChooseMonday(true);
        setChooseTuesday(true);
        setChooseWednesday(true);
        setChooseThursday(true);
        setChooseFriday(true);
    }

    public boolean isAnyDayChecked() {
        return getChooseMonday() || getChooseTuesday() || getChooseWednesday() || getChooseThursday()
                || getChooseFriday();
    }

    public boolean isValidWeekChecked() {
        if ((getMondaySchedule() != null && !getChooseMonday())
                || (getTuesdaySchedule() != null && !getChooseTuesday())
                || (getWednesdaySchedule() != null && !getChooseWednesday())
                || (getThursdaySchedule() != null && !getChooseThursday())
                || (getFridaySchedule() != null && !getChooseFriday())) {
            return false;
        }
        return true;
    }

    public boolean areSelectedDaysEmpty() {
        if ((getChooseMonday() && getMondaySchedule() != null)
                || (getChooseTuesday() && getTuesdaySchedule() != null)
                || (getChooseWednesday() && getWednesdaySchedule() != null)
                || (getChooseThursday() && getThursdaySchedule() != null)
                || (getChooseFriday() && getFridaySchedule() != null)) {
            return false;
        }
        return true;
    }

    public boolean getHasFixedPeriod() {
        if (getMondayFixedWorkPeriod().equals("") && getTuesdayFixedWorkPeriod().equals("")
                && getWednesdayFixedWorkPeriod().equals("") && getThursdayFixedWorkPeriod().equals("")
                && getFridayFixedWorkPeriod().equals("")) {
            return false;
        }
        return true;
    }

    public boolean getHasMealPeriod() {
        if (getMondayMealPeriod().equals("") && getMondayMandatoryMealPeriods().equals("")
                && getTuesdayMealPeriod().equals("") && getTuesdayMandatoryMealPeriods().equals("")
                && getWednesdayMealPeriod().equals("") && getWednesdayMandatoryMealPeriods().equals("")
                && getThursdayMealPeriod().equals("") && getThursdayMandatoryMealPeriods().equals("")
                && getFridayMealPeriod().equals("") && getFridayMandatoryMealPeriods().equals("")) {
            return false;
        }
        return true;
    }

    public String getMondayNormalWorkPeriod() {
        return getNormalWorkPeriod(getMondaySchedule());
    }

    public String getTuesdayNormalWorkPeriod() {
        return getNormalWorkPeriod(getTuesdaySchedule());
    }

    public String getWednesdayNormalWorkPeriod() {
        return getNormalWorkPeriod(getWednesdaySchedule());
    }

    public String getThursdayNormalWorkPeriod() {
        return getNormalWorkPeriod(getThursdaySchedule());
    }

    public String getFridayNormalWorkPeriod() {
        return getNormalWorkPeriod(getFridaySchedule());
    }

    public String getNormalWorkPeriod(WorkScheduleType workScheduleType) {
        if (workScheduleType == null || workScheduleType.getNormalWorkPeriod() == null) {
            return "";
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        String firstPeriod = fmt.print(workScheduleType.getNormalWorkPeriod().getFirstPeriod());
        String firstPeriodEnd = fmt.print(workScheduleType.getNormalWorkPeriod().getEndFirstPeriod());
        StringBuilder result = new StringBuilder();
        result.append(firstPeriod).append(" - ").append(firstPeriodEnd);
        if (workScheduleType.getNormalWorkPeriod().getSecondPeriod() != null) {
            String secondPeriod = fmt.print(workScheduleType.getNormalWorkPeriod().getSecondPeriod());
            String secondPeriodEnd = fmt.print(workScheduleType.getNormalWorkPeriod()
                    .getEndSecondPeriod());
            result.append("<br/>").append(secondPeriod).append(" - ").append(secondPeriodEnd);
        }
        return result.toString();
    }

    public String getMondayFixedWorkPeriod() {
        return getFixedWorkPeriod(getMondaySchedule());
    }

    public String getTuesdayFixedWorkPeriod() {
        return getFixedWorkPeriod(getTuesdaySchedule());
    }

    public String getWednesdayFixedWorkPeriod() {
        return getFixedWorkPeriod(getWednesdaySchedule());
    }

    public String getThursdayFixedWorkPeriod() {
        return getFixedWorkPeriod(getThursdaySchedule());
    }

    public String getFridayFixedWorkPeriod() {
        return getFixedWorkPeriod(getFridaySchedule());
    }

    public String getFixedWorkPeriod(WorkScheduleType workScheduleType) {
        if (workScheduleType == null || workScheduleType.getFixedWorkPeriod() == null) {
            return "";
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        String firstPeriod = fmt.print(workScheduleType.getFixedWorkPeriod().getFirstPeriod());
        String firstPeriodEnd = fmt.print(workScheduleType.getFixedWorkPeriod().getEndFirstPeriod());
        StringBuilder result = new StringBuilder();
        result.append(firstPeriod).append(" - ").append(firstPeriodEnd);
        if (workScheduleType.getFixedWorkPeriod().getSecondPeriod() != null) {
            String secondPeriod = fmt.print(workScheduleType.getFixedWorkPeriod().getSecondPeriod());
            String secondPeriodEnd = fmt.print(workScheduleType.getFixedWorkPeriod()
                    .getEndSecondPeriod());
            result.append("<br/>").append(secondPeriod).append(" - ").append(secondPeriodEnd);
        }
        return result.toString();
    }

    public String getMondayMealPeriod() {
        return getMealPeriod(getMondaySchedule());
    }

    public String getTuesdayMealPeriod() {
        return getMealPeriod(getTuesdaySchedule());
    }

    public String getWednesdayMealPeriod() {
        return getMealPeriod(getWednesdaySchedule());
    }

    public String getThursdayMealPeriod() {
        return getMealPeriod(getThursdaySchedule());
    }

    public String getFridayMealPeriod() {
        return getMealPeriod(getFridaySchedule());
    }

    public String getMealPeriod(WorkScheduleType workScheduleType) {
        if (workScheduleType == null || workScheduleType.getMeal() == null) {
            return "";
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        String beginMealBreak = fmt.print(workScheduleType.getMeal().getBeginMealBreak());
        String endMealBreak = fmt.print(workScheduleType.getMeal().getEndMealBreak());
        StringBuilder result = new StringBuilder();
        result.append(beginMealBreak).append(" - ").append(endMealBreak);
        return result.toString();
    }

    public String getMondayMandatoryMealPeriods() {
        return getMandatoryMealPeriods(getMondaySchedule());
    }

    public String getTuesdayMandatoryMealPeriods() {
        return getMandatoryMealPeriods(getTuesdaySchedule());
    }

    public String getWednesdayMandatoryMealPeriods() {
        return getMandatoryMealPeriods(getWednesdaySchedule());
    }

    public String getThursdayMandatoryMealPeriods() {
        return getMandatoryMealPeriods(getThursdaySchedule());
    }

    public String getFridayMandatoryMealPeriods() {
        return getMandatoryMealPeriods(getFridaySchedule());
    }

    public String getMandatoryMealPeriods(WorkScheduleType workScheduleType) {
        if (workScheduleType == null || workScheduleType.getMeal() == null) {
            return "";
        }
        Chronology chronology = GregorianChronology.getInstanceUTC();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        TimeOfDay time = new TimeOfDay(workScheduleType.getMeal().getMinimumMealBreakInterval()
                .getMillis(), chronology);
        String minimum = fmt.print(time);
        time = new TimeOfDay(workScheduleType.getMeal().getMandatoryMealDiscount().getMillis(),
                chronology);
        String mandatory = fmt.print(time);
        StringBuilder result = new StringBuilder();
        result.append("Mín.: ").append(minimum).append("<br/>Obr.: ").append(mandatory);
        return result.toString();
    }

    public Integer getWorkWeekNumber() {
        return workWeekNumber;
    }

    public void setWorkWeekNumber(Integer workWeekNumber) {
        this.workWeekNumber = workWeekNumber;
    }

    public Boolean getChooseFriday() {
        return chooseFriday;
    }

    public void setChooseFriday(Boolean chooseFriday) {
        this.chooseFriday = chooseFriday;
    }

    public Boolean getChooseMonday() {
        return chooseMonday;
    }

    public void setChooseMonday(Boolean chooseMonday) {
        this.chooseMonday = chooseMonday;
    }

    public Boolean getChooseThursday() {
        return chooseThursday;
    }

    public void setChooseThursday(Boolean chooseThursday) {
        this.chooseThursday = chooseThursday;
    }

    public Boolean getChooseTuesday() {
        return chooseTuesday;
    }

    public void setChooseTuesday(Boolean chooseTuesday) {
        this.chooseTuesday = chooseTuesday;
    }

    public Boolean getChooseWednesday() {
        return chooseWednesday;
    }

    public void setChooseWednesday(Boolean chooseWednesday) {
        this.chooseWednesday = chooseWednesday;
    }

    public WorkScheduleType getFridaySchedule() {
        return fridaySchedule == null ? null : fridaySchedule.getObject();
    }

    public void setFridaySchedule(WorkScheduleType fridaySchedule) {
        if (fridaySchedule != null) {
            this.fridaySchedule = new DomainReference<WorkScheduleType>(fridaySchedule);
        } else {
            this.fridaySchedule = null;
        }
    }

    public WorkScheduleType getMondaySchedule() {
        return mondaySchedule == null ? null : mondaySchedule.getObject();
    }

    public void setMondaySchedule(WorkScheduleType mondaySchedule) {
        if (mondaySchedule != null) {
            this.mondaySchedule = new DomainReference<WorkScheduleType>(mondaySchedule);
        } else {
            this.mondaySchedule = null;
        }
    }

    public WorkScheduleType getThursdaySchedule() {
        return thursdaySchedule == null ? null : thursdaySchedule.getObject();
    }

    public void setThursdaySchedule(WorkScheduleType thursdaySchedule) {
        if (thursdaySchedule != null) {
            this.thursdaySchedule = new DomainReference<WorkScheduleType>(thursdaySchedule);
        } else {
            this.thursdaySchedule = null;
        }
    }

    public WorkScheduleType getTuesdaySchedule() {
        return tuesdaySchedule == null ? null : tuesdaySchedule.getObject();
    }

    public void setTuesdaySchedule(WorkScheduleType tuesdaySchedule) {
        if (tuesdaySchedule != null) {
            this.tuesdaySchedule = new DomainReference<WorkScheduleType>(tuesdaySchedule);
        } else {
            this.tuesdaySchedule = null;
        }
    }

    public WorkScheduleType getWednesdaySchedule() {
        return wednesdaySchedule == null ? null : wednesdaySchedule.getObject();
    }

    public void setWendsaySchedule(WorkScheduleType wednesdaySchedule) {
        if (wednesdaySchedule != null) {
            this.wednesdaySchedule = new DomainReference<WorkScheduleType>(wednesdaySchedule);
        } else {
            this.wednesdaySchedule = null;
        }
    }

    public EmployeeScheduleFactory getEmployeeScheduleFactory() {
        return employeeScheduleFactory;
    }

    public void setEmployeeScheduleFactory(EmployeeScheduleFactory employeeScheduleFactory) {
        this.employeeScheduleFactory = employeeScheduleFactory;
    }
}
