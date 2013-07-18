package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class DeleteUnitSitePersonFunction extends ManageVirtualFunction {

    protected void run(UnitSite site, PersonFunction personFunction) {
        checkUnit(site, personFunction.getUnit());

        YearMonthDay tomorrow = new YearMonthDay().plusDays(1);
        if (!personFunction.belongsToPeriod(tomorrow, null)) {
            throw new DomainException("site.function.personFunction.notFuture");
        }

        personFunction.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteUnitSitePersonFunction serviceInstance = new DeleteUnitSitePersonFunction();

    @Service
    public static void runDeleteUnitSitePersonFunction(UnitSite site, PersonFunction personFunction)
            throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, personFunction);
    }

}