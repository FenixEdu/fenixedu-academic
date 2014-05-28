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

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;

public class RearrangeUnitSiteFunctions extends ManageVirtualFunction {

    protected void run(UnitSite site, Unit unit, Collection<Function> functions) {
        checkUnit(site, unit);
        unit.setFunctionsOrder(functions);
    }

    // Service Invokers migrated from Berserk

    private static final RearrangeUnitSiteFunctions serviceInstance = new RearrangeUnitSiteFunctions();

    @Atomic
    public static void runRearrangeUnitSiteFunctions(UnitSite site, Unit unit, Collection<Function> functions)
            throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, unit, functions);
    }

}