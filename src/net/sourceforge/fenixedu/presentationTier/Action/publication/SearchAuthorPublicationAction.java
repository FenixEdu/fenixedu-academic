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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

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

            Object[] args = { newStringAuthorSearch };

            List authors = (List) ServiceUtils.executeService(userView, "ReadAuthorsByName", args);

            List persons = (List) ServiceUtils.executeService(userView, "ReadPersonsNotAuthors", args);

            authors = removeFromAuthorsTheAuthorsInserted(authors, authorsIds);

            persons = removeFromPersonsTheAuthorsInserted(persons, authorsIds, userView);

            List newAuthors = infoAuthorsPersons(authors, Person.class);

            List newPersons = infoAuthorsPersons(persons, Person.class);

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


    private List infoAuthorsPersons(List listObjects, Class clazz) {
        List infoAuthorPersons = new ArrayList();

        if (clazz.equals(Person.class)) {

            infoAuthorPersons = (List) CollectionUtils.collect(listObjects, new Transformer() {
                public Object transform(Object o) {
                    IPerson author = (IPerson) o;
                    InfoPerson infoPerson = new InfoPerson();
                    infoPerson.copyFromDomain(author);
                    return infoPerson;
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

    private List joinAuthorsAndPersons(List authors, List persons) {
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

    private List sortListByName(List infoAuthorpersons) {

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator(PublicationConstants.BEAN_COMPARATOR_NAME));
        Collections.sort(infoAuthorpersons, comparatorChain);

        return infoAuthorpersons;

    }

    private List readInfoAuthors(List authorsIds, IUserView userView) throws FenixServiceException, FenixFilterException {

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
                IPerson author = (IPerson) o;
                InfoPerson infoPerson = new InfoPerson();
                infoPerson.copyFromDomain(author);
                return infoPerson;
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

            IPerson author = (IPerson) iteratorAuthors.next();
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
                IPerson author = (IPerson) authorsIterator.next();

                if (author.getIdInternal() != null
                        && author.getIdInternal().intValue() != PublicationConstants.ZERO_VALUE) {
                    if (author.getIdInternal().intValue() == person.getIdInternal().intValue()) {
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