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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.manager;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.EquivalencyPlanDA;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerBolonhaTransitionApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ManagerBolonhaTransitionApp.class, path = "global-equivalence", titleKey = "title.global.equivalence")
@Mapping(module = "manager", path = "/degreeCurricularPlan/equivalencyPlan")
@Forwards(value = { @Forward(name = "showPlan", path = "/manager/degreeCurricularPlan/showEquivalencyPlan.jsp"),
        @Forward(name = "addEquivalency", path = "/manager/degreeCurricularPlan/addEquivalency.jsp") })
public class EquivalencyPlanDAForManager extends EquivalencyPlanDA {

}