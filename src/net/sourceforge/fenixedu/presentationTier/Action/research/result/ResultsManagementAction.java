package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ResultsManagementAction extends FenixDispatchAction {

    public ActionForward prepareCreateEditResult(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm actionForm = (DynaActionForm)form;
        final String action = request.getParameter("action");
        final String authorsIdStr = (String) actionForm.get("authorsIdStr");
        List<Person> authorsList = new ArrayList<Person>();
        Integer resultId = -1;
        
        if (action.equals("createPatent") || action.equals("createPublication")) {
            if(authorsIdStr.length() == 0 || authorsIdStr == null) {
                authorsList.add(getUserView(request).getPerson());    
            }
            else {
                authorsList = getFormAuthorsList(form);
            }
        }
        
        if (action.equals("editPatent") || action.equals("editPublication")) {
            resultId = Integer.valueOf(request.getParameter("resultId"));
            if(authorsIdStr.length() == 0 || authorsIdStr == null) {
                final Result result = (Result) rootDomainObject.readResultByOID(resultId);
                authorsList = result.getAuthorships();    
            }
            else {
                authorsList = getFormAuthorsList(form);
            }
        }

        request.setAttribute("action", action);
        request.setAttribute("resultId", resultId);
        request.setAttribute("authorsList", authorsList);
        request.setAttribute("authorsIdStr", getAuthorsIdsStr(authorsList));

        return mapping.findForward("manageAuthors");
    }

    public ActionForward insertNewAuthor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm actionForm = (DynaActionForm) form;
        List<Person> authorsList = getFormAuthorsList(form);
        final Person newPerson = (Person) rootDomainObject.readPartyByOID(Integer.valueOf(request
                .getParameter("oid")));

        if (!authorsList.contains(newPerson)) {
            authorsList.add(newPerson);
        }

        actionForm.set("authorsIdStr", getAuthorsIdsStr(authorsList));

        return prepareCreateEditResult(mapping, actionForm, request, response);
    }

    public List<Person> getFormAuthorsList(ActionForm form) {
        DynaActionForm actionForm = (DynaActionForm) form;
        StringTokenizer authorsStrTemp = new StringTokenizer((String) actionForm.get("authorsIdStr"),
                ",");
        List<Person> authorsList = new ArrayList<Person>();

        while (authorsStrTemp.hasMoreTokens()) {
            Person person = (Person) rootDomainObject.readPartyByOID(Integer.valueOf(authorsStrTemp
                    .nextToken()));
            authorsList.add(person);
        }
        return authorsList;
    }

    public ActionForward removeAuthor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm actionForm = (DynaActionForm) form;
        List<Person> authorsList = getFormAuthorsList(form);
        String[] authorIdsToRemove = request.getParameterValues("authorIds");

        if (authorIdsToRemove != null) {
            for (int i = 0; i < authorIdsToRemove.length; i++) {
                Person removePerson = (Person) rootDomainObject.readPartyByOID(Integer
                        .valueOf(authorIdsToRemove[i]));
                if (authorsList.size() > 1 && removePerson != null) {
                    authorsList.remove(removePerson);
                }
            }
        }

        actionForm.set("authorsIdStr", getAuthorsIdsStr(authorsList));

        return prepareCreateEditResult(mapping, form, request, response);
    }

    public String getAuthorsIdsStr(List<Person> authorsList) {
        StringBuilder authorsIdStr = new StringBuilder();
        int i = 0;

        if (authorsList != null && !authorsList.isEmpty()) {
            for (Person person : authorsList) {
                authorsIdStr.append(person.getIdInternal().toString());
                if (i != (authorsList.size() - 1)) {
                    authorsIdStr.append(",");
                }
                i++;
            }
        }

        return authorsIdStr.toString();
    }

    public ActionForward authorsInserted(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm actionForm = (DynaActionForm) form;
        final String backToAction = (String) actionForm.get("action");
        
        if (backToAction.equals("createPatent") || backToAction.equals("editPatent")) {
            return mapping.findForward("createEditPatent");
        }
        
        // This is not supposed to happen
        return mapping.findForward("index");
    }

    public ActionForward cancelManagement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm actionForm = (DynaActionForm) form;
        final String backToAction = (String) actionForm.get("action");
        
        if (backToAction.equals("createPatent") || backToAction.equals("editPatent")) {
            return mapping.findForward("listPatents");
        }
        if (backToAction.equals("createPublication") || backToAction.equals("editPublication")) {
            return mapping.findForward("listPublications");
        }

        // This is not supposed to happen
        return mapping.findForward("index");
    }
    
    public ActionForward changeAuthorsOrder(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm actionForm = (DynaActionForm) form;
        final Person person = (Person) rootDomainObject.readPartyByOID(Integer.valueOf(request
                .getParameter("oid")));
        List<Person> authorsList = getFormAuthorsList(form);

        final Integer newPosition = authorsList.indexOf(person)
                + Integer.valueOf(request.getParameter("offset"));

        if (newPosition >= 0 && newPosition < authorsList.size() && person != null) {
            authorsList.remove(person);
            authorsList.add(newPosition, person);
        }

        actionForm.set("authorsIdStr", getAuthorsIdsStr(authorsList));

        return prepareCreateEditResult(mapping, form, request, response);
    }

    public ActionForward searchPersons(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm actionForm = (DynaActionForm) form;
        final String name = (String) actionForm.get("nameToSearch");
        List<Person> allPersons = Person.readAllPersons();

        SearchParameters searchParameters = new SearchPerson.SearchParameters(name, null, null, null,
                null, null, null, null);
        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);
        allPersons = (List<Person>) CollectionUtils.select(allPersons, predicate);

        if (allPersons.size() >= 20) {
            request.setAttribute("floodedList", "floodedList");
        }

        actionForm.set("nameToSearch", name);

        request.setAttribute("personsMatchList", allPersons.subList(0, Math.min(20, allPersons.size())));

        return prepareCreateEditResult(mapping, form, request, response);
    }
}