package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryDefinitionPeriodBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTemplate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(
		module = "gep",
		path = "/defineResponsePeriods",
		input = "/defineResponsePeriods.do?method=prepare&page=0",
		attribute = "defineResponsePeriodsForm",
		formBean = "defineResponsePeriodsForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "showForm", path = "/gep/inquiries/defineResponsePeriods.jsp", tileProperties = @Tile(
		title = "private.gep.surveys.setperiodsofresponse")) })
public class DefineResponsePeriodsDA extends FenixDispatchAction {

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