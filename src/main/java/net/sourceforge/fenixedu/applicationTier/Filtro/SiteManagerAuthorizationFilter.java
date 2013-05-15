package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.berserk.ServiceRequest;

public class SiteManagerAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request) throws Exception {
        Site site = getSite(request);

        IGroup owner = site.getOwner();

        if (owner == null) {
            throw new NotAuthorizedFilterException();
        }

        if (!owner.allows(AccessControl.getUserView())) {
            throw new NotAuthorizedFilterException();
        }
    }

    protected Site getSite(ServiceRequest request) {
        return (Site) request.getServiceParameters().getParameter(0);
    }

}
