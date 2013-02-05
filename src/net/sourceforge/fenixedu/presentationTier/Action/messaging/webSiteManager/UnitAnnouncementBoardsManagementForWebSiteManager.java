package net.sourceforge.fenixedu.presentationTier.Action.messaging.webSiteManager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/announcements/manageUnitAnnouncementBoard",
        attribute = "unitAnnouncementBoardsManagementForm", formBean = "unitAnnouncementBoardsManagementForm", scope = "request",
        validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "editAnnouncementBoardApprovers",
        path = "website-announcement-boards-approvers-management-edit-unit-board") })
public class UnitAnnouncementBoardsManagementForWebSiteManager extends
        net.sourceforge.fenixedu.presentationTier.Action.messaging.UnitAnnouncementBoardsManagement {
}