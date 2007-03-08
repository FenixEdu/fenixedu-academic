package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class EmployeeScheduleFactory implements Serializable, FactoryExecutor {

    DomainReference<WorkScheduleType> choosenWorkSchedule;

    DomainReference<Employee> employee;

    DomainReference<Employee> modifiedBy;

    DomainReference<Schedule> schedule;

    YearMonthDay beginDate;

    YearMonthDay endDate;

    boolean differencesInWorkSchedules;

    boolean differencesInDates;

    boolean toDeleteDays;

    List<EmployeeWorkWeekScheduleBean> employeeWorkWeekScheduleList = new ArrayList<EmployeeWorkWeekScheduleBean>();

    public EmployeeScheduleFactory(Schedule schedule, Employee modifiedBy) {
        setModifiedBy(modifiedBy);
        setEmployee(schedule.getAssiduousness().getEmployee());
        setSchedule(schedule);
        setEmployeeWorkWeekScheduleList(schedule, this);
        setBeginDate(schedule.getBeginDate());
        setEndDate(schedule.getEndDate());
    }

    public EmployeeScheduleFactory(Employee employee, Employee modifiedBy, Schedule schedule) {
        setModifiedBy(modifiedBy);
        setEmployee(employee);
        if (schedule != null) {
            setSchedule(schedule);
            setEmployeeWorkWeekScheduleList(schedule, this);
            setBeginDate(schedule.getBeginDate());
            setEndDate(schedule.getEndDate());
        }
    }

    public Object execute() {
        Schedule schedule = getSchedule();
        removeAllEmptyWorkWeekSchedules();
        if (isToDeleteDays()) {
            schedule = schedule.deleteDays(this);
        } else {
            if (schedule == null) {
                schedule = new Schedule(this);
            } else {
                schedule = schedule.edit(this);
            }
        }
        return schedule;
    }

    public void setEmployee(Employee employee) {
        if (employee != null) {
            this.employee = new DomainReference<Employee>(employee);
        } else {
            this.employee = null;
        }
    }

    public Employee getEmployee() {
        return employee == null ? null : employee.getObject();
    }

    public List<EmployeeWorkWeekScheduleBean> getEmployeeWorkWeekScheduleList() {
        return employeeWorkWeekScheduleList;
    }

    public void setEmployeeWorkWeekScheduleList(Schedule schedule,
            EmployeeScheduleFactory employeeScheduleFactory) {
        for (WorkSchedule workSchedule : schedule.getWorkSchedules()) {
            EmployeeWorkWeekScheduleBean employeeWorkWeekScheduleBean = getEmployeeWorkWeekScheduleByWeek(workSchedule
                    .getPeriodicity().getWorkWeekNumber());
            if (employeeWorkWeekScheduleBean != null) {
                employeeWorkWeekScheduleBean.edit(workSchedule);
            } else {
                this.employeeWorkWeekScheduleList.add(new EmployeeWorkWeekScheduleBean(workSchedule,
                        employeeScheduleFactory));
            }
        }
        Collections.sort(getEmployeeWorkWeekScheduleList(), new BeanComparator("workWeekNumber"));
    }

    private EmployeeWorkWeekScheduleBean getEmployeeWorkWeekScheduleByWeek(Integer workWeekNumber) {
        for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : getEmployeeWorkWeekScheduleList()) {
            if (workWeekScheduleBean.getWorkWeekNumber().equals(workWeekNumber)) {
                return workWeekScheduleBean;
            }
        }
        return null;
    }

    public void addEmployeeWorkWeekSchedule() {
        Integer maxWeek = 0;
        for (EmployeeWorkWeekScheduleBean employeeWorkWeekScheduleBean : getEmployeeWorkWeekScheduleList()) {
            if (employeeWorkWeekScheduleBean.getWorkWeekNumber() > maxWeek) {
                maxWeek = employeeWorkWeekScheduleBean.getWorkWeekNumber();
            }
        }
        getEmployeeWorkWeekScheduleList().add(new EmployeeWorkWeekScheduleBean(maxWeek + 1, this));
        Collections.sort(getEmployeeWorkWeekScheduleList(), new BeanComparator("workWeekNumber"));
    }

    public Employee getModifiedBy() {
        return modifiedBy == null ? null : modifiedBy.getObject();
    }

    public void setModifiedBy(Employee modifiedBy) {
        if (modifiedBy != null) {
            this.modifiedBy = new DomainReference<Employee>(modifiedBy);
        } else {
            this.modifiedBy = null;
        }
    }

    public YearMonthDay getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(YearMonthDay beginDate) {
        this.beginDate = beginDate;
    }

    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }

    public WorkScheduleType getChoosenWorkSchedule() {
        return choosenWorkSchedule == null ? null : choosenWorkSchedule.getObject();
    }

    public void setChoosenWorkSchedule(WorkScheduleType choosenWorkSchedule) {
        if (choosenWorkSchedule != null) {
            this.choosenWorkSchedule = new DomainReference<WorkScheduleType>(choosenWorkSchedule);
        } else {
            this.choosenWorkSchedule = null;
        }
    }

    public boolean equalDates(Schedule schedule) {
        if (getBeginDate().isEqual(schedule.getBeginDate())
                && ((getEndDate() == null && schedule.getEndDate() == null) || (getEndDate() != null
                        && schedule.getEndDate() != null && getEndDate().isEqual(schedule.getEndDate())))) {
            return true;
        }
        return false;
    }

    public boolean isDifferencesInWorkSchedules() {
        return differencesInWorkSchedules;
    }

    public void setDifferencesInWorkSchedules(boolean differencesInWorkSchedules) {
        this.differencesInWorkSchedules = differencesInWorkSchedules;
    }

    public boolean isDifferencesInDates() {
        return differencesInDates;
    }

    public void setDifferencesInDates(boolean differencesInDates) {
        this.differencesInDates = differencesInDates;
    }

    public void removeEmployeeWorkWeekSchedule() {
        int subtract = 0;
        EmployeeWorkWeekScheduleBean workWeekScheduleBeanToRemove = null;
        for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : getEmployeeWorkWeekScheduleList()) {
            if (workWeekScheduleBean.getIsEmptyWeek() && workWeekScheduleBeanToRemove == null) {
                workWeekScheduleBeanToRemove = workWeekScheduleBean;
                subtract = 1;
            }
            workWeekScheduleBean.setWorkWeekNumber(workWeekScheduleBean.getWorkWeekNumber() - subtract);
        }
        getEmployeeWorkWeekScheduleList().remove(workWeekScheduleBeanToRemove);
    }

    public void removeAllEmptyWorkWeekSchedules() {
        int subtract = 0;
        List<EmployeeWorkWeekScheduleBean> workWeekScheduleBeansToRemove = new ArrayList<EmployeeWorkWeekScheduleBean>();
        for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : getEmployeeWorkWeekScheduleList()) {
            if (!workWeekScheduleBean.isAnyDayChecked() && workWeekScheduleBean.getIsEmptyWeek()) {
                workWeekScheduleBeansToRemove.add(workWeekScheduleBean);
                subtract++;
            } else {
                workWeekScheduleBean.setWorkWeekNumber(workWeekScheduleBean.getWorkWeekNumber()
                        - subtract);
            }
        }
        getEmployeeWorkWeekScheduleList().removeAll(workWeekScheduleBeansToRemove);
    }

    public void selectAllCheckBoxes(Integer workWeek) {
        for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : getEmployeeWorkWeekScheduleList()) {
            if (workWeekScheduleBean.getWorkWeekNumber().equals(workWeek)) {
                workWeekScheduleBean.checkAllWeek();
                break;
            }
        }
    }

    public boolean isToDeleteDays() {
        return toDeleteDays;
    }

    public void setToDeleteDays(boolean toDeleteDays) {
        this.toDeleteDays = toDeleteDays;
    }

    public Schedule getSchedule() {
        return schedule == null ? null : schedule.getObject();
    }

    public void setSchedule(Schedule schedule) {
        if (schedule != null) {
            this.schedule = new DomainReference<Schedule>(schedule);
        }
    }
}
