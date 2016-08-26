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

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Strings;

@StrutsFunctionality(app = AccountManagementApp.class, path = "manage-accounts",
        titleKey = "link.accountmanagement.manageaccounts")
@Mapping(path = "/accounts/manageAccounts")
@Forward(name = "manageAccounts", path = "/accounts/manageAccounts.jsp")
@Forward(name = "createPerson", path = "/accounts/createPerson.jsp")
@Forward(name = "createPersonFillInfo", path = "/accounts/createPersonFillInfo.jsp")
@Forward(name = "viewPerson", path = "/accounts/viewPerson.jsp")
public class ManageAccountsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward manageAccounts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("searchParameters", new SearchParametersBean());
        return mapping.findForward("manageAccounts");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SearchParametersBean parameters = getRenderedObject("searchParameters");
        request.setAttribute("matches", parameters.search());
        return mapping.findForward("manageAccounts");
    }

    public ActionForward prepareCreatePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("personBean", new PersonBean());
        return mapping.findForward("createPerson");
    }

    public ActionForward showExistentPersonsWithSameMandatoryDetails(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PersonBean bean = getRenderedObject("personBean");
        Collection<Person> results;
        if (!Strings.isNullOrEmpty(bean.getGivenNames()) || !Strings.isNullOrEmpty(bean.getFamilyNames())) {
            String name =
                    Stream.of(bean.getGivenNames(), bean.getFamilyNames()).filter(n -> !Strings.isNullOrEmpty(n))
                            .collect(Collectors.joining(" "));
            Stream<Person> stream = Person.findPersonStream(name, Integer.MAX_VALUE);
            if (!Strings.isNullOrEmpty(bean.getDocumentIdNumber())) {
                stream = stream.filter(p -> p.getDocumentIdNumber().equals(bean.getDocumentIdNumber()));
            }
            results = stream.collect(Collectors.toSet());
        } else if (!Strings.isNullOrEmpty(bean.getDocumentIdNumber())) {
            results = Person.findPersonByDocumentID(bean.getDocumentIdNumber());
        } else {
            results = Collections.emptySet();
        }
        request.setAttribute("resultPersons", results);
        request.setAttribute("createPerson", bean);
        return mapping.findForward("createPerson");
    }

    public ActionForward prepareCreatePersonFillInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("personBean", getRenderedObject("personBean"));
        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward createNewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final PersonBean bean = getRenderedObject();
        try {
            Person person = createAccount(bean);
            SearchParametersBean searchParametersBean = new SearchParametersBean();
            searchParametersBean.setUsername(person.getUsername());
            request.setAttribute("searchParameters", searchParametersBean);
            request.setAttribute("matches", searchParametersBean.search());
            return mapping.findForward("manageAccounts");
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("personBean", bean);
            return mapping.findForward("createPersonFillInfo");
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private Person createAccount(final PersonBean bean) {
        final Person person = new Person(bean);
        person.getPhysicalAddresses().forEach(a -> a.setValid());
        return person;
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PersonBean bean = getRenderedObject();
        request.setAttribute("personBean", bean);
        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward viewPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        return viewPerson(person, mapping, request);
    }

    public ActionForward viewPerson(final Person person, final ActionMapping mapping, final HttpServletRequest request)
            throws Exception {
        request.setAttribute("person", person);
        return mapping.findForward("viewPerson");
    }
}
