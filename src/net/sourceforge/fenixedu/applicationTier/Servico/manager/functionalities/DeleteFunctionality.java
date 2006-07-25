package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

/**
 * This service is used to delete a functionality from persistent storage.
 *
 * @author cfgi
 */
public class DeleteFunctionality extends Service {

    /**
     * @param functionality the functionality to delete
     * 
     * @see Functionality#delete()
     */
    public void run(Functionality functionality) {
        functionality.delete();
    }
    
}
