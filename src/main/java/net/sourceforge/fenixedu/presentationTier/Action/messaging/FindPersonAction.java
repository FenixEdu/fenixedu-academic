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
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.MessagingApplication.MessagingSearchApp;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@StrutsFunctionality(app = MessagingSearchApp.class, path = "find-person", titleKey = "label.person.findPerson")
@Forwards(@Forward(name = "findPerson", path = "/messaging/findPerson.jsp"))
@Mapping(path = "/findPerson", module = "messaging")
public class FindPersonAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareFindPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        FindPersonBean bean = new FindPersonBean();
        request.setAttribute("bean", bean);
        return mapping.findForward("findPerson");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        FindPersonBean bean = getRenderedObject();

        boolean fromRequest = true;
        String name = request.getParameter("name");
        String roleType = null;
        String departmentId = null;
        String degreeId = null;
        String degreeType = null;

        if (bean != null && name == null) {
            fromRequest = false;
            name = bean.getName();
            roleType = bean.getRoleType() == null ? null : bean.getRoleType().toString();
            departmentId = bean.getDepartmentExternalId();
            degreeId = bean.getDegreeExternalId();
            degreeType = bean.getDegreeType() == null ? null : bean.getDegreeType().toString();
        } else {
            roleType = request.getParameter("roleType");

            departmentId = request.getParameter("departmentId");

            degreeId = request.getParameter("degreeId");
            degreeType = request.getParameter("degreeType");
        }

        if (name == null) {
            // error
        }

        SearchParameters searchParameters =
                new SearchPerson.SearchParameters(name, null, null, null, null, roleType, degreeType, degreeId, departmentId,
                        Boolean.TRUE, null, Boolean.FALSE, (String) null);

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        CollectionPager<Person> result = SearchPerson.runSearchPerson(searchParameters, predicate);

        if (result == null) {
            addErrorMessage(request, "impossibleFindPerson", "error.manager.implossible.findPerson");
            return mapping.findForward("findPerson");
        }

        if (result.getCollection().isEmpty()) {
            addErrorMessage(request, "impossibleFindPerson", "error.manager.implossible.findPerson");
            return mapping.findForward("findPerson");
        }

        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);

        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("numberOfPages", Integer.valueOf(result.getNumberOfPages()));

        request.setAttribute("personListFinded", result.getPage(pageNumber.intValue()));
        request.setAttribute("totalFindedPersons", result.getCollection().size());

        request.setAttribute("name", name);
        request.setAttribute("roleType", roleType == null ? "" : roleType);
        request.setAttribute("degreeId", degreeId == null ? "" : degreeId.toString());
        request.setAttribute("degreeType", degreeType == null ? "" : degreeType.toString());
        request.setAttribute("departmentId", departmentId == null ? "" : departmentId.toString());

        if (isEmployeeOrTeacher(userView)) {
            request.setAttribute("show", Boolean.TRUE);
        } else {
            request.setAttribute("show", Boolean.FALSE);
        }

        Boolean viewPhoto = null;
        if (request.getParameter("viewPhoto") != null && request.getParameter("viewPhoto").length() > 0) {
            viewPhoto = getCheckBoxValue(request.getParameter("viewPhoto"));
        } else if (bean.getViewPhoto() != null) {
            viewPhoto = bean.getViewPhoto();
        }

        request.setAttribute("viewPhoto", viewPhoto);

        if (fromRequest) {
            bean = new FindPersonBean();
            bean.setName(name);
            bean.setViewPhoto(viewPhoto);
            if (!StringUtils.isEmpty(roleType)) {
                bean.setRoleType(RoleType.valueOf(roleType));
            }
            if (!StringUtils.isEmpty(degreeId)) {
                bean.setDegree(FenixFramework.<Degree> getDomainObject(degreeId));
            }
            if (!StringUtils.isEmpty(degreeType)) {
                bean.setDegreeType(DegreeType.valueOf(degreeType));
            }
            if (!StringUtils.isEmpty(departmentId)) {
                bean.setDepartment(FenixFramework.<Department> getDomainObject(departmentId));
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("bean", bean);
        return mapping.findForward("findPerson");
    }

    private boolean isEmployeeOrTeacher(User userView) {
        return userView.getPerson().hasRole(RoleType.EMPLOYEE) || userView.getPerson().hasRole(RoleType.TEACHER);
    }

    private Boolean getCheckBoxValue(String value) {
        if (value != null && (value.equals("true") || value.equals("yes") || value.equals("on"))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public ActionForward postback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        FindPersonBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("bean", bean);
        return mapping.findForward("findPerson");
    }

}