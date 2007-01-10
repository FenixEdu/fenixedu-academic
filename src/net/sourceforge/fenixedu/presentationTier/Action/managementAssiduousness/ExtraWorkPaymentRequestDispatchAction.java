package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ExtraWorkRequestFactory;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.Partial;

public class ExtraWorkPaymentRequestDispatchAction extends FenixDispatchAction {

    public ActionForward chooseUnitYearMonth(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        ExtraWorkRequestFactory extraWorkRequestFactory = (ExtraWorkRequestFactory) getRenderedObject();
        if (extraWorkRequestFactory == null) {
            extraWorkRequestFactory = new ExtraWorkRequestFactory();
        }
        request.setAttribute("extraWorkRequestFactory", extraWorkRequestFactory);
        return mapping.findForward("prepare-insert-payment-request");
    }

    public ActionForward chooseEmployee(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        ExtraWorkRequestFactory extraWorkRequestFactory = new ExtraWorkRequestFactory(
                getIntegerFromRequest(request, "year"), (String) getFromRequest(request, "month"),
                getIntegerFromRequest(request, "unitCode"), Employee.readByNumber(getIntegerFromRequest(
                        request, "employeeNumber")));
        if (extraWorkRequestFactory.getExtraWorkRequest() != null) {
            request.setAttribute("extraWorkRequestFactory", extraWorkRequestFactory
                    .getExtraWorkRequestFactoryEditor());
        } else {
            request.setAttribute("extraWorkRequestFactory", extraWorkRequestFactory);
        }
        return mapping.findForward("insert-payment-request");
    }

    public ActionForward insertPaymentRequest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        ExtraWorkRequestFactory extraWorkRequestFactory = (ExtraWorkRequestFactory) getRenderedObject();
        if (extraWorkRequestFactory != null) {
            Object result = executeService(request, "ExecuteFactoryMethod", new Object[] { extraWorkRequestFactory });
            if (result != null) {
                ActionMessages actionMessages = getMessages(request);
                actionMessages.add("message", (ActionMessage)result);
                saveMessages(request, actionMessages);
                request.setAttribute("extraWorkRequestFactory", extraWorkRequestFactory);
                return mapping.findForward("insert-payment-request");
            }
            
        }
        // erro
        return chooseUnitYearMonth(mapping, form, request, response);
    }
}