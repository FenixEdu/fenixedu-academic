package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.fenixedu.bennu.core.security.Authenticate;

public class SiteManagerAuthorizationFilter {

    public static final SiteManagerAuthorizationFilter instance = new SiteManagerAuthorizationFilter();

    public void execute(Site site) throws NotAuthorizedException {
        IGroup owner = site.getOwner();

        if (owner == null) {
            throw new NotAuthorizedException();
        }

        if (!owner.allows(Authenticate.getUser())) {
            throw new NotAuthorizedException();
        }
    }

}
