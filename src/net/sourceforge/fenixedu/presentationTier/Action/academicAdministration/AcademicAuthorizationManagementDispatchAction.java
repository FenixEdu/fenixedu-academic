package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accessControl.PersistentAccessGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.PersistentAcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/authorizations", module = "academicAdministration")
@Forwards({ @Forward(name = "listAuthorizations", path = "/academicAdministration/authorizations/authorizations.jsp"),
	@Forward(name = "managePartyAuthorization", path = "/academicAdministration/authorizations/authorizationsPerPerson.jsp") })
public class AcademicAuthorizationManagementDispatchAction extends FenixDispatchAction {

    public ActionForward authorizations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Map<Party, Set<PersistentAcademicAuthorizationGroup>> groups = new HashMap<Party, Set<PersistentAcademicAuthorizationGroup>>();
	for (PersistentAccessGroup group : rootDomainObject.getPersistentAccessGroupSet()) {
	    if (group instanceof PersistentAcademicAuthorizationGroup) {
		for (Party member : group.getMemberSet()) {
		    if (!groups.containsKey(member)) {
			groups.put(member, new HashSet<PersistentAcademicAuthorizationGroup>());
		    }
		    groups.get(member).add((PersistentAcademicAuthorizationGroup) group);
		}
	    }
	}
	request.setAttribute("groups", groups.entrySet());
	request.setAttribute("authorizationsBean", new AuthorizationsManagementBean());
	return mapping.findForward("listAuthorizations");
    }

    public ActionForward managePartyAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	AuthorizationsManagementBean bean = getRenderedObject("authorizationsBean");
	if (bean == null) {
	    Party party = getDomainObject(request, "partyId");
	    bean = new AuthorizationsManagementBean();
	    bean.setParty(party);
	}

	if (request.getParameter("removeNewAuthorization") != null) {
	    bean.removeAuthorization("-1");
	}

	request.setAttribute("managementBean", bean);

	return mapping.findForward("managePartyAuthorization");
    }

    public ActionForward addNewAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	AuthorizationsManagementBean bean = getRenderedObject("managementBean");

	bean.addNewAuthorization();

	request.setAttribute("managementBean", bean);

	return mapping.findForward("managePartyAuthorization");
    }

    public ActionForward deleteAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	AuthorizationsManagementBean bean = getRenderedObject("managementBean");

	bean.removeAuthorization(request.getParameter("oid"));

	request.setAttribute("managementBean", bean);

	return mapping.findForward("managePartyAuthorization");
    }

    public ActionForward editAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	AuthorizationsManagementBean bean = getRenderedObject("managementBean");

	bean.editAuthorization(request.getParameter("oid"));

	request.setAttribute("managementBean", bean);

	return mapping.findForward("managePartyAuthorization");
    }

    public ActionForward editAuthorizationPrograms(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	AuthorizationsManagementBean bean = getRenderedObject("managementBean");

	bean.editAuthorizationPrograms(request.getParameter("oid"), request.getParameter("courses"),
		request.getParameter("offices"));

	request.setAttribute("managementBean", bean);

	return mapping.findForward("managePartyAuthorization");
    }

    public ActionForward createAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	AuthorizationsManagementBean bean = getRenderedObject("managementBean");

	try {
	    bean.createAuthorization(request.getParameter("courses"), request.getParameter("offices"));
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	}

	request.setAttribute("managementBean", bean);

	return mapping.findForward("managePartyAuthorization");
    }

    public ActionForward removePartyFromGroup(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	PersistentAcademicAuthorizationGroup group = getDomainObject(request, "groupId");
	Party party = getDomainObject(request, "partyId");

	revokePartyFromGroup(group, party);

	return authorizations(mapping, actionForm, request, response);
    }

    @Service
    private void revokePartyFromGroup(PersistentAcademicAuthorizationGroup group, Party party) {
	if (group.getMember().size() > 1) {
	    group.revoke(party);
	} else
	    group.delete();
    }

}
