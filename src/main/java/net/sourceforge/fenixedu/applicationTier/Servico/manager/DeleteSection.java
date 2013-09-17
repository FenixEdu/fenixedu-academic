package net.sourceforge.fenixedu.applicationTier.Servico.manager;

/**
 * 
 * @author lmac1
 */

import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import pt.ist.fenixframework.Atomic;

public class DeleteSection {

    protected Boolean run(Site site, final Section section) {
        if (section != null) {
            section.delete();
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteSection serviceInstance = new DeleteSection();

    @Atomic
    public static Boolean runDeleteSection(Site site, Section section) throws NotAuthorizedException {
        SiteManagerAuthorizationFilter.instance.execute(site);
        return serviceInstance.run(site, section);
    }

}