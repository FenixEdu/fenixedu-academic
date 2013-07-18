package net.sourceforge.fenixedu.applicationTier.Servico.site;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteVirtualFunction extends ManageVirtualFunction {

    protected void run(UnitSite site, Function function) {
        checkFunction(site, function);

        ArrayList<PersonFunction> accountability = new ArrayList<PersonFunction>(function.getPersonFunctions());
        for (PersonFunction pf : accountability) {
            pf.delete();
        }

        function.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteVirtualFunction serviceInstance = new DeleteVirtualFunction();

    @Service
    public static void runDeleteVirtualFunction(UnitSite site, Function function) throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, function);
    }

}