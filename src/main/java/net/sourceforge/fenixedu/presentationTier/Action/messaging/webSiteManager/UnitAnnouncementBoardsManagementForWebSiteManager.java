package net.sourceforge.fenixedu.presentationTier.Action.messaging.webSiteManager;

import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.ListSitesAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/announcements/manageUnitAnnouncementBoard",
        formBean = "unitAnnouncementBoardsManagementForm", validate = false, functionality = ListSitesAction.class)
@Forwards(@Forward(name = "editAnnouncementBoardApprovers",
        path = "/messaging/announcements/editUnitAnnouncementBoardApprovers.jsp"))
public class UnitAnnouncementBoardsManagementForWebSiteManager extends
        net.sourceforge.fenixedu.presentationTier.Action.messaging.UnitAnnouncementBoardsManagement {
}