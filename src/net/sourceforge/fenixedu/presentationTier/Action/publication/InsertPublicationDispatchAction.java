/*
 * Created on 9/Dez/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.publication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAttribute;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
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

/**
 * @author Ricardo G?es
 * 
 */
public class InsertPublicationDispatchAction extends FenixDispatchAction {

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        // InfoPublication pub = (InfoPublication) infoObject;
        Integer keyPublicationType = (Integer) dynaForm.get("infoPublicationTypeId");

        Object[] argReqAtt = { userView.getUtilizador(), keyPublicationType };
        List requiredAttributes = (List) ServiceUtils.executeService(userView, "ReadRequiredAttributes",
                argReqAtt);

        // VALIDATION CODE EXTRACTED FROM PFON'S OLD VALIDATION
        ActionErrors errors = new ActionErrors();
        Iterator iter = requiredAttributes.iterator();
        while (iter.hasNext()) {
            InfoAttribute infoAttribute = (InfoAttribute) iter.next();
            if (!infoAttribute.getAttributeType().equals(
                    PublicationConstants.DIDATIC_PEDAGOGIC_TO_COMPARE)
                    && !infoAttribute.getAttributeType().equals(
                            PublicationConstants.AUTHOR_NAME_TO_COMPARE)) {
                Object object = dynaForm.get(infoAttribute.getAttributeType());
                if (object instanceof String) {
                    String valueString = (String) object;
                    if (valueString == null || valueString.length() == 0) {
                        errors.add(infoAttribute.getAttributeType(), new ActionError(
                                "message.publicationAttribute.notVAlidate."
                                        + infoAttribute.getAttributeType()));
                    }
                } else {
                    Integer valueInteger = (Integer) object;
                    if (valueInteger == null || valueInteger.intValue() == 0) {
                        errors.add(infoAttribute.getAttributeType(), new ActionError(
                                "message.publicationAttribute.notVAlidate."
                                        + infoAttribute.getAttributeType()));

                    }
                }
            }
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        List infoAuthors = new ArrayList();
        InfoPublication publication = new InfoPublication();

        Object[] arrayId = (Object[]) dynaForm.get("authorsId");
        Object[] arrayName = (Object[]) dynaForm.get("authorsName");

        for (int i = 0; i < arrayId.length; i++) {
            if (((Integer) arrayId[i]).equals(new Integer(-1))) {
                // external author
                InfoAuthor author = new InfoAuthor();
                String[] array = ((String) arrayName[i]).split("'.'");
                author.setAuthor(array[0]);
                author.setOrganization(array[1]);
                // not actually required but explicit so the service will know this is an external author
                author.setKeyPerson(null);
                infoAuthors.add(author);
            } else {
                Object[] argReadAuthor = { arrayId[i] };
                InfoAuthor author = (InfoAuthor) ServiceUtils.executeService(userView, "ReadAuthorById",
                        argReadAuthor);
                InfoPerson person = (InfoPerson) ServiceUtils.executeService(userView, "ReadPerson",
                        new Object[] { author.getKeyPerson() });
                author.setInfoPessoa(person);
                infoAuthors.add(author);
            }
        }

        publication.setInfoPublicationAuthors(infoAuthors);

        publication.setTitle((String) dynaForm.get("title"));
        // ERROR this is wrong... a domain object is up here :(

        InfoPublicationType type = InfoPublicationType.newInfoFromDomain((IPublicationType) ServiceUtils
                .executeService(userView, "ReadPublicationType", new Object[] { keyPublicationType }));
        HashMap attributes = (HashMap) ServiceUtils.executeService(userView,
                "ReadAttributesByPublicationType", new Object[] { keyPublicationType });

        publication.setInfoPublicationType(type);
        publication.setPublicationType(type.getPublicationType());
        publication.setKeyPublicationType(keyPublicationType);

        if (attributes.get("subtype") != null)
            publication.setSubType((String) dynaForm.get("subtype"));
        if (attributes.get("journalName") != null)
            publication.setJournalName((String) dynaForm.get("journalName"));
        if (attributes.get("volume") != null)
            publication.setVolume((String) dynaForm.get("volume"));
        if (attributes.get("firstPage") != null)
            publication.setFirstPage((Integer) dynaForm.get("firstPage"));
        if (attributes.get("lastPage") != null)
            publication.setLastPage((Integer) dynaForm.get("lastPage"));
        if (attributes.get("language") != null)
            publication.setLanguage((String) dynaForm.get("language"));
        if (attributes.get("format") != null)
            publication.setFormat((String) dynaForm.get("format"));
        if (attributes.get("observation") != null)
            publication.setObservation((String) dynaForm.get("observation"));
        if (attributes.get("number") != null)
            publication.setNumber((Integer) dynaForm.get("number"));
        if (attributes.get("month") != null)
            publication.setMonth((String) dynaForm.get("month"));
        if (attributes.get("year") != null)
            publication.setYear((String) dynaForm.get("year"));
        if (attributes.get("month_end") != null)
            publication.setMonth_end((String) dynaForm.get("month_end"));
        if (attributes.get("year_end") != null)
            publication.setYear_end((String) dynaForm.get("year_end"));
        if (attributes.get("editor") != null)
            publication.setEditor((String) dynaForm.get("editor"));
        if (attributes.get("country") != null)
            publication.setCountry((String) dynaForm.get("country"));
        if (attributes.get("issn") != null)
            publication.setIssn((Integer) dynaForm.get("issn"));
        if (attributes.get("scope") != null)
            publication.setScope((String) dynaForm.get("scope"));
        if (attributes.get("url") != null)
            publication.setUrl((String) dynaForm.get("url"));
        if (attributes.get("editorCity") != null)
            publication.setEditorCity((String) dynaForm.get("editorCity"));
        if (attributes.get("numberPages") != null)
            publication.setNumberPages((Integer) dynaForm.get("numberPages"));
        if (attributes.get("edition") != null) {
            String edition = (String) dynaForm.get("edition");
            Integer ed = 0;
            if (edition.equals(""))
                ed = new Integer(0);
            else
                try {
                    ed = Integer.valueOf(edition);
                } catch (NumberFormatException nfe) {
                    errors.add("Edição",new ActionError("message.publicationAttribute.notValidate.value.edition"));
                    dynaForm.set("edition","");
                }
            publication.setEdition(ed);
        }
        if (attributes.get("fascicle") != null) {
            String fascicle = (String) dynaForm.get("fascicle");
            Integer fas = 0;
            if (fascicle.equals(""))
                fas = new Integer(0);
            else
                try {
                    fas = Integer.valueOf(fascicle);
                } catch (NumberFormatException nfe) {
                    errors.add("Fascículo",new ActionError("message.publicationAttribute.notValidate.value.fascicle"));
                    dynaForm.set("fascicle","");
                }
            publication.setFascicle(fas);
        }
        if (attributes.get("serie") != null) {
            String serie = (String) dynaForm.get("serie");
            Integer ser = 0;
            if (serie.equals(""))
                ser = new Integer(0);
            else
                try {
                    ser = Integer.valueOf(serie);
                } catch (NumberFormatException nfe) {
                    errors.add("Série",new ActionError("message.publicationAttribute.notValidate.value.serie"));
                    dynaForm.set("serie","");
                }
            publication.setSerie(ser);
        }
        if (attributes.get("isbn") != null)
            publication.setIsbn((Integer) dynaForm.get("isbn"));
        if (attributes.get("local") != null)
            publication.setLocal((String) dynaForm.get("local"));
        if (attributes.get("conference") != null)
            publication.setConference((String) dynaForm.get("conference"));
        if (attributes.get("instituition") != null)
            publication.setInstituition((String) dynaForm.get("instituition"));
        if (attributes.get("originalLanguage") != null)
            publication.setOriginalLanguage((String) dynaForm.get("originalLanguage"));
        if (attributes.get("translatedAuthor") != null)
            publication.setTranslatedAuthor((String) dynaForm.get("translatedAuthor"));
        if (attributes.get("criticizedAuthor") != null)
            publication.setCriticizedAuthor((String) dynaForm.get("criticizedAuthor"));
        // publication.setPublicationType((String)dynaForm.get("publicationType"));
        if (attributes.get("university") != null)
            publication.setUniversity((String) dynaForm.get("university"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("input");
        }
        
        Object[] argInsertPublication = { publication };
        ServiceUtils.executeService(userView, "InsertPublication", argInsertPublication);

        return mapping.findForward("done");
    }    

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        DynaActionForm insertPublicationForm = (DynaActionForm) form;

        Integer type = (Integer) insertPublicationForm.get("infoPublicationTypeId");
        if (type == null || type.intValue() <= 0) {
            // select the first from the publication type list
            type = new Integer(1);
            insertPublicationForm.set("infoPublicationTypeId", type);
        }

        IUserView userView = SessionUtils.getUserView(request);
        Object argPubType[] = { userView.getUtilizador() };
        List infoPublicationTypes = (List) ServiceUtils.executeService(userView, "ReadPublicationTypes",
                argPubType);
        request.setAttribute("publicationTypesList", infoPublicationTypes);

        Object args[] = { userView.getUtilizador(), type };
        List infoPublicationSubtypes = (List) ServiceUtils.executeService(userView,
                "ReadPublicationSubtypes", args);
        request.setAttribute("subTypeList", infoPublicationSubtypes);

        List infoPublicationFormats = (List) ServiceUtils.executeService(userView,
                "ReadPublicationFormats", args);
        request.setAttribute("formatList", infoPublicationFormats);

        List monthList = (List) ServiceUtils.executeService(userView, "ReadPublicationMonths", args);
        request.setAttribute("monthList", monthList);

        List scopeList = (List) ServiceUtils.executeService(userView, "ReadPublicationScopes", args);
        request.setAttribute("scopeList", scopeList);

        Integer[] authorsId = (Integer[]) insertPublicationForm.get("authorsId");
        if (authorsId.length == 0) {
            InfoPerson infoPerson = (InfoPerson) ServiceUtils.executeService(userView,
                    "ReadPersonByUserview", new Object[] { userView });
            InfoAuthor infoAuthor = (InfoAuthor) ServiceUtils.executeService(userView,
                    "ReadAuthorByPersonId", new Object[] { userView.getUtilizador() });

            Integer[] newAuthorsId = new Integer[1];
            String[] newAuthorsName = new String[1];

            newAuthorsId[newAuthorsId.length - 1] = infoAuthor.getIdInternal();
            newAuthorsName[newAuthorsName.length - 1] = infoPerson.getNome();

            insertPublicationForm.set("authorsId", newAuthorsId);
            insertPublicationForm.set("authorsName", newAuthorsName);

            insertPublicationForm.set("creatorId", infoAuthor.getIdInternal());

        }

        return mapping.findForward("insert");
    }

