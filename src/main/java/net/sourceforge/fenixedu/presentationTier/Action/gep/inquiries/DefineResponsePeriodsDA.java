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
package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryDefinitionPeriodBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTemplate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.gep.GepApplication.GepInquiriesApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = GepInquiriesApp.class, path = "define-response-period",
        titleKey = "link.inquiries.define.response.period")
@Mapping(module = "gep", path = "/defineResponsePeriods", input = "/defineResponsePeriods.do?method=prepare&page=0")
@Forwards(@Forward(name = "showForm", path = "/gep/inquiries/defineResponsePeriods.jsp"))
public class DefineResponsePeriodsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InquiryDefinitionPeriodBean definitionPeriodBean = getRenderedObject("inquiryResponsePeriod");

        if (definitionPeriodBean == null) {
            definitionPeriodBean = new InquiryDefinitionPeriodBean();
            definitionPeriodBean.setExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
        }

        InquiryTemplate inquiryTemplate =
                InquiryTemplate.getInquiryTemplateByTypeAndExecutionSemester(definitionPeriodBean.getExecutionPeriod(),
                        definitionPeriodBean.getResponsePeriodType());

        RenderUtils.invalidateViewState();
        if (inquiryTemplate == null) {
            RenderUtils.invalidateViewState();
            request.setAttribute("inquiryDoesntExist", "true");
        } else {
            definitionPeriodBean.setMessage(inquiryTemplate.getInquiryMessage());
            definitionPeriodBean.setBegin(inquiryTemplate.getResponsePeriodBegin());
            definitionPeriodBean.setEnd(inquiryTemplate.getResponsePeriodEnd());
            definitionPeriodBean.setInquiryTemplate(inquiryTemplate);
        }
        request.setAttribute("definitionPeriodBean", definitionPeriodBean);
        return mapping.findForward("showForm");
    }

    public ActionForward changeLanguage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InquiryDefinitionPeriodBean inquiryResponsePeriodMessage = getRenderedObject("inquiryResponsePeriodMessage");

        RenderUtils.invalidateViewState("inquiryResponsePeriodMessage");
        request.setAttribute("definitionPeriodBean", inquiryResponsePeriodMessage);
        return mapping.findForward("showForm");
    }

    public ActionForward define(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InquiryDefinitionPeriodBean inquiryResponsePeriodMessage = getRenderedObject("inquiryResponsePeriodMessage");

        try {
            inquiryResponsePeriodMessage.writePeriodAndMessage();
        } catch (DomainException e) {
            addErrorMessage(request, "error", e.getKey());
            return prepare(mapping, actionForm, request, response);
        }

        addActionMessage(request, "message.inquiry.response.period.defined");

        return prepare(mapping, actionForm, request, response);
    }
}