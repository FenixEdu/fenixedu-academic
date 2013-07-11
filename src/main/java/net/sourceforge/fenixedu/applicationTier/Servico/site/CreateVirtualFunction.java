package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateVirtualFunction extends ManageVirtualFunction {

    protected Function run(UnitSite site, Unit unit, MultiLanguageString name) {
        checkUnit(site, unit);
        return Function.createVirtualFunction(unit, name);
    }

    // Service Invokers migrated from Berserk

    private static final CreateVirtualFunction serviceInstance = new CreateVirtualFunction();

    @Atomic
    public static Function runCreateVirtualFunction(UnitSite site, Unit unit, MultiLanguageString name)
            throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        return serviceInstance.run(site, unit, name);
    }

}