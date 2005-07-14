package net.sourceforge.fenixedu.domain;


/**
 * 
 * @author Tânia Pousão
 */
public class EmployeeHistoric extends EmployeeHistoric_Base {

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
