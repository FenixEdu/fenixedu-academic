package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonByUsernameOrIstUsername;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/retrieveUserInformation", scope = "request", parameter = "method")
public class RetrieveUserInformation extends ExternalInterfaceDispatchAction {

    private static final String SUCCESS_CODE = "SUCCESS";

    private static final String UNEXPECTED_ERROR_CODE = "UNEXPECTED_ERROR";

    private static final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";

    private static final String NOT_AUTHORIZED_CODE = "NOT_AUTHORIZED";

    private static final String ENCODING = CharEncoding.UTF_8;

    public ActionForward getUserEmailAndUniqueUsername(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (!FenixConfigurationManager.getHostAccessControl().isAllowed(this, request)) {
            writeResponse(response, NOT_AUTHORIZED_CODE, "");
        } else {
            final String username = request.getParameter("username");

            String responseMessage = "";
            String responseCode = null;

            try {
                final Person person = ReadPersonByUsernameOrIstUsername.run(username);

                if (person == null || StringUtils.isEmpty(person.getIstUsername())) {
                    responseCode = USER_NOT_FOUND_CODE;
                } else {
                    responseCode = SUCCESS_CODE;

                    final String uniqueUsername = person.getIstUsername();
                    final String email = String.format("%s@ist.utl.pt", uniqueUsername);
                    responseMessage =
                            "email=" + URLEncoder.encode(email, ENCODING) + "&" + "uniqueUsername="
                                    + URLEncoder.encode(uniqueUsername, ENCODING);
                }
            } catch (FenixServiceException e) {
                responseCode = UNEXPECTED_ERROR_CODE;
            }

            writeResponse(response, responseCode, responseMessage);
        }
        return null;
    }

    /**
     * Retrieves user information and encodes as CSV like format: First row:
     * Comma separated list of columns Next rows: User information (respecting
     * previously defined colums)
     * 
     * @return
     */
    public String getPersonInformationAsCSV() {
        // TODO: finish
        return null;
    }

}