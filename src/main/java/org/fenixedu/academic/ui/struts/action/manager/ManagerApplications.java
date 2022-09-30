/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.manager;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

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

    @StrutsApplication(path = "students", titleKey = "label.students", bundle = "AcademicAdminOffice", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class ManagerStudentsApp {
    }

}
