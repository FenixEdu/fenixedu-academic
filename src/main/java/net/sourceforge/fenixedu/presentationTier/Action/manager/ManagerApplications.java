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
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "ManagerResources", path = "manager", titleKey = "MANAGER", hint = "Manager",
        accessGroup = "#managers")
@Mapping(path = "/index", module = "manager", parameter = "/manager/welcomScreen.jsp")
public class ManagerApplications extends ForwardAction {

    private static final String HINT = "Manager";
    private static final String ACCESS_GROUP = "#managers";
    private static final String BUNDLE = "ManagerResources";

    @StrutsApplication(bundle = BUNDLE, path = "system-management", titleKey = "title.system", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class ManagerSystemManagementApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "bolonha-transition", titleKey = "title.bolonha.transition",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ManagerBolonhaTransitionApp {
    }

    @StrutsApplication(path = "messages-notices", titleKey = "title.messages.and.notices", bundle = BUNDLE,
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ManagerMessagesAndNoticesApp {
    }

    @StrutsApplication(path = "organizational-structure", titleKey = "title.manager.organizationalStructureManagement",
            bundle = BUNDLE, accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ManagerOrganizationalStructureApp {
    }

    @StrutsApplication(path = "executions", titleKey = "title.executions", bundle = BUNDLE, accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class ManagerExecutionsApp {
    }

    @StrutsApplication(path = "people-management", titleKey = "title.people", bundle = BUNDLE,
            accessGroup = "(role(OPERATOR) | #managers)", hint = HINT)
    public static class ManagerPeopleApp {
    }

    @StrutsApplication(path = "payments", titleKey = "title.payments", bundle = BUNDLE, accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ManagerPaymentsApp {
    }

    @StrutsApplication(path = "students", titleKey = "label.students", bundle = "AcademicAdminOffice",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ManagerStudentsApp {
    }

    // Sub applications

    @StrutsApplication(path = "person-management", titleKey = "label.manager.personManagement", bundle = BUNDLE,
            accessGroup = "(role(OPERATOR) | #managers)", hint = HINT)
    @Mapping(path = "/personManagementApp", module = "manager", parameter = "/manager/personManagement/welcomeScreen.jsp")
    public static class ManagerPersonManagementApp extends ForwardAction {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = ManagerPaymentsApp.class, path = "update-gratuity-situations",
            titleKey = "title.gratuity.situations")
    @Mapping(path = "/gratuity/updateGratuitySituations", module = "manager")
    public static class UpdateGratuitySituations extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = ManagerPersonManagementApp.class, path = "management-functions",
            titleKey = "link.functions.management")
    @Mapping(path = "/functionsManagement/personSearchForFunctionsManagement", module = "manager")
    public static class ManagementFunctionsPage extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = ManagerOrganizationalStructureApp.class, path = "manage",
            titleKey = "link.manager.organizationalStructureManagement")
    @Mapping(path = "/organizationalStructureManagament/listAllUnits", module = "manager")
    public static class OrganizationalStructurePage extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = ManagerExecutionsApp.class, path = "create-execution-degree",
            titleKey = "label.manager.createExecutionDegrees")
    @Mapping(path = "/degree/chooseDegreeType", module = "manager")
    public static class CreateExecutionDegree extends FacesEntryPoint {
    }
}
