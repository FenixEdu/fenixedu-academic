package net.sourceforge.fenixedu.presentationTier.Action.coordinator.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.AnnouncementsManagementDA;


@Mapping(path = "/announcementsManagement", module = "coordinator")
@Forwards(value = {
	@Forward(name = "noBoards", path = "coordinator-announcements-no-boards"),
	@Forward(name = "viewAnnouncementsRedirect", path = "/announcementsManagement.do?method=viewAnnouncements&tabularVersion=true"),
	@Forward(name = "listAnnouncementBoards", path = "coordinator-announcements-list-boards"),
	@Forward(name = "listAnnouncements", path = "coordinator-announcements-list-announcements"),
	@Forward(name = "add", path = "coordinator-announcements-add-announcement"),
	@Forward(name = "viewAnnouncement", path = "coordinator-announcements-view-announcement"),
	@Forward(name = "edit", path = "coordinator-announcements-edit-announcement"),
	@Forward(name = "uploadFile", path = "coordinator-announcements-uploadFile"),
	@Forward(name = "editFile", path = "coordinator-announcements-editFile")})
public class AnnouncementsManagementDA_AM1 extends AnnouncementsManagementDA {

}
