package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.accessControl.Group;

/**
 * 
 * @author naat
 * 
 */
public abstract class FileContentService {

    protected boolean isPublic(Group permittedGroup) {
        if (permittedGroup == null) {
            return true;
        }

        if (permittedGroup.isMember(null)) {
            return true;
        }

        return false;
    }
}