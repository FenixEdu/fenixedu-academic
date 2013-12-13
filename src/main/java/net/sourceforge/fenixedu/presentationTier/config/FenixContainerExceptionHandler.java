/*
 * Created on 2004/04/07
 *
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.LogLevel;
import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

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

        ex.printStackTrace();
        if (LogLevel.ERROR) {
            if (!(ex instanceof FenixServiceException) && !(ex instanceof FenixActionException)) {
                logger.error(ex.getMessage(), ex);
            }
        }

        final User userView = Authenticate.getUser();
        if (userView != null) {
            final Person person = userView.getPerson();
            request.setAttribute("loggedPersonEmail", person.getEmail());
        }

        super.execute(ex, ae, mapping, formInstance, request, response);
        throw new ServletException(ex);
    }
}