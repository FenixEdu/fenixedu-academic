package DataBeans;

import Dominio.IEmployee;

/**
 * @author Fernanda Quitério Created on 6/Set/2004
 *  
 */
public class InfoEmployeeWithPerson extends InfoEmployee {

    public void copyFromDomain(IEmployee employee) {
        super.copyFromDomain(employee);
        if (employee != null) {
            setPerson(InfoPerson.newInfoFromDomain(employee.getPerson()));
        }
    }

    public static InfoEmployee newInfoFromDomain(IEmployee employee) {
        InfoEmployeeWithPerson infoEmployeeWithPerson = null;
        if (employee != null) {
            infoEmployeeWithPerson = new InfoEmployeeWithPerson();
            infoEmployeeWithPerson.copyFromDomain(employee);
        }
        return infoEmployeeWithPerson;
    }
}