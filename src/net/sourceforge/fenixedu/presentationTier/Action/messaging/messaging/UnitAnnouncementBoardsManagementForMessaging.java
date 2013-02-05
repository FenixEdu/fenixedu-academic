package net.sourceforge.fenixedu.presentationTier.Action.messaging.messaging;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "messaging", path = "/announcements/manageUnitAnnouncementBoard",
        attribute = "unitAnnouncementBoardsManagementForm", formBean = "unitAnnouncementBoardsManagementForm", scope = "request",
        parameter = "method")
@Forwards(value = {
        @Forward(name = "viewAnnouncement", path = "/messaging/announcements/viewAnnouncement.jsp"),
        @Forward(name = "createBoard", path = "/messaging/announcements/createUnitAnnouncementBoard.jsp"),
        @Forward(name = "editAnnouncementBoardApprovers",
                path = "/messaging/announcements/editUnitAnnouncementBoardApprovers.jsp"),
        @Forward(name = "editAnnouncementBoard", path = "/messaging/announcements/editUnitAnnouncementBoard.jsp"),
        @Forward(name = "edit", path = "/messaging/announcements/editAnnouncement.jsp"),
        @Forward(name = "listAnnouncements", path = "/messaging/announcements/listBoardAnnouncements.jsp"),
        @Forward(name = "add", path = "/messaging/announcements/addAnnouncement.jsp"),
        @Forward(name = "listAnnouncementBoards", path = "/messaging/announcements/listUnitAnnouncementBoards.jsp") })
public class UnitAnnouncementBoardsManagementForMessaging extends
        net.sourceforge.fenixedu.presentationTier.Action.messaging.UnitAnnouncementBoardsManagement {
}