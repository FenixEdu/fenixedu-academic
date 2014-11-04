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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.service.factoryExecutors.RegistrationIngressionFactorExecutor.RegistrationIngressionEditor;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Input;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

@Mapping(path = "/manageIngression", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showEditIngression", path = "/academicAdminOffice/manageIngression.jsp") })
public class ManageIngressionDA extends FenixDispatchAction {

    private Registration getRegistration(final HttpServletRequest request) {
        return getDomainObject(request, "registrationId");
    }

    private RegistrationIngressionEditor getRegistrationIngressionEditor() {
        return getRenderedObject();
    }

    @Input
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("ingressionBean", new RegistrationIngressionEditor(getRegistration(request)));
        return mapping.findForward("showEditIngression");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        RegistrationIngressionEditor ingressionInformationBean = getRegistrationIngressionEditor();
        if (!ingressionInformationBean.hasRegistrationProtocol()
                || ingressionInformationBean.getRegistrationProtocol().isEnrolmentByStudentAllowed()) {
            ingressionInformationBean.setAgreementInformation(null);
        }

        RenderUtils.invalidateViewState();
        request.setAttribute("ingressionBean", ingressionInformationBean);
        return mapping.findForward("showEditIngression");
    }

    public ActionForward editIngression(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeFactoryMethod();
        } catch (final DomainException e) {
            request.setAttribute("ingressionBean", getRegistrationIngressionEditor());
            RenderUtils.invalidateViewState();
            addActionMessage(request, e.getKey());
            return mapping.findForward("showEditIngression");
        }

        addActionMessage(request, "message.registration.ingression.and.agreement.edit.success");
        request.setAttribute("registrationId", getRegistrationIngressionEditor().getRegistration().getExternalId());

        return prepare(mapping, actionForm, request, response);
    }

}
