package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author Tânia Pousão
 */
public class EmployeeHistoric extends DomainObject implements IEmployeeHistoric {

    private Integer keyResponsableEmployee = null;

    private IEmployee responsableEmployee = null;

    private Integer keyWorkingPlaceCostCenter = null;

    private Integer keyMailingCostCenter = null;

    private Integer keySalaryCostCenter = null;

    private ICostCenter workingPlaceCostCenter = null;

    private ICostCenter mailingCostCenter = null;

    private ICostCenter salaryCostCenter = null;

    private String calendar = null;

    private Integer keyStatus = null;

    private StatusAssiduidade status = null;

    private Date beginDate = null;

    private Date endDate = null;

    private Integer who = null;

    private Timestamp when = null;

    public Integer keyEmployee = null;

    public IEmployee employee = null;

    public EmployeeHistoric() {
    }

    public EmployeeHistoric(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public EmployeeHistoric(IEmployee employee, ICostCenter workingPlaceCostCenter,
            ICostCenter mailingCostCenter, ICostCenter salaryCostCenter, String calendar,
            StatusAssiduidade statusAssiduidade, Date beginDate, Date endDate, Integer who,
            Timestamp when) {
        setResponsableEmployee(employee);
        setWorkingPlaceCostCenter(workingPlaceCostCenter);
        setMailingCostCenter(mailingCostCenter);
        setSalaryCostCenter(salaryCostCenter);
        setCalendar(calendar);
        setStatus(statusAssiduidade);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setWho(who);
        setWhen(when);
    }

    public Integer getKeyResponsableEmployee() {
        return keyResponsableEmployee;
    }

    public void setKeyResponsableEmployee(Integer keyResponsableEmployee) {
        this.keyResponsableEmployee = keyResponsableEmployee;
    }

    public IEmployee getResponsableEmployee() {
        return responsableEmployee;
    }

    public void setResponsableEmployee(IEmployee responsableEmployee) {
        this.responsableEmployee = responsableEmployee;
    }

    public Integer getKeyWorkingPlaceCostCenter() {
        return keyWorkingPlaceCostCenter;
    }

    public void setKeyWorkingPlaceCostCenter(Integer keyWorkingPlaceCostCenter) {
        this.keyWorkingPlaceCostCenter = keyWorkingPlaceCostCenter;
    }

    public Integer getKeyMailingCostCenter() {
        return keyMailingCostCenter;
    }

    public void setKeyMailingCostCenter(Integer keyMailingCostCenter) {
        this.keyMailingCostCenter = keyMailingCostCenter;
    }

    public Integer getKeySalaryCostCenter() {
        return keySalaryCostCenter;
    }

    public void setKeySalaryCostCenter(Integer keySalaryCostCenter) {
        this.keySalaryCostCenter = keySalaryCostCenter;
    }

    public ICostCenter getWorkingPlaceCostCenter() {
        return workingPlaceCostCenter;
    }

    public void setWorkingPlaceCostCenter(ICostCenter workingPlaceCostCenter) {
        this.workingPlaceCostCenter = workingPlaceCostCenter;
    }

    public ICostCenter getMailingCostCenter() {
        return mailingCostCenter;
    }

    public void setMailingCostCenter(ICostCenter mailingCostCenter) {
        this.mailingCostCenter = mailingCostCenter;
    }

    public ICostCenter getSalaryCostCenter() {
        return salaryCostCenter;
    }

    public void setSalaryCostCenter(ICostCenter salaryCostCenter) {
        this.salaryCostCenter = salaryCostCenter;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public Integer getKeyStatus() {
        return keyStatus;
    }

    public void setKeyStatus(Integer keyStatus) {
        this.keyStatus = keyStatus;
    }

    public StatusAssiduidade getStatus() {
        return status;
    }

    public void setStatus(StatusAssiduidade status) {
        this.status = status;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Timestamp getWhen() {
        return when;
    }

    public void setWhen(Timestamp when) {
        this.when = when;
    }

    public Integer getWho() {
        return who;
    }

    public void setWho(Integer who) {
        this.who = who;
    }

    public IEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

    public Integer getKeyEmployee() {
        return keyEmployee;
    }

    public void setKeyEmployee(Integer employeeKey) {
        this.keyEmployee = employeeKey;
    }

    public String toString() {
        String result = "[Dominio.EmployeeHistoric ";
        result += "Responsable Employee =" + getKeyResponsableEmployee();
        result += ", Working Place =" + getKeyWorkingPlaceCostCenter();
        result += ", Mailing =" + getKeyMailingCostCenter();
        result += ", Salary =" + getKeySalaryCostCenter();
        result += ", Calendar =" + getCalendar();
        result += ", Status =" + getKeyStatus();
        result += ", Begin Date =" + getBeginDate();
        result += ", End Date =" + getEndDate();
        result += "]";
        return result;
    }
}