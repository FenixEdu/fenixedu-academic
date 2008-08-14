package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteBoardsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DepartmentEventsDA extends DepartmentBoardsDA {

    @Override
    protected MultiLanguageString getBoardName(HttpServletRequest request) {
	return UnitSiteBoardsDA.EVENTS;
    }

    public ActionForward viewEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return viewAnnouncements(mapping, form, request, response);
    }

}
