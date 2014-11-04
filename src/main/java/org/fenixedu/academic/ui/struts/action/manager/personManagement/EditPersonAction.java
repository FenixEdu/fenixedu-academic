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
package org.fenixedu.academic.ui.struts.action.manager.personManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.person.SearchPerson;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchParameters;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchPersonPredicate;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerPersonManagementApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@StrutsFunctionality(app = ManagerPersonManagementApp.class, path = "edit-person", titleKey = "edit.person.title")
@Mapping(path = "/editPerson", module = "manager")
@Forwards({ @Forward(name = "searchPersonToEdit", path = "/manager/personManagement/prepareEditPerson.jsp"),
        @Forward(name = "prepareEditPerson", path = "/manager/personManagement/editPerson.jsp") })
public class EditPersonAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSearchPersonToEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        PersonBean personBean = new PersonBean();
        request.setAttribute("personBean", personBean);
        return mapping.findForward("searchPersonToEdit");
    }

    public ActionForward searchPersonToEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        readAndSetValidPersons(request);
        return mapping.findForward("searchPersonToEdit");
    }

    public ActionForward prepareEditPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getDomainObject(request, "personID");
        request.setAttribute("person", person);
        return mapping.findForward("prepareEditPerson");
    }

    private void readAndSetValidPersons(HttpServletRequest request) throws FenixServiceException {
        final IViewState viewState = RenderUtils.getViewState("personBeanID");
        PersonBean personBean = (PersonBean) viewState.getMetaObject().getObject();

        SearchPerson.SearchParameters parameters =
                new SearchParameters(personBean.getName(), null, personBean.getUsername(), personBean.getDocumentIdNumber(),
                        null, null, null, null, null, null, null, (String) null);
        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

        CollectionPager<Person> persons = SearchPerson.runSearchPerson(parameters, predicate);
        request.setAttribute("resultPersons", persons.getCollection());
        request.setAttribute("personBean", personBean);
    }

}
