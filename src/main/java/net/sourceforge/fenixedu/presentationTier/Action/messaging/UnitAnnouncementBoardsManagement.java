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
/**
 * Author : Goncalo Luiz
 * Creation Date: Jun 26, 2006,3:07:21 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements.DeleteAnnouncementBoard;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 26, 2006,3:07:21 PM
 * 
 */
public class UnitAnnouncementBoardsManagement extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.messaging.announcements.UnitAnnouncementBoardsManagement {

    @Override
    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "prepareEditAnnouncementBoard");
        return super.addAnnouncement(mapping, form, request, response);
    }

    @Override
    public ActionForward deleteAnnouncementBoard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (!getLoggedPerson(request).hasRole(RoleType.MANAGER)) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.not.allowed.to.delete.board"));
            saveErrors(request, actionMessages);
            return prepareEditAnnouncementBoard(mapping, actionForm, request, response);
        }

        try {
            DeleteAnnouncementBoard.run(this.getRequestedAnnouncementBoard(request));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey());
            return prepareEditAnnouncementBoard(mapping, actionForm, request, response);
        }

        return this.start(mapping, actionForm, request, response);
    }
}