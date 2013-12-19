package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeMapping;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeMapping.PaymentCodeMappingBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/paymentCodesAttribution", module = "manager")
@Forwards({ @Forward(name = "viewCodes", path = "/manager/payments/codes/viewCodes.jsp"),
        @Forward(name = "createPaymentCodeMapping", path = "/manager/payments/codes/createPaymentCodeMapping.jsp") })
public class PaymentCodesAttributionDA extends FenixDispatchAction {

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
            request.setAttribute("paymentCodeMappings", bean.getExecutionInterval().getPaymentCodeMappings());
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

        request.setAttribute("paymentCodeMappings", bean.getExecutionInterval().getPaymentCodeMappings());
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
        request.setAttribute("paymentCodeMappings", bean.getExecutionInterval().getPaymentCodeMappings());

        try {
            codeMapping.delete();
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        return mapping.findForward("viewCodes");
    }
}