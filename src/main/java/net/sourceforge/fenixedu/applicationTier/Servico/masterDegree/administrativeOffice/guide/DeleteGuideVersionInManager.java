package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.Guide;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideVersionInManager {

    @Atomic
    public static void run(String guideID) throws InvalidChangeServiceException {
        check(RolePredicates.MANAGER_PREDICATE);
        Guide guide = FenixFramework.getDomainObject(guideID);

        if (!guide.canBeDeleted()) {
            throw new InvalidChangeServiceException();
        }

        guide.delete();
    }

}