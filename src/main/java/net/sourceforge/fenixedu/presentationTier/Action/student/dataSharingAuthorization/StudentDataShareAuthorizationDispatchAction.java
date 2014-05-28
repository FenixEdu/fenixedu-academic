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
package net.sourceforge.fenixedu.presentationTier.Action.student.dataSharingAuthorization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentViewApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@StrutsFunctionality(app = StudentViewApp.class, descriptionKey = "title.student.dataShareAuthorizations", path = "data-share",
        titleKey = "title.student.dataShareAuthorizations.short")
@Mapping(path = "/studentDataShareAuthorization", module = "student")
@Forwards({
        @Forward(name = "authorizations", path = "/student/dataShareAuthorization/manageAuthorizations.jsp",
                tileProperties = @Tile(title = "private.student.view.dataauthorization")),
        @Forward(name = "historic", path = "/student/dataShareAuthorization/authorizationHistory.jsp", tileProperties = @Tile(
                title = "private.student.view.dataauthorization")) })
public class StudentDataShareAuthorizationDispatchAction extends FenixDispatchAction {
    @EntryPoint
    public ActionForward manageAuthorizations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("student", getLoggedPerson(request).getStudent());
        return mapping.findForward("authorizations");
    }

    public ActionForward saveAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        addActionMessage(request, "message.student.dataShareAuthorization.saveSuccess");
        request.setAttribute("student", getLoggedPerson(request).getStudent());
        return mapping.findForward("authorizations");
    }

    public ActionForward viewAuthorizationHistory(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("student", getLoggedPerson(request).getStudent());
        return mapping.findForward("historic");
    }
}