    public ActionForward externalAuthor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        DynaActionForm insertPublicationForm = (DynaActionForm) form;

        ActionErrors errors = new ActionErrors();
        String name = (String) insertPublicationForm.get("authorName");
        if(name == null || name.equals("")){
            errors.add("error1", new ActionError("message.publication.missingAuthor"));
        }
        String org = (String) insertPublicationForm.get("authorOrganization");
        if(org == null || org.equals("")){
            errors.add("error2", new ActionError("message.publication.missingOrganization"));
        }
        
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("searchAuthors");
        }
        String nameAndOrg = name + "'.'" + org;

        Integer[] authorsId = (Integer[]) insertPublicationForm.get("authorsId");
        String[] authorsName = (String[]) insertPublicationForm.get("authorsName");

        Integer[] newAuthorsId = new Integer[authorsId.length + 1];
        String[] newAuthorsName = new String[authorsName.length + 1];
        for (int i = 0; i < authorsId.length; i++) {
            newAuthorsId[i] = authorsId[i];
            newAuthorsName[i] = authorsName[i];
        }

        newAuthorsId[newAuthorsId.length - 1] = new Integer(-1);
        newAuthorsName[newAuthorsName.length - 1] = nameAndOrg;

        insertPublicationForm.set("authorsId", newAuthorsId);
        insertPublicationForm.set("authorsName", newAuthorsName);

        prepare(mapping, form, request, response);

        return mapping.findForward("insert");
    }

    public ActionForward up(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        reorderAuthorsList(form, -1);
        prepare(mapping, form, request, response);

        return mapping.findForward("insert");

    }

    public ActionForward down(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        reorderAuthorsList(form, 1);
        prepare(mapping, form, request, response);

        return mapping.findForward("insert");
    }

    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        DynaActionForm insertPublicationForm = (DynaActionForm) form;

        int index = ((Integer) insertPublicationForm.get("index")).intValue();

        Object[] arrayId = (Object[]) insertPublicationForm.get("authorsId");
        Object[] arrayName = (Object[]) insertPublicationForm.get("authorsName");

        Integer[] newArrayId = new Integer[arrayId.length - 1];
        String[] newArrayName = new String[arrayId.length - 1];

        for (int i = 0, j = 0; i < arrayId.length;) {
            if (i == index) {
                i++;
                continue;
            }
            newArrayId[j] = (Integer) arrayId[i];
            newArrayName[j] = (String) arrayName[i];
            i++;
            j++;
        }

        insertPublicationForm.set("authorsId", newArrayId);
        insertPublicationForm.set("authorsName", newArrayName);

        prepare(mapping, form, request, response);

        return mapping.findForward("insert");
    }

    /**
     * @param form
     * @param direction
     * @param o
     */
    private void reorderArray(Object[] o, int index, int direction) {

        Object tmpId = o[index + direction];
        o[index + direction] = o[index];
        o[index] = tmpId;

    }

    /**
     * @param form
     * @param direction
     */
    private void reorderAuthorsList(ActionForm form, int direction) {
        DynaActionForm insertPublicationForm = (DynaActionForm) form;

        int index = ((Integer) insertPublicationForm.get("index")).intValue();

        reorderArray((Object[]) insertPublicationForm.get("authorsId"), index, direction);
        reorderArray((Object[]) insertPublicationForm.get("authorsName"), index, direction);
    }

    public ActionForward prepareSearchAuthor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("searchAuthors");
    }

    public ActionForward selectAuthor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixServiceException, FenixFilterException {

        DynaActionForm insertPublicationForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        ActionErrors errors = new ActionErrors();

        Integer[] authorsId = (Integer[]) insertPublicationForm.get("authorsId");
        String[] authorsName = (String[]) insertPublicationForm.get("authorsName");
        Integer selectedAuthorId = (Integer) insertPublicationForm.get("selectedAuthor");

        boolean alreadyInList = false;
        for (int i = 0; i < authorsId.length; i++) {
            if (authorsId[i].equals(selectedAuthorId)) {
                alreadyInList = true;
            }
        }

        if (alreadyInList && selectedAuthorId != new Integer(-1)) {
            errors.add("error1", new ActionError("message.publication.repeatedAuthor"));
        } else {

            InfoAuthor infoAuthor = null;

            try {
                infoAuthor = (InfoAuthor) ServiceUtils.executeService(userView, "ReadAuthorById",
                        new Object[] { selectedAuthorId });
                if (infoAuthor.getAuthor() == null || infoAuthor.getAuthor().equals("")) {
                    InfoPerson infoPerson = (InfoPerson) ServiceUtils.executeService(userView,
                            "ReadPerson", new Object[] { infoAuthor.getKeyPerson() });
                    infoAuthor.setAuthor(infoPerson.getNome());
                }
            } catch (FenixServiceException e) {
                e.printStackTrace();
                throw new FenixActionException(e);
            }

            Integer[] newAuthorsId = new Integer[authorsId.length + 1];
            String[] newAuthorsName = new String[authorsName.length + 1];
            for (int i = 0; i < authorsId.length; i++) {
                newAuthorsId[i] = authorsId[i];
                newAuthorsName[i] = authorsName[i];
            }

            newAuthorsId[newAuthorsId.length - 1] = infoAuthor.getIdInternal();
            newAuthorsName[newAuthorsName.length - 1] = infoAuthor.getAuthor();

            insertPublicationForm.set("authorsId", newAuthorsId);
            insertPublicationForm.set("authorsName", newAuthorsName);

        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        prepare(mapping, form, request, response);

        return mapping.findForward("insert");
    }

    public ActionForward searchAuthor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm insertPublicationForm = (DynaActionForm) form;

        String searchedAuthorName = (String) insertPublicationForm.get("searchedAuthorName");
        if (searchedAuthorName == null || searchedAuthorName.equals("")) {
            ActionErrors errors = new ActionErrors();
            errors.add("error1", new ActionError("message.publication.emptySearch"));
            return mapping.findForward("searchAuthors");
        }

        Object[] arg = { searchedAuthorName };
        List authorsList = null, personList = null;

        // ATEN??O, MAIS OBJECTOS DE DOM?NIO NA APRESENTA??O!!
        try {
            authorsList = (List) ServiceUtils.executeService(userView, "ReadAuthorsByName", arg);
            personList = (List) ServiceUtils.executeService(userView, "ReadPersonsNotAuthors",
                    new Object[] { "%" + searchedAuthorName.replace(' ', '%') + "%", userView });
            Iterator iter = personList.iterator();
            while (iter.hasNext()) {
                IPerson pessoa = (IPerson) iter.next();
                InfoAuthor infoAuthor = (InfoAuthor) ServiceUtils.executeService(userView,
                        "ReadAuthorByPersonId", new Object[] { pessoa.getIdInternal() });
                if (pessoa.getIdInternal() != null)
                    infoAuthor.setKeyPerson(pessoa.getIdInternal());
                infoAuthor.setAuthor(pessoa.getNome());
                if (infoAuthor.getIdInternal() != null)
                    authorsList.add(infoAuthor);
            }
        } catch (FenixServiceException e) {
            e.printStackTrace();
        }

        request.setAttribute("searchedAuthorsList", authorsList);
        
        return mapping.findForward("searchAuthors");
    }

    public List readInfoAuthors(List authorsIds, IUserView userView) throws FenixFilterException,
            FenixServiceException {

        List newAuthorsIds = new ArrayList();
        Iterator iteratorIds = authorsIds.iterator();

        while (iteratorIds.hasNext()) {
            newAuthorsIds.add(iteratorIds.next());
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

    public List infoAuthorsPersons(List listObjects, Object object) {
        List infoAuthorPersons = new ArrayList();

        if (object instanceof IAuthor) {

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

}