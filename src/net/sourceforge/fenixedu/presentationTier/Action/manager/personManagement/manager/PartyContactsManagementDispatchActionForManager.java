package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.manager;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "manager", path = "/partyContacts", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "visualizePersonalInformation", path = "/manager/personManagement/viewPerson.jsp"),
		@Forward(name = "editPartyContact", path = "/manager/personManagement/contacts/editPartyContact.jsp"),
		@Forward(name = "createPartyContact", path = "/manager/personManagement/contacts/createPartyContact.jsp") })
public class PartyContactsManagementDispatchActionForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.PartyContactsManagementDispatchAction {
}