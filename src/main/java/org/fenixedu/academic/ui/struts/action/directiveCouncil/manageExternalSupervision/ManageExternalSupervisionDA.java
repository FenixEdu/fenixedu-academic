/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.directiveCouncil.manageExternalSupervision;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.directiveCouncil.DirectiveCouncilApplication.DirectiveCouncilExternalSupervision;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = DirectiveCouncilExternalSupervision.class, path = "manage",
        titleKey = "link.directiveCouncil.manageExternalSupervision")
@Mapping(path = "/manageExternalSupervision", module = "directiveCouncil")
@Forwards({
        @Forward(name = "selectRegistrationAgreement",
                path = "/directiveCouncil/manageExternalSupervision/selectRegistrationAgreement.jsp"),
        @Forward(name = "showSupervisors", path = "/directiveCouncil/manageExternalSupervision/showSupervisors.jsp") })
public class ManageExternalSupervisionDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSelectAgreement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("sessionBean", new ManageExternalSupervisionBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("selectRegistrationAgreement");
    }

    public ActionForward showSupervisors(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ManageExternalSupervisionBean bean = getRenderedObject("sessionBean");
        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");
        return mapping.findForward("showSupervisors");
    }

    public ActionForward addSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ManageExternalSupervisionBean bean = getRenderedObject("sessionBean");
        bean.addSupervisor();
        bean.setNewSupervisor(null);

        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");
        request.setAttribute("startVisible", true);

        return mapping.findForward("showSupervisors");
    }

    public ActionForward invalidAddSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ManageExternalSupervisionBean bean = getRenderedObject("sessionBean");
        bean.setNewSupervisor(null);

        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");
        request.setAttribute("startVisible", true);

        return mapping.findForward("showSupervisors");
    }

    public ActionForward deleteSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String personId = request.getParameter("supervisorId");
        Person person = (Person) FenixFramework.getDomainObject(personId);
        final String registrationProtocolId = request.getParameter("registrationProtocolId");
        RegistrationProtocol registrationProtocol = FenixFramework.getDomainObject(registrationProtocolId);

        ManageExternalSupervisionBean bean = new ManageExternalSupervisionBean(registrationProtocol);
        bean.removeSupervisor(person);

        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");

        return mapping.findForward("showSupervisors");
    }

}
