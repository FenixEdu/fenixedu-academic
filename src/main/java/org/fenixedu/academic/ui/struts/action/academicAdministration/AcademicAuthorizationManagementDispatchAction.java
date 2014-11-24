/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.academicAdministration;

import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@StrutsFunctionality(app = AcademicAdministrationApplication.class, path = "authorizations", titleKey = "label.authorizations",
        accessGroup = "academic(MANAGE_AUTHORIZATIONS)")
@Mapping(path = "/authorizations", module = "academicAdministration")
@Forward(name = "listAuthorizations", path = "/academicAdministration/authorizations/authorizations.jsp")
@Forward(name = "manageOperationAuthorization", path = "/academicAdministration/authorizations/authorizationsPerPerson.jsp")
public class AcademicAuthorizationManagementDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward authorizations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        TreeMap<AcademicOperationType, Set<AuthorizationGroupBean>> groups =
                AcademicAccessRule.accessRules().map(AuthorizationGroupBean::new).sorted()
                        .collect(Collectors.groupingBy(r -> r.getRule().getOperation(), TreeMap::new, Collectors.toSet()));
        for (AcademicOperationType operation : AcademicOperationType.values()) {
            if (!groups.containsKey(operation)) {
                groups.put(operation, Collections.emptySet());
            }
        }
        request.setAttribute("groups", groups.entrySet());
        return mapping.findForward("listAuthorizations");
    }

    public ActionForward manageOperation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        AuthorizationsManagementBean bean = getRenderedObject("authorizationsBean");
        if (bean == null) {
            bean = new AuthorizationsManagementBean(AcademicOperationType.valueOf(request.getParameter("operation")));
        }

        if (request.getParameter("removeNewAuthorization") != null) {
            bean.removeAuthorization("-1");
        }

        request.setAttribute("authorizationsBean", bean);

        return mapping.findForward("manageOperationAuthorization");
    }

    public ActionForward addNewAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        AuthorizationsManagementBean bean = getRenderedObject("authorizationsBean");

        bean.addNewAuthorization();

        request.setAttribute("authorizationsBean", bean);

        return mapping.findForward("manageOperationAuthorization");
    }

    public ActionForward deleteAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        AuthorizationsManagementBean bean = getRenderedObject("authorizationsBean");

        bean.removeAuthorization(request.getParameter("oid"));

        request.setAttribute("authorizationsBean", bean);

        return mapping.findForward("manageOperationAuthorization");
    }

    public ActionForward editAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        AuthorizationsManagementBean bean = getRenderedObject("authorizationsBean");

        bean.editAuthorization(request.getParameter("oid"));

        request.setAttribute("authorizationsBean", bean);

        return mapping.findForward("manageOperationAuthorization");
    }

    public ActionForward editAuthorizationPrograms(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        AuthorizationsManagementBean bean = getRenderedObject("authorizationsBean");

        bean.editAuthorizationPrograms(request.getParameter("oid"), request.getParameter("courses"),
                request.getParameter("offices"));

        request.setAttribute("authorizationsBean", bean);

        return mapping.findForward("manageOperationAuthorization");
    }

    public ActionForward createAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        AuthorizationsManagementBean bean = getRenderedObject("authorizationsBean");

        try {
            bean.createAuthorization(request.getParameter("courses"), request.getParameter("offices"));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        request.setAttribute("authorizationsBean", bean);

        return mapping.findForward("manageOperationAuthorization");
    }

    public ActionForward revokeRule(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        AcademicAccessRule rule = getDomainObject(request, "ruleId");
        revoke(rule);
        return authorizations(mapping, actionForm, request, response);
    }

    @Atomic(mode = TxMode.WRITE)
    private void revoke(AcademicAccessRule rule) {
        rule.revoke();
    }
}
