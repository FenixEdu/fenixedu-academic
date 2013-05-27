package net.sourceforge.fenixedu.applicationTier.Servico.site;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.webSiteManager.WebSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import pt.ist.fenixWebFramework.services.Service;

/**
 * Adds a new person to the managers of a UnitSite.
 * 
 * @author cfgi
 */
public class AddUnitSiteManager {

    protected void run(UnitSite site, Person person) {
        site.addManagers(person);
    }

    // Service Invokers migrated from Berserk

    private static final AddUnitSiteManager serviceInstance = new AddUnitSiteManager();

    @Service
    public static void runAddUnitSiteManager(UnitSite site, Person person) throws NotAuthorizedException {
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

    @Service
    public static void runAddDepartmentSiteManager(UnitSite site, Person person) throws NotAuthorizedException {
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