/*
 * Created on 19/Fev/2005
 */
package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWorkHistoric;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentExtraWorkHistoric extends IPersistentObject {
    public IExtraWorkHistoric readEXtraWorkHistoricByYear(Integer year) throws Exception;
}
