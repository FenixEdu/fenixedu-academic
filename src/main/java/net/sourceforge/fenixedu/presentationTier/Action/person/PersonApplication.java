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
package net.sourceforge.fenixedu.presentationTier.Action.person;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

public class PersonApplication {

    @StrutsApplication(descriptionKey = "label.navheader.person", path = "personal-area", titleKey = "label.navheader.person",
            bundle = "ApplicationResources", accessGroup = "role(PERSON)", hint = "Person")
    @Mapping(path = "/index", module = "person", parameter = "/person/personMainPage.jsp")
    public static class PersonalAreaApp extends ForwardAction {

    }

    @StrutsApplication(descriptionKey = "label.homepage", path = "homepage", titleKey = "label.homepage",
            bundle = "ApplicationResources", accessGroup = "role(PERSON)", hint = "Person")
    public static class HomepageApp {

    }

    @StrutsApplication(descriptionKey = "oauthapps.label", path = "external-applications", titleKey = "oauthapps.label",
            bundle = "ApplicationResources", accessGroup = "role(PERSON)", hint = "Person")
    public static class ExternalApplicationsApp {

    }

    @StrutsFunctionality(app = PersonalAreaApp.class, path = "change-password", titleKey = "label.person.changePassword")
    @Mapping(path = "/changePassword", module = "person", parameter = "/person/showChangePassLink.jsp")
    public static class ShowPersonPasswordLink extends ForwardAction {

    }

}