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
package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import com.google.common.collect.Iterables;

public abstract class UnitAcronymSiteTemplateController extends SiteTemplateController {

    private UnitSite unitSite;
    private final String unitAcronym;

    public UnitAcronymSiteTemplateController(String unitAcronym) {
        this.unitAcronym = unitAcronym;
    }

    @Override
    public Site selectSiteForPath(String[] parts) {
        return getAssemblySite();
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 0;
    }

    private UnitSite getAssemblySite() {
        if (unitSite == null) {
            unitSite = getAssemblyUnit().getSite();
        }
        return unitSite;
    }

    private Unit getAssemblyUnit() {
        return Iterables.getFirst(Unit.readUnitsByAcronym(unitAcronym, true), null);
    }
}
