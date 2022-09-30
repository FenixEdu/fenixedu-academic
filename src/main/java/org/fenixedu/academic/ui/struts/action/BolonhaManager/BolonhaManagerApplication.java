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
package org.fenixedu.academic.ui.struts.action.BolonhaManager;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "BolonhaManagerResources", path = "bolonha", titleKey = "bolonhaManager", hint = "Bolonha Manager",
        accessGroup = "role(BOLONHA_MANAGER)")
@Mapping(module = "bolonhaManager", path = "/index", parameter = "/bolonhaManager/index.jsp")
public class BolonhaManagerApplication extends ForwardAction {

    @StrutsApplication(bundle = "BolonhaManagerResources", path = "competence-courses",
            titleKey = "navigation.competenceCoursesManagement", accessGroup = "role(BOLONHA_MANAGER)", hint = "Bolonha Manager")
    public static class CompetenceCourseManagementApp {

    }

    @StrutsApplication(bundle = "BolonhaManagerResources", path = "curricular-plans",
            titleKey = "navigation.curricularPlansManagement", accessGroup = "role(BOLONHA_MANAGER)", hint = "Bolonha Manager")
    public static class CurricularPlansManagementApp {

    }

}