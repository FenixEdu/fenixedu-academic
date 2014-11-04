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
package org.fenixedu.academic.ui.struts.action.departmentMember;

import org.fenixedu.academic.ui.struts.action.vigilancy.UnavailablePeriodManagement;

import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/vigilancy/unavailablePeriodManagement",
        functionality = VigilantManagementForDepartmentMember.class)
@Forwards({ @Forward(name = "editUnavailablePeriod", path = "/departmentMember/vigilancy/editUnavailablePeriod.jsp"),
        @Forward(name = "addedUnavailablePeriod", path = "/departmentMember/vigilancy/manageVigilant.jsp"),
        @Forward(name = "addUnavailablePeriod", path = "/departmentMember/vigilancy/createUnavailable.jsp"),
        @Forward(name = "deleteUnavailablePeriod", path = "/departmentMember/vigilancy/manageVigilant.jsp") })
public class UnavailablePeriodManagementForDepartmentMember extends UnavailablePeriodManagement {
}