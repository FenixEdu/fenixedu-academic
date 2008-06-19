package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeExtraWorkRequestFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ExtraWorkRequestFactory;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ExtraWorkPaymentRequestDispatchAction extends FenixDispatchAction {

    public ActionForward chooseUnitYearMonth(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	ExtraWorkRequestFactory extraWorkRequestFactory = (ExtraWorkRequestFactory) getRenderedObject();
	if (extraWorkRequestFactory == null) {
	    extraWorkRequestFactory = new ExtraWorkRequestFactory();
	} else {
	    if (extraWorkRequestFactory.getUnit() == null) {
		ActionMessages actionMessages = getMessages(request);
		actionMessages.add("message", new ActionMessage("message.inexistentCostCenter"));
		saveMessages(request, actionMessages);
	    } else {
		extraWorkRequestFactory.reloadEmployeeExtraWorkRequest();
	    }
	}
	request.setAttribute("extraWorkRequestFactory", extraWorkRequestFactory);
	return mapping.findForward("prepare-insert-payment-request");
    }

    public ActionForward chooseEmployee(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	ExtraWorkRequest extraWorkRequest = null;
	if (!request.getParameter("idInternal").equalsIgnoreCase("null")) {
	    Integer idInternal = Integer.parseInt(request.getParameter("idInternal"));
	    extraWorkRequest = (ExtraWorkRequest) rootDomainObject.readDomainObjectByOID(ExtraWorkRequest.class, idInternal);
	}
	ExtraWorkRequestFactory extraWorkRequestFactory = new ExtraWorkRequestFactory(getIntegerFromRequest(request, "year"),
		(String) getFromRequest(request, "month"), getIntegerFromRequest(request, "unitCode"), getIntegerFromRequest(
			request, "doneInYear"), (String) getFromRequest(request, "doneInMonth"));
	EmployeeExtraWorkRequestFactory employeeExtraWorkRequestFactory = null;
	if (extraWorkRequest != null) {
	    employeeExtraWorkRequestFactory = new EmployeeExtraWorkRequestFactory(extraWorkRequest, extraWorkRequestFactory);
	} else {
	    employeeExtraWorkRequestFactory = new EmployeeExtraWorkRequestFactory(Employee.readByNumber(getIntegerFromRequest(
		    request, "employeeNumber")), extraWorkRequestFactory);
	}

	request.setAttribute("employeeExtraWorkRequestFactory", employeeExtraWorkRequestFactory);

	return mapping.findForward("insert-payment-request");
    }

    public ActionForward processPayments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	ExtraWorkRequestFactory extraWorkRequestFactory = (ExtraWorkRequestFactory) getRenderedObject();
	if (request.getParameter("cancelPayment") != null) {
	    extraWorkRequestFactory.setPerformPayment(false);
	} else {
	    extraWorkRequestFactory.setPerformPayment(true);
	}
	Object result = executeService(request, "ExecuteFactoryMethod", new Object[] { extraWorkRequestFactory });
	if (result != null) {
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("message", (ActionMessage) result);
	    saveMessages(request, actionMessages);
	}
	extraWorkRequestFactory.reloadEmployeeExtraWorkRequest();

	request.setAttribute("extraWorkRequestFactory", extraWorkRequestFactory);
	return mapping.findForward("prepare-insert-payment-request");
    }

    public ActionForward deleteExtraWorkRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	Integer extraWorkRequestID = getIdInternal(request, "idInternal");
	executeService(request, "DeleteExtraWorkRequest", new Object[] { extraWorkRequestID });
	ExtraWorkRequestFactory extraWorkRequestFactory = new ExtraWorkRequestFactory(getIntegerFromRequest(request, "year"),
		(String) getFromRequest(request, "month"), getIntegerFromRequest(request, "unitCode"), getIntegerFromRequest(
			request, "doneInYear"), (String) getFromRequest(request, "doneInMonth"));
	request.setAttribute("extraWorkRequestFactory", extraWorkRequestFactory);
	return mapping.findForward("prepare-insert-payment-request");
    }

    public ActionForward insertPaymentRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	EmployeeExtraWorkRequestFactory employeeExtraWorkRequestFactory = (EmployeeExtraWorkRequestFactory) getRenderedObject();
	Object result = executeService(request, "ExecuteFactoryMethod", new Object[] { employeeExtraWorkRequestFactory });

	if (result != null) {
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("message", (ActionMessage) result);
	    saveMessages(request, actionMessages);
	    request.setAttribute("employeeExtraWorkRequestFactory", employeeExtraWorkRequestFactory);
	    return mapping.findForward("insert-payment-request");
	}
	employeeExtraWorkRequestFactory.getExtraWorkRequestFactory().reloadEmployeeExtraWorkRequest();
	request.setAttribute("extraWorkRequestFactory", employeeExtraWorkRequestFactory.getExtraWorkRequestFactory());
	return mapping.findForward("prepare-insert-payment-request");
    }
}