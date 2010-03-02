package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PartyContactsManagementDispatchAction extends
	net.sourceforge.fenixedu.presentationTier.Action.person.PartyContactsManagementDispatchAction {

    @Override
    protected Party getParty(final HttpServletRequest request) {
	return rootDomainObject.fromExternalId(request.getParameter("personID"));

    }

    @Override
    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	PartyContactBean contact = (PartyContactBean) getRenderedObject("edit-contact");
	if (contact != null) {
	    request.setAttribute("person", contact.getParty());
	} else {
	    request.setAttribute("person", getParty(request));
	}

	return mapping.findForward("visualizePersonalInformation");
    }
}
