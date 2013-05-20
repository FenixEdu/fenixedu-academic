package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class SiteManagerAuthorizationFilter {

    public static final SiteManagerAuthorizationFilter instance = new SiteManagerAuthorizationFilter();

    public void execute(Site site, Section section) throws Exception {
        IGroup owner = site.getOwner();

        if (owner == null) {
            throw new NotAuthorizedFilterException();
        }

        if (!owner.allows(AccessControl.getUserView())) {
            throw new NotAuthorizedFilterException();
        }
    }

}
