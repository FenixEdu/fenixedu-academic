package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

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
    @Atomic
    public static void run(Content functionality) {
        check(RolePredicates.MANAGER_PREDICATE);
        functionality.delete();
    }

}