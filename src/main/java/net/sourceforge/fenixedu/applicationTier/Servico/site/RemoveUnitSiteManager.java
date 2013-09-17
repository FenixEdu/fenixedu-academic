package net.sourceforge.fenixedu.applicationTier.Servico.site;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.webSiteManager.WebSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import pt.ist.fenixframework.Atomic;

/**
 * Removes a person from the managers of a UnitSite.
 * 
 * @author cfgi
 */
public class RemoveUnitSiteManager {

    protected void run(UnitSite site, Person person) {
        site.removeManagers(person);
    }

    // Service Invokers migrated from Berserk

    private static final RemoveUnitSiteManager serviceInstance = new RemoveUnitSiteManager();

    @Atomic
    public static void runRemoveUnitSiteManager(UnitSite site, Person person) throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(site, person);
        } catch (NotAuthorizedException ex1) {
            try {
                ResearchSiteManagerAuthorizationFilter.instance.execute(site);
                serviceInstance.run(site, person);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static void runRemoveDepartmentSiteManager(UnitSite site, Person person) throws NotAuthorizedException {
        try {
            DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
            serviceInstance.run(site, person);
        } catch (NotAuthorizedException ex1) {
            try {
                WebSiteManagerAuthorizationFilter.instance.execute();
                serviceInstance.run(site, person);
            } catch (NotAuthorizedException ex2) {
                try {
                    ManagerAuthorizationFilter.instance.execute();
                    serviceInstance.run(site, person);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}