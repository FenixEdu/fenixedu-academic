package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import org.apache.struts.action.ActionMapping;

@Mapping(path = "/announcementsManagement", module = "publicRelations")
@Forwards( {
	@Forward(name = "add", path = "publicRelationsOffice-add-announcement"),
	@Forward(name = "edit", path = "publicRelationsOffice-edit-announcement"),
	@Forward(name = "listAnnouncementBoards", path = "publicRelationsOffice-list-announcement-boards"),
	@Forward(name = "listAnnouncements", path = "publicRelationsOffice-list-announcements"),
	@Forward(name = "viewAnnouncementBoard", path = "publicRelationsOffice-view-announcementBoard"),
	@Forward(name = "viewAnnouncement", path = "publicRelationsOffice-view-announcement"),
	@Forward(name = "uploadFile", path = "publicRelationsOffice-uploadFile"),
	@Forward(name = "editFile", path = "publicRelationsOffice-editFile") })
	
public class PublicRelationAnnouncementManagement extends AnnouncementManagement {

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
	final Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();
	final Person person = this.getLoggedPerson(request);
	for (final AnnouncementBoard currentBoard : rootDomainObject.getInstitutionUnit().getBoards()) {
	    final UnitAnnouncementBoard board = (UnitAnnouncementBoard) currentBoard;
	    if (board.getWriters().isMember(person)) {
		boards.add(board);
	    }
	}
	return boards;
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
	return "/announcementsManagement.do";
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
	return "tabularVersion=true";
    }

}
