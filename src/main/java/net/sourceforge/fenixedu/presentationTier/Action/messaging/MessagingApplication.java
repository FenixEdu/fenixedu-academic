package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

public class MessagingApplication {

    private static final String BUNDLE = "MessagingResources";
    private static final String ACCESS_GROUP = "role(MESSAGING)";
    private static final String HINT = "Messaging";

    @StrutsApplication(descriptionKey = "messaging.menu.announcements.link", path = "messaging",
            titleKey = "messaging.menu.announcements.link", bundle = BUNDLE, accessGroup = ACCESS_GROUP, hint = HINT)
    public static class MessagingAnnouncementsApp {
    }

    @StrutsApplication(descriptionKey = "label.emails", path = "emails", titleKey = "label.emails", bundle = BUNDLE,
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class MessagingEmailsApp {
    }

    @StrutsApplication(descriptionKey = "label.navheader.files", path = "files", titleKey = "label.navheader.files",
            bundle = BUNDLE, accessGroup = ACCESS_GROUP, hint = HINT)
    public static class MessagingFilesApp {
    }

    @StrutsApplication(descriptionKey = "label.navheader.search", path = "search", titleKey = "label.navheader.search",
            bundle = BUNDLE, accessGroup = ACCESS_GROUP, hint = HINT)
    public static class MessagingSearchApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = MessagingSearchApp.class, path = "organizational-structure", titleKey = "label.orgUnit")
    @Mapping(path = "/organizationalStructure/structurePage", module = "messaging")
    public static class OrganizationalStructurePage extends FacesEntryPoint {
    }
}
