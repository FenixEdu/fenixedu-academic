package net.sourceforge.fenixedu.applicationTier.Servico;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLink;
import pt.ist.fenixWebFramework.services.Service;

public class RearrangeUnitSiteLinks extends FenixService {

    protected void run(UnitSite site, Boolean top, List<UnitSiteLink> links) {
        if (top) {
            site.setTopLinksOrder(links);
        } else {
            site.setFooterLinksOrder(links);
        }
    }

    // Service Invokers migrated from Berserk

    private static final RearrangeUnitSiteLinks serviceInstance = new RearrangeUnitSiteLinks();

    @Service
    public static void runRearrangeUnitSiteLinks(UnitSite site, Boolean top, List<UnitSiteLink> links)
            throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, top, links);
    }

}