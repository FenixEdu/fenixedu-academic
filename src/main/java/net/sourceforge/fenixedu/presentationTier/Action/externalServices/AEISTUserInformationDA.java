package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentDataShareAuthorization;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/userForAEIST", scope = "request", parameter = "method")
public class AEISTUserInformationDA extends ExternalInterfaceDispatchAction {

    private static final String USERNAME_KEY = "externalServices.AEIST.username";
    private static final String PASSWORD_KEY = "externalServices.AEIST.password";
    private static final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";
    private static final String USER_DOES_NOT_ALLOW = "USER_DOES_NOT_ALLOW";
    private static final String NOT_ACTIVE_STUDENT = "NOT_ACTIVE_STUDENT";

    private boolean doLogin(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        final String username = (String) getFromRequest(request, "username");
        final String password = (String) getFromRequest(request, "password");
        final String usernameProp = PropertiesManager.getProperty(USERNAME_KEY);
        final String passwordProp = PropertiesManager.getProperty(PASSWORD_KEY);
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(usernameProp)
                || StringUtils.isEmpty(passwordProp)) {
            return false;
        }
        return username.equals(usernameProp) && password.equals(passwordProp);
    }

    public ActionForward getBasicUserData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final JSONObject payload = new JSONObject();
        if (doLogin(mapping, actionForm, request, response)) {
            final String istID = (String) getFromRequest(request, "istID");
            final Person person = Person.readPersonByIstUsername(istID);

            if (person != null) {
                Registration registration = person.getStudent().getLastActiveRegistration();
                if (registration != null) {
                    StudentDataShareAuthorization dataAuthorizationStudentsAssociation =
                            registration.getStudent().getActivePersonalDataAuthorizationStudentsAssociation();
                    if (dataAuthorizationStudentsAssociation != null
                            && dataAuthorizationStudentsAssociation.getAuthorizationChoice().isForStudentsAssociation()) {
                        final JSONObject jsonObject = new JSONObject();
                        jsonObject.put("email", person.getEmailForSendingEmails());
                        jsonObject.put("degree", registration.getDegreeCurricularPlanName());
                        jsonObject.put("name", person.getName());
                        jsonObject.put("birthdate", person.getDateOfBirthYearMonthDay().toString());
                        jsonObject.put("address", person.getAddress());
                        jsonObject.put("locality", person.getParishOfResidence());
                        jsonObject.put("zipCode", person.getPostalCode());
                        jsonObject.put("cellphone", person.getDefaultMobilePhone());
                        jsonObject.put("phone", person.getDefaultPhone());
                        jsonObject.put("nif", person.getSocialSecurityNumber());
                        jsonObject.put("citizenCard/BI", person.getDocumentIdNumber());
                        payload.put("status", SUCCESS_CODE);
                        payload.put("data", jsonObject);
                    } else {
                        payload.put("status", USER_DOES_NOT_ALLOW);
                    }
                } else {
                    payload.put("status", NOT_ACTIVE_STUDENT);
                }
            } else {
                payload.put("status", USER_NOT_FOUND_CODE);
            }
        } else {
            payload.put("status", NOT_AUTHORIZED_CODE);
        }
        writeJSONObject(response, payload);
        return null;
    }
}
