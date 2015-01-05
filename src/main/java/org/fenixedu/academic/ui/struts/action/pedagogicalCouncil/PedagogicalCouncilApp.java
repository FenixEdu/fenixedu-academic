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
package org.fenixedu.academic.ui.struts.action.pedagogicalCouncil;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.academic.ui.struts.action.commons.FacesEntryPoint;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsApplication(bundle = PedagogicalCouncilApp.BUNDLE, path = "pedagogical-council", titleKey = "pedagogical.council",
        accessGroup = "role(PEDAGOGICAL_COUNCIL)", hint = PedagogicalCouncilApp.HINT)
@Mapping(path = "/index", module = "pedagogicalCouncil", parameter = "/pedagogicalCouncil/index.jsp")
public class PedagogicalCouncilApp extends ForwardAction {

    static final String BUNDLE = "PedagogicalCouncilResources";
    static final String ACCESS_GROUP = "role(PEDAGOGICAL_COUNCIL)";
    static final String HINT = "Pedagogical Council";

    @StrutsApplication(bundle = BUNDLE, path = "bolonha-process", titleKey = "bolonha.process", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class PedagogicalBolonhaProcessApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "delegates", titleKey = "delegates.section", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class PedagogicalDelegatesApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "control", titleKey = "link.control", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class PedagogicalControlApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = PedagogicalBolonhaProcessApp.class, path = "competence-courses",
            titleKey = "navigation.competenceCoursesManagement")
    @Mapping(path = "/competenceCourses/competenceCoursesManagement", module = "pedagogicalCouncil")
    public static class PedagogicalCompetenceCoursesManagement extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = PedagogicalBolonhaProcessApp.class, path = "curricular-plans",
            titleKey = "navigation.curricularPlansManagement")
    @Mapping(path = "/curricularPlans/curricularPlansManagement", module = "pedagogicalCouncil")
    public static class PedagogicalCurricularPlansManagement extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = PedagogicalDelegatesApp.class, path = "elections", titleKey = "link.createEditVotingPeriods")
    @Mapping(path = "/delegateElections", module = "pedagogicalCouncil",
            parameter = "/pedagogicalCouncil/elections/firstPage.jsp")
    public static class PedagogicalDelegateElectionsEntryPoint extends ForwardAction {
    }

}
