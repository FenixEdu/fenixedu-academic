package net.sourceforge.fenixedu.applicationTier.Servico;


import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import pt.ist.fenixframework.Atomic;

public class DeleteUnitSiteBanner {

    protected void run(final UnitSite site, final UnitSiteBanner banner) {
        banner.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteUnitSiteBanner serviceInstance = new DeleteUnitSiteBanner();

    @Atomic
    public static void runDeleteUnitSiteBanner(UnitSite site, UnitSiteBanner banner) throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, banner);
    }

}