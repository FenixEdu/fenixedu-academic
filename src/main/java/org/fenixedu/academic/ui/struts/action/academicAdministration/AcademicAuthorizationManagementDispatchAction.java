/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.academicAdministration;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.UnitGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.rules.AccessRule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.Atomic;

@StrutsFunctionality(app = AcademicAdministrationApplication.class, path = "authorizations", titleKey = "label.authorizations",
        accessGroup = "academic(MANAGE_AUTHORIZATIONS)")
@Mapping(path = "/authorizations", module = "academicAdministration")
@Forwards({ @Forward(name = "listAuthorizations", path = "/academicAdministration/authorizations/authorizations.jsp"),
        @Forward(name = "managePartyAuthorization", path = "/academicAdministration/authorizations/authorizationsPerPerson.jsp") })
public class AcademicAuthorizationManagementDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward authorizations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Map<Group, Set<AccessRule>> groups =
                AcademicAccessRule.accessRules().sorted(AcademicAccessRule.COMPARATOR_BY_OPERATION)
                        .collect(Collectors.groupingBy(AccessRule::getWhoCanAccess, Collectors.toSet()));
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

        AcademicAccessRule rule = getDomainObject(request, "groupId");
        Party party = getDomainObject(request, "partyId");

        revokePartyFromGroup(rule, party);

        return authorizations(mapping, actionForm, request, response);
    }

    @Atomic
    private void revokePartyFromGroup(AcademicAccessRule rule, Party party) {
        if (party instanceof Person) {
            rule.revoke(((Person) party).getUser());
        } else {
            rule.changeWhoCanAccess(rule.getWhoCanAccess().or(UnitGroup.recursiveWorkers((Unit) party)));
        }
    }
}
