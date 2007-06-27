/**
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.BaseAuthenticationAction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 12:08:43,13/Out/2005
 * @version $Id$
 */
public class Authentication extends FenixAction {
    final static String allowedProtocol = "https";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final String requestURL = request.getRequestURL().toString();
        boolean result = false;              
        final String remoteHostName = BaseAuthenticationAction.getRemoteHostName(request);
        Object argsAutenticacao[] = { username, password, requestURL, remoteHostName };
        try {
            String scheme = request.getScheme();

            if (allowedProtocol.equalsIgnoreCase(scheme)) {

                Person person = Person.readPersonByUsername(username);
                if (person == null) {
                    person = Person.readPersonByIstUsername(username);
                }
                if (person != null) {
                	ServiceUtils.executeService(null, "SetUserUID", new Object[] { person } );
                }

                ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsAutenticacao);
                result = true;
            }

        } catch (Exception e) {
            result = false;
        }

        try {
            sendAnswer(response, result);
        } catch (IOException ex) {
            throw new FenixActionException(ex);
        }

        return null;
    }

    private void sendAnswer(HttpServletResponse response, boolean result) throws IOException {
        ServletOutputStream writer = response.getOutputStream();
        response.setContentType("text/plain");
        writer.print(result);
        writer.flush();
        response.flushBuffer();
    }

}
