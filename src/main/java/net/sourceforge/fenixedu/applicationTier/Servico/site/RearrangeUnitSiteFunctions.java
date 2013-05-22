package net.sourceforge.fenixedu.applicationTier.Servico.site;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class RearrangeUnitSiteFunctions extends ManageVirtualFunction {

    protected void run(UnitSite site, Unit unit, Collection<Function> functions) {
        checkUnit(site, unit);
        unit.setFunctionsOrder(functions);
    }

    // Service Invokers migrated from Berserk

    private static final RearrangeUnitSiteFunctions serviceInstance = new RearrangeUnitSiteFunctions();

    @Service
    public static void runRearrangeUnitSiteFunctions(UnitSite site, Unit unit, Collection<Function> functions)
            throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, unit, functions);
    }

}