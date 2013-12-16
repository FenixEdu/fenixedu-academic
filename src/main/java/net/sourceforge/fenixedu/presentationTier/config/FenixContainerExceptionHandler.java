/*
 * Created on 2004/04/07
 *
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ExceptionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.core.security.Authenticate;

/**
 * 
 * @author Luis Cruz
 */
public class FenixContainerExceptionHandler extends FenixExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(FenixContainerExceptionHandler.class);

    @Override
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
            HttpServletRequest request, HttpServletResponse response) throws ServletException {

        logger.error(ex.getMessage(), ex);

        if (Authenticate.isLogged()) {
            request.setAttribute("loggedPersonEmail", Authenticate.getUser().getPerson().getDefaultEmailAddressValue());
        }

        super.execute(ex, ae, mapping, formInstance, request, response);

        return new ActionForward("error-page", "/showErrorPage.do", false, "/");
    }
}