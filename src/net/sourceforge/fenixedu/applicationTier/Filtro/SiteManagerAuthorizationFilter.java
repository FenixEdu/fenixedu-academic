package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class SiteManagerAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        Site site = getSite(request, response);
        
        IGroup owner = site.getOwner();
        
        if (owner == null) {
            throw new NotAuthorizedFilterException();
        }
        
        if (! owner.allows(getRemoteUser(request))) {
            throw new NotAuthorizedFilterException();
        }
    }

    protected Site getSite(ServiceRequest request, ServiceResponse response) {
        return (Site) request.getServiceParameters().getParameter(0);
    }
    
}
