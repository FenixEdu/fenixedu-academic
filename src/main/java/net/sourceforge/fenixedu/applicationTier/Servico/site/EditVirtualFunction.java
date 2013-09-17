package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EditVirtualFunction extends ManageVirtualFunction {

    protected void run(UnitSite site, Function function, MultiLanguageString name) {
        checkFunction(site, function);
        function.setTypeName(name);
    }

    // Service Invokers migrated from Berserk

    private static final EditVirtualFunction serviceInstance = new EditVirtualFunction();

    @Atomic
    public static void runEditVirtualFunction(UnitSite site, Function function, MultiLanguageString name)
            throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, function, name);
    }

}