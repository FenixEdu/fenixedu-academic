package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.BoardSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "messaging", path = "/announcements/boards", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "/messaging/announcements/searchBoards.jsp", tileProperties = @Tile(
		title = "private.messaging.announcements.announcementboards")) })
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
		final BoardSearchBean boardSearchBean = getRenderedObject();
		return boardSearchBean == null ? (BoardSearchBean) request.getAttribute("boardSearchBean") : boardSearchBean;
	}

}