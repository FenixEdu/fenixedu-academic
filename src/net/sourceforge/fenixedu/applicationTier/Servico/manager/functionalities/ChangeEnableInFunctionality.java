package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

/**
 * This service allows you to manage the enabled status of a functionality
 * making it globaly enabled or disable.
 * 
 * @author cfgi
 */
public class ChangeEnableInFunctionality extends Service {
    
    /**
     * Changes the enabled status of a functionality.
     * 
     * @param functionality the affected functionality
     * @param enable the new enabled status
     */
    public void run(Functionality functionality, boolean enable) {
        functionality.setEnabled(enable);
    }
    
}
