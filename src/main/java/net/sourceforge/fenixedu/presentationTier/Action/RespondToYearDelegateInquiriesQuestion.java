package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/respondToYearDelegateInquiriesQuestion")
public class RespondToYearDelegateInquiriesQuestion extends FenixDispatchAction {

    public final ActionForward showQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DelegateInquiryTemplate inquiryTemplate = DelegateInquiryTemplate.getCurrentTemplate();
        request.setAttribute("executionPeriod", inquiryTemplate == null ? null : inquiryTemplate.getExecutionPeriod());

        return new ActionForward("/respondToYearDelegateInquiriesQuestion.jsp");
    }

    private ActionForward forward(final String path) {
        final ActionForward actionForward = new ActionForward();
        actionForward.setPath(path);
        actionForward.setRedirect(true);
        return actionForward;
    }

    public final ActionForward respondLater(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return forward("/home.do");
    }

    public final ActionForward respondNow(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String path =
                "/delegate/delegateInquiry.do?method=showCoursesToAnswerPage&page=0&"
                        + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                        + "=/delegado/delegado";
        return forward(path
                + "&_request_checksum_="
                + pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.calculateChecksum(request
                        .getContextPath() + path));
    }

}