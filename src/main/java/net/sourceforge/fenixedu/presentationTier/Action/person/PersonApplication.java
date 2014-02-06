package net.sourceforge.fenixedu.presentationTier.Action.person;

import org.fenixedu.bennu.portal.StrutsApplication;

public class PersonApplication {

    @StrutsApplication(descriptionKey = "label.navheader.person", path = "personal-area", titleKey = "label.navheader.person",
            bundle = "resources.ApplicationResources", accessGroup = "role(PERSON)")
    public static class PersonalAreaApp {

    }

    @StrutsApplication(descriptionKey = "label.homepage", path = "homepage", titleKey = "label.homepage",
            bundle = "resources.ApplicationResources", accessGroup = "role(PERSON)")
    public static class HomepageApp {

    }

    @StrutsApplication(descriptionKey = "oauthapps.label", path = "external-applications", titleKey = "oauthapps.label",
            bundle = "resources.ApplicationResources", accessGroup = "role(PERSON)")
    public static class ExternalApplicationsApp {

    }

}
