package net.sourceforge.fenixedu.presentationTier.Action.manager.documents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.documents.DocumentSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocument;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author Pedro Santos (pmrsa)
 */
@Mapping(path = "/generatedDocuments", module = "manager")
@Forwards({ @Forward(name = "search", path = "/manager/documents/generatedDocuments.jsp") })
public class GeneratedDocumentsDA extends FenixDispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("searchBean", new DocumentSearchBean());
        request.setAttribute("personBean", new PersonBean());
        return mapping.findForward("search");
    }

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DocumentSearchBean searchBean = getRenderedObject();
        List<GeneratedDocument> documents = new ArrayList<GeneratedDocument>();
        for (File file : rootDomainObject.getFilesSet()) {
            if (file instanceof GeneratedDocument) {
                documents.add((GeneratedDocument) file);
            }
        }
        if (searchBean.hasAddressee()) {
            SearchPerson.SearchParameters parameters =
                    new SearchParameters(searchBean.getAddressee().getName(), null, searchBean.getAddressee().getUsername(),
                            searchBean.getAddressee().getDocumentIdNumber(), null, null, null, null, null, null, null, null,
                            (String) null);
            SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);
            CollectionPager<Person> persons =
                    (CollectionPager<Person>) executeService("SearchPerson", new Object[] { parameters, predicate });
            Set<GeneratedDocument> personDocuments = new HashSet<GeneratedDocument>();
            for (Person person : persons.getCollection()) {
                personDocuments.addAll(person.getAddressedDocumentSet());
            }
            documents.retainAll(personDocuments);
        }
        if (searchBean.hasOperator()) {
            SearchPerson.SearchParameters parameters =
                    new SearchParameters(searchBean.getOperator().getName(), null, searchBean.getOperator().getUsername(),
                            searchBean.getOperator().getDocumentIdNumber(), null, null, null, null, null, null, null, null,
                            (String) null);
            SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);
            CollectionPager<Person> operators =
                    (CollectionPager<Person>) executeService("SearchPerson", new Object[] { parameters, predicate });
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
        if (searchBean.getUploadTime() != null) {
            Set<GeneratedDocument> dated = new HashSet<GeneratedDocument>();
            for (GeneratedDocument document : documents) {
                if (!document.getUploadTime().toLocalDate().isBefore(searchBean.getUploadTime())) {
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
                        null, null, null, null, null, null, null, null, (String) null);
        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);
        CollectionPager<Person> persons =
                (CollectionPager<Person>) executeService("SearchPerson", new Object[] { parameters, predicate });
        request.setAttribute("resultPersons", persons.getCollection());
        request.setAttribute("personBean", personBean);
        return mapping.findForward("search");
    }

    public ActionForward selectPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getPersonFromParameter(request);
        request.setAttribute("login", person.getLoginIdentification());
        request.setAttribute("documents", person.getAddressedDocumentSet());
        return mapping.findForward("search");
    }

    private Person getPersonFromParameter(HttpServletRequest request) {
        String personIDString = request.getParameter("personID");
        return (Person) ((StringUtils.isEmpty(personIDString)) ? null : rootDomainObject.readPartyByOID(Integer
                .valueOf(personIDString)));
    }
}
