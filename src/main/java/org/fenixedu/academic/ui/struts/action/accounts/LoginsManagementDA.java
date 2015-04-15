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
package org.fenixedu.academic.ui.struts.action.accounts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.service.services.person.SearchPerson;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchParameters;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchPersonPredicate;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.CollectionPager;
import org.fenixedu.bennu.core.domain.UserLoginPeriod;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = AccountManagementApp.class, path = "logins", titleKey = "logins.management.title")
@Mapping(module = "manager", path = "/loginsManagement")
@Forwards({ @Forward(name = "prepareManageLoginTimeIntervals", path = "/manager/loginsManagement/manageLoginTimeIntervals.jsp"),
        @Forward(name = "prepareCreateNewLoginTimeInterval", path = "/manager/loginsManagement/createNewLoginPeriod.jsp"),
        @Forward(name = "prepareSearchPerson", path = "/manager/loginsManagement/searchPersonToManageLogins.jsp"),
        @Forward(name = "prepareEditLoginTimeInterval", path = "/manager/loginsManagement/editLoginPeriod.jsp") })
public class LoginsManagementDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("personBean", new PersonBean());
        return mapping.findForward("prepareSearchPerson");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState("personBeanID");
        PersonBean personBean = (PersonBean) viewState.getMetaObject().getObject();

        SearchPerson.SearchParameters parameters =
                new SearchParameters(personBean.getName(), null, personBean.getUsername(), personBean.getDocumentIdNumber(),
                        null, null, null, null, null, null, null, (String) null);
        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

        CollectionPager<Person> persons = SearchPerson.runSearchPerson(parameters, predicate);

        request.setAttribute("resultPersons", persons.getCollection());
        request.setAttribute("personBean", personBean);
        return mapping.findForward("prepareSearchPerson");
    }

    public ActionForward prepareManageLoginTimeIntervals(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Person person = getPersonFromParameter(request);
        request.setAttribute("person", person);
        return mapping.findForward("prepareManageLoginTimeIntervals");
    }

    private Person getPersonFromParameter(HttpServletRequest request) {
        String personIDString = request.getParameter("personID");
        return StringUtils.isEmpty(personIDString) ? null : FenixFramework.<Person> getDomainObject(personIDString);
    }

    public ActionForward prepareEditLoginTimeInterval(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("period", getDomainObject(request, "periodID"));
        return mapping.findForward("prepareEditLoginTimeInterval");
    }

    public ActionForward prepareCreateLoginTimeInterval(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("user", getDomainObject(request, "userID"));
        return mapping.findForward("prepareCreateNewLoginTimeInterval");
    }

    public ActionForward deleteLoginTimeInterval(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UserLoginPeriod period = getDomainObject(request, "periodID");
        request.setAttribute("person", period.getUser().getPerson());
        deletePeriod(period);
        return mapping.findForward("prepareManageLoginTimeIntervals");
    }

    @Atomic
    private void deletePeriod(UserLoginPeriod period) {
        period.delete();
    }

}