/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    @Atomic
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

    @Atomic
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