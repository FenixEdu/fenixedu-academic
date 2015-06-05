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
package org.fenixedu.academic.ui.struts.action.manager.documents;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.documents.GeneratedDocument;
import org.fenixedu.academic.dto.documents.DocumentSearchBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.service.services.person.SearchPerson;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchParameters;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchPersonPredicate;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerSystemManagementApp;
import org.fenixedu.academic.util.CollectionPager;
import org.fenixedu.bennu.io.domain.GenericFile;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

/**
 * @author Pedro Santos (pmrsa)
 */
@StrutsFunctionality(app = ManagerSystemManagementApp.class, path = "generated-documents",
        titleKey = "documents.management.title")
@Mapping(path = "/generatedDocuments", module = "manager")
@Forwards(@Forward(name = "search", path = "/manager/documents/generatedDocuments.jsp"))
public class GeneratedDocumentsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("searchBean", new DocumentSearchBean());
        request.setAttribute("personBean", new PersonBean());
        return mapping.findForward("search");
    }

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DocumentSearchBean searchBean = getRenderedObject();
        List<GeneratedDocument> documents = GenericFile.getFiles(GeneratedDocument.class);
        if (searchBean.hasAddressee()) {
            SearchPerson.SearchParameters parameters =
                    new SearchParameters(searchBean.getAddressee().getName(), null, searchBean.getAddressee().getUsername(),
                            searchBean.getAddressee().getDocumentIdNumber(), null, null, null, null, null, null, null,
                            (String) null);
            SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);
            CollectionPager<Person> persons = SearchPerson.runSearchPerson(parameters, predicate);
            Set<GeneratedDocument> personDocuments = new HashSet<GeneratedDocument>();
            for (Person person : persons.getCollection()) {
                personDocuments.addAll(person.getAddressedDocumentSet());
            }
            documents.retainAll(personDocuments);
        }
        if (searchBean.hasOperator()) {
            SearchPerson.SearchParameters parameters =
                    new SearchParameters(searchBean.getOperator().getName(), null, searchBean.getOperator().getUsername(),
                            searchBean.getOperator().getDocumentIdNumber(), null, null, null, null, null, null, null,
                            (String) null);
            SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);
            CollectionPager<Person> operators = SearchPerson.runSearchPerson(parameters, predicate);
            Set<GeneratedDocument> operatorDocuments = new HashSet<GeneratedDocument>();
            for (Person person : operators.getCollection()) {
                operatorDocuments.addAll(person.getProcessedDocumentSet());
            }
            documents.retainAll(operatorDocuments);
        }
        if (searchBean.getType() != null) {
            Set<GeneratedDocument> typed = new HashSet<GeneratedDocument>();
            for (GeneratedDocument document : documents) {
                if (document.getType() == searchBean.getType()) {
                    typed.add(document);
                }
            }
            documents.retainAll(typed);
        }
        if (searchBean.getCreationDate() != null) {
            Set<GeneratedDocument> dated = new HashSet<GeneratedDocument>();
            for (GeneratedDocument document : documents) {
                if (!document.getCreationDate().toLocalDate().isBefore(searchBean.getCreationDate())) {
                    dated.add(document);
                }
            }
            documents.retainAll(dated);
        }
        // Collections.sort
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("documents", documents);
        return mapping.findForward("search");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PersonBean personBean = getRenderedObject();
        SearchPerson.SearchParameters parameters =
                new SearchParameters(personBean.getName(), null, personBean.getUsername(), personBean.getDocumentIdNumber(),
                        null, null, null, null, null, null, null, (String) null);
        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);
        CollectionPager<Person> persons = SearchPerson.runSearchPerson(parameters, predicate);
        request.setAttribute("resultPersons", persons.getCollection());
        request.setAttribute("personBean", personBean);
        return mapping.findForward("search");
    }

    public ActionForward selectPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getPersonFromParameter(request);
        request.setAttribute("login", person.getUser());
        request.setAttribute("documents", person.getAddressedDocumentSet());
        return mapping.findForward("search");
    }

    private Person getPersonFromParameter(HttpServletRequest request) {
        return getDomainObject(request, "personID");
    }
}
