/**
 * Author : Goncalo Luiz
 * Creation Date: Jun 26, 2006,3:07:21 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.messaging.announcements;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements.CreateUnitAnnouncementBoard.UnitAnnouncementBoardParameters;
import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.UnitAnnouncementBoardsManagementForm;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 26, 2006,3:07:21 PM
 * 
 */
public class UnitAnnouncementBoardsManagement extends AnnouncementManagement {

    public ActionForward showTree(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
     
        request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
        return mapping.findForward("chooseUnit");

    }

    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionForward destination = null;
        if (this.getRequestedUnit(request) != null) {
            destination = this.prepareCreateBoard(mapping, actionForm, request, response);
        } else {
            destination = super.start(mapping, actionForm, request, response);
        }
        return destination;

    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Unit unit = this.getRequestedUnit(request);
        request.setAttribute("unit", unit);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.deleteAnnouncement(mapping, form, request, response);
        return this.prepareEditAnnouncementBoard(mapping, form, request, response);
    }

    public ActionForward deleteAnnouncementBoard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!getLoggedPerson(request).hasRole(RoleType.MANAGER)) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.delete.board"));
            saveErrors(request, actionMessages);
        } else {

            ServiceUtils.executeService(getUserView(request), "DeleteAnnouncementBoard",
                    new Object[] { this.getRequestedAnnouncementBoard(request) });

            if (request.getParameter("returnAction") != null
                    && !request.getParameter("returnAction").equals("")
                    && !request.getParameter("returnAction").equals("null")) {
                ActionForward destination = new ActionForward();
                String returnUrl = request.getParameter("returnAction");
                if (request.getParameter("returnMethod") != null
                        && !request.getParameter("returnMethod").equals("")
                        && !request.getParameter("returnMethod").equals("null")) {
                    returnUrl += "?method=" + request.getParameter("returnMethod");
                }
                destination.setPath(returnUrl);
                return destination;

            }
        }
        
        return this.prepareCreateBoard(mapping, actionForm, request, response);
    }

    public ActionForward createBoard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!getLoggedPerson(request).hasRole(RoleType.MANAGER)) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.create.board"));
            saveErrors(request, actionMessages);
        } else {
            UnitAnnouncementBoardsManagementForm form = (UnitAnnouncementBoardsManagementForm) actionForm;
            UnitAnnouncementBoardParameters params = new UnitAnnouncementBoardParameters();
            params.name = form.getName();
            params.mandatory = form.getMandatory();
            params.readersGroupType = UnitBoardPermittedGroupType.valueOf(form
                    .getUnitBoardReadPermittedGroupType());
            params.writersGroupType = UnitBoardPermittedGroupType.valueOf(form
                    .getUnitBoardWritePermittedGroupType());
            params.managementGroupType = UnitBoardPermittedGroupType.valueOf(form
                    .getUnitBoardManagementPermittedGroupType());
            params.unitId = form.getKeyUnit();

            ServiceUtils.executeService(getUserView(request), "CreateUnitAnnouncementBoard",
                    new Object[] { params });
        }
        return this.prepareCreateBoard(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditAnnouncementBoard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnitAnnouncementBoard board = (UnitAnnouncementBoard) this
                .getRequestedAnnouncementBoard(request);

        if (board.getManagers() != null && !board.getManagers().allows(getUserView(request))) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.edit.board"));
            saveErrors(request, actionMessages);
            return this.showTree(mapping, actionForm, request, response);
        }

        UnitAnnouncementBoardsManagementForm form = (UnitAnnouncementBoardsManagementForm) actionForm;
        form.setReturnAction(request.getParameter("returnAction"));
        form.setReturnMethod(request.getParameter("returnMethod"));
        form.setName(board.getName());
        form.setMandatory(board.getMandatory());
        form.setKeyUnit(board.getParty().getIdInternal());
        form.setUnitBoardManagementPermittedGroupType(board.getUnitPermittedManagementGroupType() == null ? null
                        : board.getUnitPermittedManagementGroupType().name());
        form.setUnitBoardWritePermittedGroupType(board.getUnitPermittedWriteGroupType() == null ? null
                : board.getUnitPermittedWriteGroupType().name());
        form.setUnitBoardReadPermittedGroupType(board.getUnitPermittedReadGroupType() == null ? null
                : board.getUnitPermittedReadGroupType().name());
        
        if (request.getParameter("sortBy") == null) {
            request.setAttribute("sortBy", "creationDate=descending");
        } else {
            request.setAttribute("sortBy", request.getParameter("sortBy"));
        }

        request.setAttribute("unit", board.getParty());
        request.setAttribute("announcementBoard", board);
        request.setAttribute("announcements", board.getAnnouncements());
        return mapping.findForward("editAnnouncementBoard");
    }

    public ActionForward editAnnouncementBoard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnitAnnouncementBoardsManagementForm form = (UnitAnnouncementBoardsManagementForm) actionForm;
        UnitAnnouncementBoardParameters params = new UnitAnnouncementBoardParameters();
        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);

        if (board.getManagers() != null && !board.getManagers().allows(getUserView(request))) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.edit.board"));
            saveErrors(request, actionMessages);
        } else {

            params.name = form.getName();
            params.mandatory = form.getMandatory();
            params.readersGroupType = UnitBoardPermittedGroupType.valueOf(form
                    .getUnitBoardReadPermittedGroupType());
            params.writersGroupType = UnitBoardPermittedGroupType.valueOf(form
                    .getUnitBoardWritePermittedGroupType());
            params.managementGroupType = UnitBoardPermittedGroupType.valueOf(form
                    .getUnitBoardManagementPermittedGroupType());
            params.unitId = form.getKeyUnit();

            ServiceUtils.executeService(getUserView(request), "EditUnitAnnouncementBoard", new Object[] {
                    board, params });

            if (form.getReturnAction() != null && !form.getReturnAction().equals("")
                    && !form.getReturnAction().equals("null")) {
                ActionForward destination = new ActionForward();
                String returnUrl = form.getReturnAction();
                if (form.getReturnMethod() != null && !form.getReturnMethod().equals("")
                        && !form.getReturnMethod().equals("null")) {
                    returnUrl += "?method=" + form.getReturnMethod();
                }
                destination.setPath(returnUrl);
                return destination;
            }
        }
        return prepareEditAnnouncementBoard(mapping, actionForm, request, response);
    }

    public ActionForward prepareCreateBoard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Unit unit = this.getRequestedUnit(request);
        UnitAnnouncementBoardsManagementForm form = (UnitAnnouncementBoardsManagementForm) actionForm;
        form.setName(unit.getName());
        form.setKeyUnit(unit.getIdInternal());
        form.setMandatory(false);

        super.viewAllBoards(mapping, form, request, response);
        return mapping.findForward("createBoard");
    }

    public ActionForward addBookmark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.addBookmark(mapping, form, request, response);
        return this.start(mapping, form, request, response);
    }

    public ActionForward removeBookmark(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.removeBookmark(mapping, form, request, response);
        return this.start(mapping, form, request, response);
    }

    private Unit getRequestedUnit(HttpServletRequest request) {
        return getRequestedUnitOID(request) == null ? null : (Unit) rootDomainObject.readPartyByOID(this
                .getRequestedUnitOID(request));
    }

    private Integer getRequestedUnitOID(HttpServletRequest request) {
        return request.getParameter("keyUnit") == null || request.getParameter("keyUnit").equals("null") ? null
                : Integer.valueOf(request.getParameter("keyUnit"));
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
	StringBuilder result = new StringBuilder(); 
	if (getRequestedUnitOID(request) != null) {
	    result.append("keyUnit=").append(getRequestedUnitOID(request));
	}
	if (!StringUtils.isEmpty(request.getParameter("tabularVersion"))) {
	    result.append("&tabularVersion=").append(request.getParameter("tabularVersion"));
	}
        return result.toString();
    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        return "/announcements/manageUnitAnnouncementBoard.do";
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();
        Unit unit = this.getRequestedUnit(request);
        if (unit != null) {
            for (AnnouncementBoard board : unit.getBoards()) {
                if (board.getWriters() == null || board.getReaders() == null || board.getManagers() == null
                        || board.getWriters().allows(getUserView(request))
                        || board.getReaders().allows(getUserView(request))
                        || board.getManagers().allows(getUserView(request)))
                    boards.add(board);
            }

        }
        return boards;
    }

}
