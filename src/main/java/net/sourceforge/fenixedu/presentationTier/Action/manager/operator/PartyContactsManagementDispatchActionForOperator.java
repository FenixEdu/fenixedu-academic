package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.manager.PartyContactsManagementDispatchActionForManager;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "operator", path = "/partyContacts", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "visualizePersonalInformation", path = "/manager/personManagement/viewPerson.jsp"),
        @Forward(name = "editPartyContact", path = "/manager/personManagement/contacts/editPartyContact.jsp"),
        @Forward(name = "createPartyContact", path = "/manager/personManagement/contacts/createPartyContact.jsp") })
public class PartyContactsManagementDispatchActionForOperator extends PartyContactsManagementDispatchActionForManager {

    @Override
    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return new ActionForward("redirectToShowInformation", "/findPerson.do?method=viewPerson&personID="
                + getFromRequest(request, "personID"), false, "/operator");
    }

}
