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
package org.fenixedu.academic.ui.struts.action.manager.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.accounting.PaymentCodeMapping;
import org.fenixedu.academic.domain.accounting.PaymentCodeMapping.PaymentCodeMappingBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerPaymentsApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = ManagerPaymentsApp.class, path = "payment-codes", titleKey = "label.payments.paymentCodes",
        bundle = "AcademicAdminOffice")
@Mapping(path = "/paymentCodesAttribution", module = "manager")
@Forwards({ @Forward(name = "viewCodes", path = "/manager/payments/codes/viewCodes.jsp"),
        @Forward(name = "createPaymentCodeMapping", path = "/manager/payments/codes/createPaymentCodeMapping.jsp") })
public class PaymentCodesAttributionDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareViewPaymentCodeMappings(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("paymentCodeMappingBean", new PaymentCodeMappingBean());
        return mapping.findForward("viewCodes");
    }

    public ActionForward viewPaymentCodeMappings(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PaymentCodeMappingBean bean = getRenderedObject("paymentCodeMappingBean");
        request.setAttribute("paymentCodeMappingBean", bean);

        if (bean.hasExecutionInterval()) {
            request.setAttribute("paymentCodeMappings", bean.getExecutionInterval().getPaymentCodeMappingsSet());
        }

        return mapping.findForward("viewCodes");
    }

    public ActionForward prepareCreatePaymentCodeMapping(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        RenderUtils.invalidateViewState();
        final PaymentCodeMappingBean bean = new PaymentCodeMappingBean();
        bean.setExecutionInterval((ExecutionInterval) getDomainObject(request, "executionIntervalOid"));
        request.setAttribute("paymentCodeMappingBean", bean);
        return mapping.findForward("createPaymentCodeMapping");
    }

    public ActionForward createPaymentCodeMapping(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PaymentCodeMappingBean bean = getRenderedObject("paymentCodeMappingBean");
        request.setAttribute("paymentCodeMappingBean", bean);

        try {
            bean.create();
        } catch (final DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("createPaymentCodeMapping");
        }

        request.setAttribute("paymentCodeMappings", bean.getExecutionInterval().getPaymentCodeMappingsSet());
        RenderUtils.invalidateViewState();
        bean.clear();
        return mapping.findForward("viewCodes");
    }

    public ActionForward createPaymentCodeMappingInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("paymentCodeMappingBean", getRenderedObject("paymentCodeMappingBean"));
        return mapping.findForward("createPaymentCodeMapping");
    }

    public ActionForward deletePaymentCodeMapping(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PaymentCodeMapping codeMapping = getDomainObject(request, "paymentCodeMappingOid");
        final PaymentCodeMappingBean bean = new PaymentCodeMappingBean();
        bean.setExecutionInterval(codeMapping.getExecutionInterval());

        request.setAttribute("paymentCodeMappingBean", bean);
        request.setAttribute("paymentCodeMappings", bean.getExecutionInterval().getPaymentCodeMappingsSet());

        try {
            codeMapping.delete();
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        return mapping.findForward("viewCodes");
    }
}