package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.config.ExceptionConfig;

public class FenixDomainExceptionHandler extends FenixExceptionHandler {

    public ActionForward execute(Exception ex, ExceptionConfig exceptionConfig, ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws ServletException {

	ActionForward forward = mapping.getInputForward();

	if (ex instanceof DomainException) {
	    super.execute(ex, exceptionConfig, mapping, actionForm, request, response);

	    DomainException domainException = (DomainException) ex;
	    String property = domainException.getKey();
	    ActionMessage error = new ActionMessage(domainException.getKey(), domainException.getArgs());
	    super.storeException(request, property, error, forward, exceptionConfig.getScope());
	}
	return forward;
    }

}
