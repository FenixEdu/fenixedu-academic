package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLayoutType;
import pt.ist.fenixWebFramework.services.Service;

/**
 * Changes the layout of a unit site.
 * 
 * @author cfgi
 */
public class ChangeUnitSiteLayout extends FenixService {

    protected void run(UnitSite site, UnitSiteLayoutType layout) {
        site.setLayout(layout);
    }

    // Service Invokers migrated from Berserk

    private static final ChangeUnitSiteLayout serviceInstance = new ChangeUnitSiteLayout();

    @Service
    public static void runChangeUnitSiteLayout(UnitSite site, UnitSiteLayoutType layout) throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, layout);
    }

}