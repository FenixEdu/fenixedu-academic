/*
 * Created on May 31, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.presentationTier.Action.publication;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;

public class SearchAuthorPublicationAction extends FenixDispatchAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        IUserView userView = SessionUtils.getUserView(request);

        Integer publicationTypeId = new Integer(request.getParameter("infoPublicationTypeId"));

        String typePublication = (String) dynaForm.get("typePublication");

        Integer idTeacher = (Integer) dynaForm.get("teacherId");

        String[] list = (String[]) dynaForm.get("authorsIds");
        List newList = Arrays.asList(list);
        List authorsIds = new ArrayList();
        authorsIds.addAll(newList);

        ActionForward actionForward = null;

        Object args[] = { publicationTypeId };
        IPublicationType publicationType = (IPublicationType) ServiceUtils.executeService(userView,
                "ReadPublicationType", args);

        if (session != null) {

            List infoAuthors = readInfoAuthors(authorsIds, userView);

            request.setAttribute("infoAuthorsList", infoAuthors);
            dynaForm.set("infoPublicationTypeId", publicationTypeId);
            dynaForm.set("typePublication", typePublication);
            dynaForm.set("teacherId", idTeacher);

            if (publicationType.getPublicationType().equalsIgnoreCase("Unstructured"))
                actionForward = mapping.findForward("show-attributes");
            else
                actionForward = mapping.findForward("show-search-author-form");
        }

        return actionForward;
    }

    public ActionForward searchAuthor(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        ActionForward actionForward = mapping.findForward("show-search-author-form");

        IUserView userView = SessionUtils.getUserView(request);

        Integer publicationTypeId = (Integer) dynaForm.get("infoPublicationTypeId");

        String[] list = (String[]) dynaForm.get("authorsIds");
        List newList = Arrays.asList(list);
        List authorsIds = new ArrayList();
        authorsIds.addAll(newList);

        String typePublication = (String) dynaForm.get("typePublication");

        Integer idTeacher = (Integer) dynaForm.get("teacherId");

        String stringAuthorSearch = request.getParameter("searchAuthorString");

        if (stringAuthorSearch == null || stringAuthorSearch.length() == PublicationConstants.ZERO_VALUE) {

            List infoAuthors = readInfoAuthors(authorsIds, userView);
            request.setAttribute("infoAuthorsList", infoAuthors);
            dynaForm.set("infoPublicationTypeId", publicationTypeId);
            dynaForm.set("typePublication", typePublication);
            dynaForm.set("teacherId", idTeacher);

            ActionErrors errors = new ActionErrors();
            errors.add("nonValidating", new ActionError("message.publication.notSearchFilled"));
            saveErrors(request, errors);
            return actionForward;
        }
        String newStringAuthorSearch = PublicationConstants.CONCATE_STRING_TO_SEARCH
                + stringAuthorSearch + PublicationConstants.CONCATE_STRING_TO_SEARCH;

        if (session != null) {

            Object[] args = { newStringAuthorSearch, userView };

            List authors = (List) ServiceUtils.executeService(userView, "ReadAuthors", args);

            List persons = (List) ServiceUtils.executeService(userView, "ReadPersonsNotAuthors", args);

            authors = removeFromAuthorsTheAuthorsInserted(authors, authorsIds);

            persons = removeFromPersonsTheAuthorsInserted(persons, authorsIds, userView);

            List newAuthors = infoAuthorsPersons(authors, new Author());

            List newPersons = infoAuthorsPersons(persons, new Person());

            List infoAuthorsPersons = joinAuthorsAndPersons(newAuthors, newPersons);

            List finalInfoAuthorsPersons = sortListByName(infoAuthorsPersons);

            List infoAuthors = readInfoAuthors(authorsIds, userView);

            request.setAttribute("infoAuthorsList", infoAuthors);
            request.setAttribute("infoAuthorsPersons", finalInfoAuthorsPersons);
            dynaForm.set("infoPublicationTypeId", publicationTypeId);
            dynaForm.set("typePublication", typePublication);
            dynaForm.set("teacherId", idTeacher);
        }

        return actionForward;
    }

    public ActionForward insertAuthorsInPublication(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        IUserView userView = SessionUtils.getUserView(request);

        Integer publicationTypeId = (Integer) dynaForm.get("infoPublicationTypeId");

        List authorsPersonskeys = Arrays.asList((String[]) dynaForm.get("authorsPersonsCodes"));

        String typePublication = (String) dynaForm.get("typePublication");

        Integer idTeacher = (Integer) dynaForm.get("teacherId");

        String[] list = (String[]) dynaForm.get("authorsIds");
        List newList = Arrays.asList(list);
        List authorsIds = new ArrayList();
        authorsIds.addAll(newList);

        ActionForward actionForward = null;

        if (session != null) {

            List infoAuthors = getInfoAuthors(userView, authorsPersonskeys);

            List infoAuthorsInserteds = readInfoAuthors(authorsIds, userView);

            infoAuthors.addAll(infoAuthorsInserteds);

            request.setAttribute("infoAuthorsList", infoAuthors);

            dynaForm.set("infoPublicationTypeId", publicationTypeId);
            dynaForm.set("typePublication", typePublication);
            dynaForm.set("teacherId", idTeacher);
            actionForward = mapping.findForward("show-search-author-form");
        }

        return actionForward;
    }

    public ActionForward deleteAuthorInPublication(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        IUserView userView = SessionUtils.getUserView(request);

        Integer publicationTypeId = (Integer) dynaForm.get("infoPublicationTypeId");

        String[] list = (String[]) dynaForm.get("authorsIds");
        List newList = Arrays.asList(list);
        List authorsIds = new ArrayList();
        authorsIds.addAll(newList);

        String[] idsToDelete = (String[]) dynaForm.get("authorsIdstoDelete");
        List authorsIdsToDelete = Arrays.asList(idsToDelete);

        String typePublication = (String) dynaForm.get("typePublication");

        Integer idTeacher = (Integer) dynaForm.get("teacherId");

        ActionForward actionForward = null;

        if (session != null) {

            Iterator iterator = authorsIdsToDelete.iterator();
            while (iterator.hasNext()) {
                String authorIdToDelete = (String) iterator.next();
                authorsIds.remove(authorIdToDelete);
            }

            List infoAuthors = readInfoAuthors(authorsIds, userView);

            request.setAttribute("infoAuthorsList", infoAuthors);
            dynaForm.set("infoPublicationTypeId", publicationTypeId);
            dynaForm.set("typePublication", typePublication);
            dynaForm.set("teacherId", idTeacher);
            actionForward = mapping.findForward("show-search-author-form");
        }

        return actionForward;
    }

    public ActionForward insertAuthorInPublication(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        ActionForward actionForward = mapping.findForward("show-search-author-form");

        IUserView userView = SessionUtils.getUserView(request);

        boolean validate = false;

        Integer publicationTypeId = (Integer) dynaForm.get("infoPublicationTypeId");

        String[] list = (String[]) dynaForm.get("authorsIds");
        List newList = Arrays.asList(list);
        List authorsIds = new ArrayList();
        authorsIds.addAll(newList);

        String typePublication = (String) dynaForm.get("typePublication");

        Integer idTeacher = (Integer) dynaForm.get("teacherId");

        String stringName = request.getParameter("authorName");

        ActionErrors errors = new ActionErrors();

        if (stringName == null || stringName.length() == 0) {
            errors.add("nonValidatingName", new ActionError("message.publication.notNameFilled"));
            validate = true;
        }

        String stringOrganisation = request.getParameter("authorOrganisation");

        if (stringOrganisation == null || stringOrganisation.length() == 0) {
            errors.add("nonValidatingOrganisation", new ActionError(
                    "message.publication.notOrganisationFilled"));
            validate = true;
        }

        List infoAuthors = new ArrayList();
        if (validate) {
            infoAuthors = readInfoAuthors(authorsIds, userView);
            request.setAttribute("infoAuthorsPersons", new ArrayList());
            saveErrors(request, errors);
        } else {
            if (session != null) {

                Object[] args1 = { stringName, stringOrganisation };

                IAuthor author = (IAuthor) ServiceUtils.executeService(userView, "InsertAuthor", args1);

                authorsIds.add(author.getIdInternal().toString());
                infoAuthors = readInfoAuthors(authorsIds, userView);

            }
        }

        request.setAttribute("infoAuthorsList", infoAuthors);
        dynaForm.set("infoPublicationTypeId", publicationTypeId);
        dynaForm.set("typePublication", typePublication);
        dynaForm.set("teacherId", idTeacher);

        return actionForward;
    }

    public List infoAuthorsPersons(List listObjects, Object object) {
        List infoAuthorPersons = new ArrayList();

        if (object instanceof Author) {

            infoAuthorPersons = (List) CollectionUtils.collect(listObjects, new Transformer() {
                public Object transform(Object o) {
                    IAuthor author = (IAuthor) o;
                    return Cloner.copyIAuthor2InfoAuthorperson(author);
                }
            });

        } else {

            infoAuthorPersons = (List) CollectionUtils.collect(listObjects, new Transformer() {
                public Object transform(Object o) {
                    IPerson person = (IPerson) o;
                    return Cloner.copyIPerson2InfoAuthorPerson(person);
                }
            });
        }

        return infoAuthorPersons;

    }

    public List joinAuthorsAndPersons(List authors, List persons) {
        List authorsPersons = new ArrayList();

        if ((authors == null || authors.size() == PublicationConstants.ZERO_VALUE)
                && (persons == null || persons.size() == PublicationConstants.ZERO_VALUE)) {
            return authorsPersons;
        }

        if (authors == null || authors.size() == PublicationConstants.ZERO_VALUE) {
            authorsPersons = persons;
        }
        if (persons == null || persons.size() == PublicationConstants.ZERO_VALUE) {
            authorsPersons = authors;
        } else {
            authorsPersons = persons;
            authorsPersons.addAll(authors);
        }

        return authorsPersons;
    }

    public List sortListByName(List infoAuthorpersons) {

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator(PublicationConstants.BEAN_COMPARATOR_NAME));
        Collections.sort(infoAuthorpersons, comparatorChain);

        return infoAuthorpersons;

    }

    public List getInfoAuthors(IUserView userView, List authorsPersonsKey) throws FenixServiceException, FenixFilterException {
        List infoAuthors = new ArrayList();

        List authorsIds = new ArrayList();
        List personsIds = new ArrayList();

        Iterator iterator = authorsPersonsKey.iterator();

        while (iterator.hasNext()) {

            String keyIdInternal = (String) iterator.next();
            Integer size = new Integer(keyIdInternal.length());
            char charToChoose = keyIdInternal.charAt(size.intValue() - PublicationConstants.ONE_VALUE);
            String stringIdInternal = keyIdInternal.substring(PublicationConstants.ZERO_VALUE, size
                    .intValue()
                    - PublicationConstants.ONE_VALUE);
            Integer idInternal = new Integer(stringIdInternal);

            if (charToChoose == PublicationConstants.CHAR_INIT_PERSON) {
                personsIds.add(idInternal);
            } else {
                authorsIds.add(idInternal);
            }
        }

        Object[] args = { personsIds };

        List personAuthors = (List) ServiceUtils.executeService(userView, "InsertNewAuthorsAsPersons",
                args);

        Object[] args1 = { authorsIds };
        List authors = (List) ServiceUtils.executeService(userView, "ReadAuthorsToInsert", args1);

        authors.addAll(personAuthors);

        infoAuthors = (List) CollectionUtils.collect(authors, new Transformer() {
            public Object transform(Object o) {
                IAuthor author = (IAuthor) o;
                return Cloner.copyIAuthor2InfoAuthor(author);
            }
        });
        return infoAuthors;
    }

    public List readInfoAuthors(List authorsIds, IUserView userView) throws FenixServiceException, FenixFilterException {

        List newAuthorsIds = new ArrayList();
        Iterator iteratorIds = authorsIds.iterator();

        while (iteratorIds.hasNext()) {
            String idString = (String) iteratorIds.next();
            newAuthorsIds.add(new Integer(idString));
        }

        Object[] args = { newAuthorsIds };
        List authors = (List) ServiceUtils.executeService(userView, "ReadAuthorsToInsert", args);

        List infoAuthors = (List) CollectionUtils.collect(authors, new Transformer() {
            public Object transform(Object o) {
                IAuthor author = (IAuthor) o;
                return Cloner.copyIAuthor2InfoAuthor(author);
            }
        });
        return infoAuthors;
    }

    /**
     * @param authors
     * @param authorsIds
     * @return
     */
    private List removeFromAuthorsTheAuthorsInserted(List authors, List authorsIds) {
        List newAuthors = new ArrayList();

        Boolean contains;
        Iterator iteratorAuthors = authors.iterator();
        Iterator iteratorAuthorsIds;

        while (iteratorAuthors.hasNext()) {

            IAuthor author = (IAuthor) iteratorAuthors.next();
            iteratorAuthorsIds = authorsIds.iterator();
            contains = Boolean.FALSE;
            while (iteratorAuthorsIds.hasNext()) {
                String authorIdString = (String) iteratorAuthorsIds.next();
                Integer authorId = new Integer(authorIdString);

                if (authorId.intValue() == author.getIdInternal().intValue()) {
                    contains = Boolean.TRUE;
                }
            }
            if (!contains.booleanValue()) {
                newAuthors.add(author);
            }

        }

        return newAuthors;
    }

    /**
     * @param persons
     * @param authorsIds
     * @return
     */
    private List removeFromPersonsTheAuthorsInserted(List persons, List authorsIds, IUserView userView)
            throws FenixServiceException, FenixFilterException {
        // TODO Auto-generated method stub

        List newAuthorsIds = new ArrayList();
        Iterator iteratorIds = authorsIds.iterator();

        while (iteratorIds.hasNext()) {
            String idString = (String) iteratorIds.next();
            newAuthorsIds.add(new Integer(idString));
        }

        Object[] args = { newAuthorsIds };

        List authors = (List) ServiceUtils.executeService(userView, "ReadAuthorsToInsert", args);

        List newPersons = new ArrayList();

        Boolean contains;
        Iterator authorsIterator;
        Iterator personsIterator = persons.iterator();

        while (personsIterator.hasNext()) {
            IPerson person = (IPerson) personsIterator.next();
            authorsIterator = authors.iterator();
            contains = Boolean.FALSE;

            while (authorsIterator.hasNext()) {
                IAuthor author = (IAuthor) authorsIterator.next();

                if (author.getKeyPerson() != null
                        && author.getKeyPerson().intValue() != PublicationConstants.ZERO_VALUE) {
                    if (author.getKeyPerson().intValue() == person.getIdInternal().intValue()) {
                        contains = Boolean.TRUE;
                    }
                }
            }

            if (!contains.booleanValue()) {
                newPersons.add(person);
            }
        }

        return newPersons;
    }

}