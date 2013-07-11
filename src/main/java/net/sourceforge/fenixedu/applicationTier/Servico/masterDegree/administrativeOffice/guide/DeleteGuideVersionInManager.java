package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.Guide;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideVersionInManager {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(String guideID) throws InvalidChangeServiceException {
        Guide guide = FenixFramework.getDomainObject(guideID);

        if (!guide.canBeDeleted()) {
            throw new InvalidChangeServiceException();
        }

        guide.delete();
    }

}