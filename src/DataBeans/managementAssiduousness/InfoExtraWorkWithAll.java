/*
 * Created on 5/Fev/2005
 */
package DataBeans.managementAssiduousness;

import DataBeans.InfoEmployeeWithPerson;
import Dominio.managementAssiduousness.IExtraWork;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExtraWorkWithAll extends InfoExtraWork {

    public void copyFromDomain(IExtraWork extraWork) {
        super.copyFromDomain(extraWork);
        if(extraWork != null) {
            setInfoEmployee(InfoEmployeeWithPerson.newInfoFromDomain(extraWork.getEmployee()));          
        }
    }
    
    public static InfoExtraWork newInfoFromDomain(IExtraWork extraWork) {
        InfoExtraWorkWithAll infoExtraWorkWithAll = null;
        if (extraWork != null) {
            infoExtraWorkWithAll = new InfoExtraWorkWithAll();
            infoExtraWorkWithAll.copyFromDomain(extraWork);
        }
        return infoExtraWorkWithAll;
    }
}
