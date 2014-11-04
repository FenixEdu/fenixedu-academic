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
/*
 * Created on April 21, 2006
 *	by mrsp
 */
package org.fenixedu.academic.ui.faces.bean.scientificCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.ui.faces.bean.manager.personManagement.ManagerFunctionsManagementBackingBean;

import org.joda.time.YearMonthDay;

public class ScientificCouncilFunctionsManagementBackingBean extends ManagerFunctionsManagementBackingBean {

    @Override
    protected void getUnitsList(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer, YearMonthDay currentDate) {

        openLITag(buffer);

        List<Unit> subUnits = new ArrayList<Unit>(getSubUnits(parentUnit, currentDate));
        Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer, parentUnitParent);
        }

        buffer.append("<a href=\"").append(getContextPath())
                .append("/scientificCouncil/functionsManagement/chooseFunction.faces?personID=").append(personID)
                .append("&unitID=").append(parentUnit.getExternalId()).append("\">").append(parentUnit.getPresentationName())
                .append("</a>").append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer, parentUnitParent);
        }

        for (Unit subUnit : subUnits) {
            getUnitsList(subUnit, parentUnit, buffer, currentDate);
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }
}
