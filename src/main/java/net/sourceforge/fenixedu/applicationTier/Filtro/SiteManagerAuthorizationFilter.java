package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class SiteManagerAuthorizationFilter extends Filtro {

    public void execute(Object[] parameters) throws Exception {
        Site site = getSite(parameters);

        IGroup owner = site.getOwner();

        if (owner == null) {
            throw new NotAuthorizedFilterException();
        }

        if (!owner.allows(AccessControl.getUserView())) {
            throw new NotAuthorizedFilterException();
        }
    }

    protected Site getSite(Object[] parameters) {
        return (Site) parameters[0];
    }

}
