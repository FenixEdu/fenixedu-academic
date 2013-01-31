package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteBoardsDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(module = "publico", path = "/department/announcements", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "viewAnnouncement", path = "department-view-announcement"),
		@Forward(name = "listAnnouncements", path = "public-department-list-announcements") })
public class DepartmentAnnouncementsDA extends DepartmentBoardsDA {

	@Override
	protected MultiLanguageString getBoardName(HttpServletRequest request) {
		return UnitSiteBoardsDA.ANNOUNCEMENTS;
	}

}
