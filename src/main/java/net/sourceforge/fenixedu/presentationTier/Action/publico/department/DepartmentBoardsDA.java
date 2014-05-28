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
package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteBoardsDA;

public abstract class DepartmentBoardsDA extends UnitSiteBoardsDA {

    @Override
    protected void setUnitContext(HttpServletRequest request) {
        Department department = getDepartment(request);
        if (department != null) {
            request.setAttribute("department", department);
            OldCmsSemanticURLHandler.selectSite(request, department.getDepartmentUnit().getSite());
        }
    }

    public Department getDepartment(HttpServletRequest request) {
        Unit unit = getUnit(request);
        if (unit == null) {
            return null;
        } else {
            return unit.getDepartment();
        }
    }

    @Override
    public String getContextParamName() {
        return "selectedDepartmentUnitID";
    }

}
