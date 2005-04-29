package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author Tânia Pousão
 */
public class EmployeeHistoric extends EmployeeHistoric_Base {
    private Timestamp when = null;

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

    public Timestamp getWhen() {
        return when;
    }

    public void setWhen(Timestamp when) {
        this.when = when;
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