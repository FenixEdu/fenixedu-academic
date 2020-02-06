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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.candidacy.IngressionInformationBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Input;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

@Mapping(path = "/manageIngression", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showEditIngression", path = "/academicAdminOffice/manageIngression.jsp"),
        @Forward(name = "createReingression", path = "/academicAdminOffice/createReingression.jsp") })
public class ManageIngressionDA extends FenixDispatchAction {

    private Registration getRegistration(final HttpServletRequest request) {
        return getDomainObject(request, "registrationId");
    }

    private IngressionInformationBean getRegistrationIngressionEditor() {
        return getRenderedObject();
    }

    @Input
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = getRegistration(request);
        final IngressionInformationBean bean = new IngressionInformationBean(registration);
        bean.setRegistrationProtocol(registration.getRegistrationProtocol());
        bean.setIngressionType(registration.getIngressionType());
        bean.setEntryPhase(registration.getEntryPhase());

        request.setAttribute("ingressionBean", bean);

        return mapping.findForward("showEditIngression");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        IngressionInformationBean ingressionInformationBean = getRegistrationIngressionEditor();
        ingressionInformationBean.setAgreementInformation(null);

        RenderUtils.invalidateViewState();
        request.setAttribute("ingressionBean", ingressionInformationBean);
        return mapping.findForward("showEditIngression");
    }

    public ActionForward editIngression(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final IngressionInformationBean bean = getRegistrationIngressionEditor();
        try {
            editIngressionService(bean);
        } catch (final DomainException e) {
            request.setAttribute("ingressionBean", bean);
            RenderUtils.invalidateViewState();
            addActionMessage(request, e.getKey());
            return mapping.findForward("showEditIngression");
        }

        addActionMessage(request, "message.registration.ingression.and.agreement.edit.success");
        request.setAttribute("registrationId", bean.getRegistration().getExternalId());

        return prepare(mapping, actionForm, request, response);
    }

    @Atomic
    private void editIngressionService(IngressionInformationBean bean) {
        final Registration registration = bean.getRegistration();
        registration.setRegistrationProtocol(bean.getRegistrationProtocol());
        registration.setIngressionType(bean.getIngressionType());
        registration.setEntryPhase(bean.getEntryPhase());
    }

    public ActionForward prepareCreateReingression(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        Registration registration = getDomainObject(request, "registrationId");
        IngressionInformationBean bean = new IngressionInformationBean(registration);

        request.setAttribute("bean", bean);
        request.setAttribute("registration", registration);

        return mapping.findForward("createReingression");
    }

    public ActionForward createReingressionInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        Registration registration = getDomainObject(request, "registrationId");
        IngressionInformationBean ingressionInformationBean = getRenderedObject("bean");

        request.setAttribute("bean", ingressionInformationBean);
        request.setAttribute("registration", registration);

        return mapping.findForward("createReingression");
    }

    public ActionForward createReingression(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        try {
            Registration registration = getDomainObject(request, "registrationId");
            IngressionInformationBean bean = getRenderedObject("bean");

            registration.createReingression(bean.getExecutionYear(), bean.getReingressionDate());

            addActionMessage("success", request, "message.registration.reingression.mark.success");

            RenderUtils.invalidateViewState();
            return prepare(mapping, form, request, response);
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return createReingressionInvalid(mapping, form, request, response);
        }
    }

    public ActionForward deleteReingression(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        try {
            Registration registration = getDomainObject(request, "registrationId");
            ExecutionYear executionYear = getDomainObject(request, "executionYearId");

            registration.deleteReingression(executionYear);
            addActionMessage("success", request, "message.registration.reingression.delete.success");

            RenderUtils.invalidateViewState();
            return prepare(mapping, form, request, response);
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getKey());
            return prepare(mapping, form, request, response);
        }

    }
}
