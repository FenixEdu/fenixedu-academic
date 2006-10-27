package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;

/**
 * 
 * @author naat
 * 
 */
public abstract class FileItemService extends Service {

    protected boolean isPublic(Group permittedGroup) {
        if (permittedGroup == null) {
            return true;
        }
        
        if (permittedGroup instanceof EveryoneGroup) {
            return true;
        }
        
        return false;
    }

}