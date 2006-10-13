/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created
 *         on Jun 8, 2006, 2:28:29 PM
 * 
 */
public class WebSiteAnnouncementManagement extends AnnouncementManagement {

    @Override
    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	if (getAnnouncementBoardId(request) == null) {
	    return super.start(mapping, actionForm, request, response);
	} else {
	    return this.viewAnnouncementBoard(mapping, actionForm, request, response);
	}
    }

    public ActionForward viewAnnouncementBoard(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	super.viewAllBoards(mapping, actionForm, request, response);
	request.setAttribute("announcementBoard", this.getRequestedAnnouncementBoard(request));
	return mapping.findForward("viewAnnouncementBoard");
    }

    public ActionForward listAnnouncements(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	super.viewAnnouncements(mapping, form, request, response);
	super.viewAllBoards(mapping, form, request, response);
	getSortByParameter(request);
	return mapping.findForward("listAnnouncements");
    }

    private void getSortByParameter(HttpServletRequest request) {
	if (request.getParameter("sortBy") == null) {
	    request.setAttribute("sortBy", "creationDate=descending");
	} else {
	    request.setAttribute("sortBy", request.getParameter("sortBy"));
	}
    }

    @Override
    public ActionForward viewAnnouncement(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	this.viewAllBoards(mapping, actionForm, request, response);
	return super.viewAnnouncement(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	super.viewAnnouncements(mapping, form, request, response);
	super.viewAllBoards(mapping, form, request, response);
	return mapping.findForward("viewAnnouncementBoard");
    }

    @Override
    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	super.viewAllBoards(mapping, form, request, response);
	return super.addAnnouncement(mapping, form, request, response);
    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
	return "/announcementsManagement.do";
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
	final Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();
	for (final AnnouncementBoard currentBoard : rootDomainObject.getInstitutionUnit().getBoards()) {
	    final UnitAnnouncementBoard board = (UnitAnnouncementBoard) currentBoard;
	    if (board.getUnitPermittedWriteGroupType() == UnitBoardPermittedGroupType.UB_WEBSITE_MANAGER
		    && board.getWriters().isMember(this.getLoggedPerson(request)))
		boards.add(board);
	}
	return boards;
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
	return "tabularVersion=true";
    }

    @Override
    public ActionForward viewArchive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	getSortByParameter(request);
	return super.viewArchive(mapping, form, request, response);
    }

    @Override
    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	deleteAnnouncement(request);
	return this.listAnnouncements(mapping, form, request, response);
    }

}
