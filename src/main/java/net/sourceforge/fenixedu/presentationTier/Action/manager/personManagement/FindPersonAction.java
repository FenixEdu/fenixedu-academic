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
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerPersonManagementApp;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@StrutsFunctionality(app = ManagerPersonManagementApp.class, path = "find-person", titleKey = "label.manager.findPerson")
@Mapping(path = "/findPerson", module = "manager")
@Forwards({ @Forward(name = "findPerson", path = "/manager/personManagement/findPerson.jsp"),
        @Forward(name = "displayPerson", path = "/manager/personManagement/displayPerson.jsp"),
        @Forward(name = "viewPerson", path = "/manager/personManagement/viewPerson.jsp") })
public class FindPersonAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareFindPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("findPerson");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String name = getStringFromRequest(request, "name");
        request.setAttribute("name", name);

        String email = getStringFromRequest(request, "email");
        request.setAttribute("email", email);

        String username = getStringFromRequest(request, "username");
        request.setAttribute("username", username);

        String documentIdNumber = getStringFromRequest(request, "documentIdNumber");
        request.setAttribute("documentIdNumber", documentIdNumber);

        String mechanoGraphicalNumber = getStringFromRequest(request, "mechanoGraphicalNumber");
        request.setAttribute("mechanoGraphicalNumber", mechanoGraphicalNumber);

        SearchParameters searchParameters =
                new SearchPerson.SearchParameters(name, email, username, documentIdNumber, null, null, null, null, null, null,
                        null, null, (String) null);

        if ((mechanoGraphicalNumber != null) && (mechanoGraphicalNumber.length() > 0)) {
            searchParameters.setMechanoGraphicalNumber(Integer.parseInt(mechanoGraphicalNumber));
        }

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        CollectionPager<Person> result = SearchPerson.runSearchPerson(searchParameters, predicate);

        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);
        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("numberOfPages", Integer.valueOf(result.getNumberOfPages()));
        request.setAttribute("personListFinded", result.getPage(pageNumber.intValue()));
        request.setAttribute("totalFindedPersons", result.getCollection().size());

        return mapping.findForward("displayPerson");
    }

    public ActionForward viewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("person", getDomainObject(request, "personID"));
        return mapping.findForward("viewPerson");
    }

}
