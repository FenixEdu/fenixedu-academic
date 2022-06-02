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
package org.fenixedu.academic.ui.struts.action.scientificCouncil;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.academic.ui.struts.action.commons.FacesEntryPoint;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsApplication(bundle = ScientificCouncilApplication.BUNDLE, path = "scientific-council", titleKey = "scientificCouncil",
        accessGroup = ScientificCouncilApplication.ACCESS_GROUP, hint = ScientificCouncilApplication.HINT)
@Mapping(path = "/index", module = "scientificCouncil", parameter = "/scientificCouncil/firstPage.jsp")
public class ScientificCouncilApplication extends ForwardAction {

    static final String HINT = "Scientific Council";
    static final String ACCESS_GROUP = "role(SCIENTIFIC_COUNCIL)";
    static final String BUNDLE = "ScientificCouncilResources";

    @StrutsApplication(bundle = BUNDLE, path = "bolonha-process", titleKey = "bolonha.process", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class ScientificBolonhaProcessApp {
    }

    @StrutsApplication(bundle = "CandidateResources", path = "applications", titleKey = "title.applications",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ScientificApplicationsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "teachers", titleKey = "title.teachers", accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ScientificTeachersApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = ScientificBolonhaProcessApp.class, path = "competence-courses",
            titleKey = "navigation.competenceCoursesManagement")
    @Mapping(path = "/competenceCourses/competenceCoursesManagement", module = "scientificCouncil")
    public static class ScientificCompetenceCoursesManagement extends FacesEntryPoint {
    }

}
