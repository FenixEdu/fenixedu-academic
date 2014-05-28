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

import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym;

import org.fenixedu.bennu.core.domain.Bennu;

public class DepartmentSiteController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        Unit departmentUnit = null;
        for (UnitAcronym acronym : Bennu.getInstance().getUnitAcronymsSet()) {
            if (acronym.getAcronym().equalsIgnoreCase(parts[0])) {
                departmentUnit = getDepartmentFromAcronym(acronym);
                break;
            }
        }

        if (departmentUnit == null) {
            return null;
        }
        return departmentUnit.getSite();
    }

    private DepartmentUnit getDepartmentFromAcronym(UnitAcronym acronym) {
        for (Unit unit : acronym.getUnitsSet()) {
            if (unit instanceof DepartmentUnit) {
                return (DepartmentUnit) unit;
            }
        }
        return null;
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return DepartmentSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 1;
    }

}
