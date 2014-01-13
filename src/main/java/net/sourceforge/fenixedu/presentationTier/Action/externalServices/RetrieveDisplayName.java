package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/retrieveDisplayName", scope = "request", parameter = "method")
public class RetrieveDisplayName extends ExternalInterfaceDispatchAction {

    public ActionForward check(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String responseMessage;
        if (FenixConfigurationManager.getHostAccessControl().isAllowed(this, request)) {
            final String username = request.getParameter("username");
            responseMessage = getNickname(username);
        } else {
            responseMessage = "";
        }

        response.setContentType("text/plain");

        final OutputStream outputStream = response.getOutputStream();
        outputStream.write(responseMessage.getBytes());
        outputStream.close();

        return null;
    }

    private String getNickname(final String username) {
        return username == null || username.isEmpty() ? "" : getNickname(User.findByUsername(username));
    }

    private String getNickname(final User user) {
        return user == null ? "" : getNickname(user.getPerson());
    }

    private String getNickname(final Person person) {
        return person == null ? "" : person.getNickname();
    }

}
