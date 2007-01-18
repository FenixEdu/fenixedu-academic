package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateScormFileItemForItem.CreateScormFileItemForItemArgs;
import net.sourceforge.fenixedu.domain.Site;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class SiteManagerScormFileAuthorizationFilter extends
        SiteManagerAuthorizationFilter {

    @Override
    protected Site getSite(ServiceRequest request, ServiceResponse response) {
        CreateScormFileItemForItemArgs args = (CreateScormFileItemForItemArgs) request.getServiceParameters().getParameter(0);
        return args.getSite();
    }
    
}
