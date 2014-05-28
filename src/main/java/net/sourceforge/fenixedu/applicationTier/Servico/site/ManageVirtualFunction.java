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

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ManageVirtualFunction {

    protected void checkFunction(UnitSite site, Function function) {
        checkUnit(site, function.getUnit());

        if (!function.isVirtual()) {
            throw new DomainException("site.functions.notVirtual");
        }
    }

    protected void checkUnit(UnitSite site, Unit unit) {
        if (unit == site.getUnit()) {
            return;
        }

        if (site.getUnit().getAllSubUnits().contains(unit)) {
            return;
        }

        throw new DomainException("site.functions.unrelatedUnit");
    }

}
