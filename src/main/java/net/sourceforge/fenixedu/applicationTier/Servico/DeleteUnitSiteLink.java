package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLink;
import pt.ist.fenixframework.Atomic;

public class DeleteUnitSiteLink {

    protected void run(UnitSite site, UnitSiteLink link) {
        link.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteUnitSiteLink serviceInstance = new DeleteUnitSiteLink();

    @Atomic
    public static void runDeleteUnitSiteLink(UnitSite site, UnitSiteLink link) throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, link);
    }

}