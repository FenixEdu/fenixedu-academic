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

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

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

    @Atomic
    public static void runDeleteUnitSitePersonFunction(UnitSite site, PersonFunction personFunction)
            throws NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, personFunction);
    }

}