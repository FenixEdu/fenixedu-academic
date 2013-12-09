package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentDataShareAuthorization;
import net.sourceforge.fenixedu.domain.student.StudentDataShareStudentsAssociationAuthorization;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/userForAEIST", scope = "request", parameter = "method")
public class AEISTUserInformationDA extends ExternalInterfaceDispatchAction {

    private static final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";
    private static final String USER_DOES_NOT_ALLOW = "USER_DOES_NOT_ALLOW";
    private static final String NOT_ACTIVE_STUDENT = "NOT_ACTIVE_STUDENT";

    private boolean doLogin(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        final String username = (String) getFromRequest(request, "username");
        final String password = (String) getFromRequest(request, "password");
        final String usernameProp = FenixConfigurationManager.getConfiguration().getExternalServicesAEISTUsername();
        final String passwordProp = FenixConfigurationManager.getConfiguration().getExternalServicesAEISTPassword();
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
            final Person person = Person.readPersonByUsername(istID);

            if (person != null) {
                Registration registration = person.getStudent().getLastActiveRegistration();
                if (registration != null) {
                    boolean allowAccess = false;
                    StudentDataShareStudentsAssociationAuthorization dataStudentsAssociationAuthorization =
                            registration.getStudent().getStudentPersonalDataStudentsAssociationAuthorization();
                    if (dataStudentsAssociationAuthorization != null
                            && dataStudentsAssociationAuthorization.getAuthorizationChoice().isForStudentsAssociation()) {
                        allowAccess = true;
                    } else {
                        StudentDataShareAuthorization dataAuthorizationStudentsAssociation =
                                registration.getStudent().getActivePersonalDataAuthorization();
                        allowAccess =
                                dataAuthorizationStudentsAssociation != null
                                        && dataAuthorizationStudentsAssociation.getAuthorizationChoice()
                                                .isForStudentsAssociation();
                    }
                    if (allowAccess) {
                        final JSONObject jsonObject = new JSONObject();
                        jsonObject.put("email", person.getEmailForSendingEmails());
                        jsonObject.put("degree", registration.getLastDegreeCurricularPlan().getDegree().getSigla());
                        jsonObject.put("name", person.getName());
                        jsonObject.put("birthdate", person.getDateOfBirthYearMonthDay().toString());
                        jsonObject.put("address", person.getAddress());
                        jsonObject.put("locality", person.getParishOfResidence());
                        jsonObject.put("zipCode", person.getPostalCode());
                        jsonObject.put("cellphone", person.getDefaultMobilePhoneNumber());
                        jsonObject.put("phone", person.getDefaultPhoneNumber());
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
