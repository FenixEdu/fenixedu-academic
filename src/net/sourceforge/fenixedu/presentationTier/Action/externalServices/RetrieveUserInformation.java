package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RetrieveUserInformation extends ExternalInterfaceDispatchAction {

    private static final String SUCCESS_CODE = "SUCCESS";

    private static final String UNEXPECTED_ERROR_CODE = "UNEXPECTED_ERROR";

    private static final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";

    private static final String NOT_AUTHORIZED_CODE = "NOT_AUTHORIZED";

    private static final String ENCODING = "UTF-8";

    public ActionForward getUserEmailAndUniqueUsername(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (!HostAccessControl.isAllowed(this, request)) {
	    writeResponse(response, NOT_AUTHORIZED_CODE, "");
	} else {

	    final String username = request.getParameter("username");

	    String responseMessage = "";
	    String responseCode = null;

	    try {
		final Person person = (Person) ServiceUtils.executeService("ReadPersonByUsernameOrIstUsername",
			new Object[] { username });

		if (person == null) {
		    responseCode = USER_NOT_FOUND_CODE;
		} else {
		    responseCode = SUCCESS_CODE;

		    String email = (person.getEmail() != null) ? person.getEmail() : "";
		    String uniqueUsername = (person.getIstUsername() != null) ? person.getIstUsername() : "";

		    responseMessage = "email=" + URLEncoder.encode(email, ENCODING) + "&" + "uniqueUsername="
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
