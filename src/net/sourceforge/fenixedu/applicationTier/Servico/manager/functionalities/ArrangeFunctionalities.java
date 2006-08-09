package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import pt.ist.utl.fenix.utils.Pair;

/**
 * This service can be used to rearrange the hierarchy of several functionalities
 * at once.
 * 
 * @author cfgi
 */
public class ArrangeFunctionalities extends Service {
    
    /**
     * Every pair in the given list will be changed so that the value is make a 
     * child of the key.
     * 
     * @param arrangements list of pairs (parent, child)
     */
    public void run(List<Pair<Module, Functionality>> arrangements) {
        for (Pair<Module, Functionality> pair : arrangements) {
            pair.getValue().setModule(pair.getKey());
        }
    }
    
}
