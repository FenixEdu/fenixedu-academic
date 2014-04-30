package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Site;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;

public class SiteManagerAuthorizationFilter {

    public static final SiteManagerAuthorizationFilter instance = new SiteManagerAuthorizationFilter();

    public void execute(Site site) throws NotAuthorizedException {
        Group owner = site.getOwner();

        if (owner == null) {
            throw new NotAuthorizedException();
        }

        if (!owner.isMember(Authenticate.getUser())) {
            throw new NotAuthorizedException();
        }
    }

}
