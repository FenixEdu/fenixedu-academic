/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DeleteNonAffiliatedTeacher extends FenixService {

    protected void run(NonAffiliatedTeacher nonAffiliatedTeacher) {
        nonAffiliatedTeacher.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteNonAffiliatedTeacher serviceInstance = new DeleteNonAffiliatedTeacher();

    @Service
    public static void runDeleteNonAffiliatedTeacher(NonAffiliatedTeacher nonAffiliatedTeacher) throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(nonAffiliatedTeacher);
        } catch (NotAuthorizedException ex1) {
            try {
                GEPAuthorizationFilter.instance.execute();
                serviceInstance.run(nonAffiliatedTeacher);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}