package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author Tânia Pousão
 */
public class EmployeeHistoric extends EmployeeHistoric_Base {

    public Timestamp getWhen() {
        if (this.getWhenDate() != null) {
            return new Timestamp(this.getWhenDate().getTime());
        }
        return null;
    }

    public void setWhen(Timestamp when) {
        if (when != null) {
            this.setWhenDate(new Date(when.getTime()));
        } else {
            this.setWhenDate(null);
        }
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
