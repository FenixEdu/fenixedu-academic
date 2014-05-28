/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.BoardSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.MessagingApplication.MessagingAnnouncementsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = MessagingAnnouncementsApp.class, path = "boards", titleKey = "messaging.menu.boards.link")
@Mapping(module = "messaging", path = "/announcements/boards")
@Forwards(@Forward(name = "search", path = "/messaging/announcements/searchBoards.jsp"))
public class BoardsDA extends FenixDispatchAction {

    @EntryPoint
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