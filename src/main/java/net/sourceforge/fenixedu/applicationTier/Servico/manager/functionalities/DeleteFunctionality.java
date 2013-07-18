package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;


import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * This service is used to delete a functionality from persistent storage.
 * 
 * @author cfgi
 */
public class DeleteFunctionality {

    /**
     * @param functionality
     *            the functionality to delete
     * 
     * @see Functionality#delete()
     */
    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Content functionality) {
        functionality.delete();
    }

}