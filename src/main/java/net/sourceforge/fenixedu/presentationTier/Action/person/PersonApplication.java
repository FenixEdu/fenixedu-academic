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