/*
 * Created on 5/Fev/2005
 */
package net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness;

import net.sourceforge.fenixedu.dataTransferObject.InfoEmployeeWithPerson;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWork;

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
