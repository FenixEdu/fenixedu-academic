package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.BoardSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class BoardsDA extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	BoardSearchBean boardSearchBean = getBoardSearchBean(request);
	RenderUtils.invalidateViewState();
	if (boardSearchBean == null) {
	    boardSearchBean = new BoardSearchBean();
	}
	request.setAttribute("boardSearchBean", boardSearchBean);
	request.setAttribute("boards", boardSearchBean.getSearchResult());
	return mapping.findForward("search");
    }

    private BoardSearchBean getBoardSearchBean(HttpServletRequest request) {
	final BoardSearchBean boardSearchBean = (BoardSearchBean) getRenderedObject();
	return boardSearchBean == null ? (BoardSearchBean) request.getAttribute("boardSearchBean") : boardSearchBean;
    }

}