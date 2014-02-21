package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import org.fenixedu.bennu.portal.StrutsApplication;

public class CommunicationApplication {

    @StrutsApplication(descriptionKey = "messaging.menu.announcements.link", path = "messaging",
            titleKey = "messaging.menu.announcements.link", bundle = "MessagingResources", accessGroup = "role(MESSAGING)",
            hint = "Messaging")
    public static class AnnouncementsApp {

    }

    @StrutsApplication(descriptionKey = "label.emails", path = "e-mails", titleKey = "label.emails",
            bundle = "MessagingResources", accessGroup = "role(MESSAGING)", hint = "Messaging")
    public static class EMailsApp {

    }

    @StrutsApplication(descriptionKey = "label.navheader.files", path = "files", titleKey = "label.navheader.files",
            bundle = "MessagingResources", accessGroup = "role(MESSAGING)", hint = "Messaging")
    public static class FilesApp {

    }

    @StrutsApplication(descriptionKey = "label.navheader.search", path = "search", titleKey = "label.navheader.search",
            bundle = "MessagingResources", accessGroup = "role(MESSAGING)", hint = "Messaging")
    public static class SearchApp {

    }
}
