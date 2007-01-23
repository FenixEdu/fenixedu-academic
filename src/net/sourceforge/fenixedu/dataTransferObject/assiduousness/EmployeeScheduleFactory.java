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

    YearMonthDay beginDate;

    YearMonthDay endDate;

    boolean differencesInWorkSchedules;

    boolean differencesInDates;

    boolean toDeleteDays;

    List<EmployeeWorkWeekScheduleBean> employeeWorkWeekScheduleList = new ArrayList<EmployeeWorkWeekScheduleBean>();

    public EmployeeScheduleFactory(Employee employee, Employee modifiedBy) {
        setEmployee(employee);
        setModifiedBy(modifiedBy);
        Schedule currentSchedule = employee.getAssiduousness().getCurrentSchedule();
        if (currentSchedule != null) {
            setEmployeeWorkWeekScheduleList(currentSchedule, this);
            setBeginDate(currentSchedule.getBeginDate());
            setEndDate(currentSchedule.getEndDate());
        }
    }

    public EmployeeScheduleFactory(Schedule currentSchedule) {
        setEmployee(currentSchedule.getAssiduousness().getEmployee());
        setModifiedBy(currentSchedule.getModifiedBy());
        setEmployeeWorkWeekScheduleList(currentSchedule, this);
        setBeginDate(currentSchedule.getBeginDate());
        setEndDate(currentSchedule.getEndDate());
    }

    public Object execute() {
        Schedule currentSchedule = getEmployee().getAssiduousness().getCurrentSchedule();
        if (isToDeleteDays()) {
            currentSchedule.deleteDays(this);
        } else {
            if (currentSchedule == null) {
                currentSchedule = new Schedule(this);
            } else {
                currentSchedule.edit(this);
            }
            currentSchedule = getEmployee().getAssiduousness().getCurrentSchedule();
        }
        return null;
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

    public void selectAllCheckBoxes(Integer workWeek) {
        for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : getEmployeeWorkWeekScheduleList()) {
            if(workWeekScheduleBean.getWorkWeekNumber().equals(workWeek)){
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
}
