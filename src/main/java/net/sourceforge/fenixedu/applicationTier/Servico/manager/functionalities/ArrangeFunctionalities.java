package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import java.util.List;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.IFunctionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.Pair;

/**
 * This service can be used to rearrange the hierarchy of several
 * functionalities at once.
 * 
 * @author cfgi
 */
public class ArrangeFunctionalities {

    /**
     * Every pair in the given list will be changed so that the value is make a
     * child of the key.
     * 
     * @param arrangements
     *            list of pairs (parent, child)
     */
    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(List<Pair<Module, Content>> arrangements) {
        for (Pair<Module, Content> pair : arrangements) {
            ((IFunctionality) pair.getValue()).setModule(pair.getKey());
        }
    }

}