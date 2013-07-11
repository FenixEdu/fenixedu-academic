package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.personnelSection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "personnelSection", path = "/partyContacts", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "visualizePersonalInformation", path = "/personnelSection/people/viewPerson.jsp", tileProperties = @Tile(
                title = "private.staffarea.interfacegiaf.interfacegiaf.searchpeople")),
        @Forward(name = "editPartyContact", path = "/manager/personManagement/contacts/editPartyContact.jsp",
                tileProperties = @Tile(title = "private.staffarea.interfacegiaf.interfacegiaf.searchpeople")),
        @Forward(name = "createPartyContact", path = "/manager/personManagement/contacts/createPartyContact.jsp",
                tileProperties = @Tile(title = "private.staffarea.interfacegiaf.interfacegiaf.searchpeople")) })
public class PartyContactsManagementDispatchActionForPersonnelSection extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.PartyContactsManagementDispatchAction {

    @Override
    public ActionForward forwardToInputValidationCode(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, PartyContact partyContact) {
        return backToShowInformation(mapping, actionForm, request, response);
    }

    @Override
    protected Party getParty(HttpServletRequest request) {
        final String personID = (String) getFromRequest(request, "personID");
        return FenixFramework.getDomainObject(personID);
    }

}