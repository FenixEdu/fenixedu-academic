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
package net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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

    @StrutsFunctionality(app = CompetenceCourseManagementApp.class, path = "view", titleKey = "label.view")
    @Mapping(module = "bolonhaManager", path = "/competenceCourses/competenceCoursesManagement")
    public static class CompetenceCoursesManagement extends FacesEntryPoint {

    }

    @StrutsFunctionality(app = CurricularPlansManagementApp.class, path = "view", titleKey = "label.view")
    @Mapping(module = "bolonhaManager", path = "/curricularPlans/curricularPlansManagement")
    public static class CurricularPlansManagement extends FacesEntryPoint {

    }

}