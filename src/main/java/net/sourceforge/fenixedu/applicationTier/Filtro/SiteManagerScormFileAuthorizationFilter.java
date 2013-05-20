package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateScormFile.CreateScormFileItemForItemArgs;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class SiteManagerScormFileAuthorizationFilter {

    public static final SiteManagerScormFileAuthorizationFilter instance = new SiteManagerScormFileAuthorizationFilter();

    protected void execute(CreateScormFileItemForItemArgs args) throws NotAuthorizedFilterException {
        IGroup owner = args.getSite().getOwner();

        if (owner == null) {
            throw new NotAuthorizedFilterException();
        }

        if (!owner.allows(AccessControl.getUserView())) {
            throw new NotAuthorizedFilterException();
        }
    }

}
