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
package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.manageExternalSupervision;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.DirectiveCouncilApplication.DirectiveCouncilExternalSupervision;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
        RegistrationProtocol registrationProtocol =
                RegistrationProtocol.serveRegistrationProtocol(bean.getRegistrationAgreement());
        bean.setRegistrationProtocol(registrationProtocol);
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
