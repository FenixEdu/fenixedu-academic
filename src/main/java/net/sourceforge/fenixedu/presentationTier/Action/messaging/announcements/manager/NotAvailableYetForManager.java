package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "manager", path = "/announcements/manageExecutionCourseAnnouncementBoard",
        attribute = "unitAnnouncementBoardsManagementForm", formBean = "unitAnnouncementBoardsManagementForm", scope = "request")
@Forwards(value = { @Forward(name = "notAvailable", path = "/messaging/announcements/notAvailableYet.jsp",
        tileProperties = @Tile(navLocal = "/manager/announcements/menu.jsp")) })
public class NotAvailableYetForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.NotAvailableYet {
}