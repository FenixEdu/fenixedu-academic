package net.sourceforge.fenixedu.applicationTier.Filtro;

import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateScormFile.CreateScormFileItemForItemArgs;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class SiteManagerScormFileAuthorizationFilter {

    public static final SiteManagerScormFileAuthorizationFilter instance = new SiteManagerScormFileAuthorizationFilter();

    public void execute(CreateScormFileItemForItemArgs args) throws NotAuthorizedException {
        IGroup owner = args.getSite().getOwner();

        if (owner == null) {
            throw new NotAuthorizedException();
        }

        if (!owner.allows(Authenticate.getUser())) {
            throw new NotAuthorizedException();
        }
    }

}
