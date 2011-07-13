package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.messaging;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "messaging", path = "/announcements/manageExecutionCourseAnnouncementBoard", attribute = "unitAnnouncementBoardsManagementForm", formBean = "unitAnnouncementBoardsManagementForm", scope = "request")
@Forwards(value = { @Forward(name = "notAvailable", path = "/messaging/announcements/notAvailableYet.jsp") })
public class NotAvailableYetForMessaging extends net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.NotAvailableYet {
}