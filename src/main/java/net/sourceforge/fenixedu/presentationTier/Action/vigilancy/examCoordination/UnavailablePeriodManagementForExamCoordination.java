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
package net.sourceforge.fenixedu.presentationTier.Action.vigilancy.examCoordination;

import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.UnavailablePeriodManagement;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ExamCoordinationApplication.class, path = "unavailability",
        titleKey = "label.vigilancy.unavailablePeriodsShortLabel")
@Mapping(module = "examCoordination", path = "/vigilancy/unavailablePeriodManagement")
@Forwards({
        @Forward(name = "prepareAddPeriodToVigilant", path = "/examCoordinator/vigilancy/addUnavailablePeriodToVigilant.jsp"),
        @Forward(name = "manageUnavailablePeriodsOfVigilants",
                path = "/examCoordinator/vigilancy/manageGroupsUnavailablePeriods.jsp"),
        @Forward(name = "editPeriodOfVigilant", path = "/examCoordinator/vigilancy/editUnavailablePeriodOfVigilant.jsp") })
public class UnavailablePeriodManagementForExamCoordination extends UnavailablePeriodManagement {
}