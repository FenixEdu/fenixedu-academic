package net.sourceforge.fenixedu.presentationTier.Action.publication;

import java.util.ArrayList;
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
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationAuthor;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Goes
 *  
 */
public class EditPublicationDispatchAction extends FenixDispatchAction {

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
        DynaActionForm dynaForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        //InfoPublication pub = (InfoPublication) infoObject;
        Integer keyPublicationType = (Integer) dynaForm.get("infoPublicationTypeId");

        Object[] argReqAtt = { keyPublicationType };
        List requiredAttributes = (List) ServiceUtils.executeService(userView, "ReadRequiredAttributes", argReqAtt);

        //VALIDATION CODE EXTRACTED FROM PFON'S OLD VALIDATION
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
        
        List<InfoAuthor> infoAuthors = new ArrayList<InfoAuthor>();
        InfoPublication publication = new InfoPublication();
        
        Object[] arrayId = (Object[]) dynaForm.get("authorsId");
        Object[] arrayName = (Object[]) dynaForm.get("authorsName");
        
        for (int i=0; i<arrayId.length; i++) {
        	if (((Integer)arrayId[i]).equals(new Integer(-1))) {
        		//external author
        		InfoAuthor author = new InfoAuthor();
        		String[] array = ((String)arrayName[i]).split("'.'");
        		author.setAuthor(array[0]);
        		author.setOrganization(array[1]);
        		//not actually required but explicit so the service will know this is an external author
        		author.setKeyPerson(null);
        		infoAuthors.add(author);
        	}
        	else {
	        	Object[] argReadAuthor = { arrayId[i] };
	        	InfoPerson author = (InfoPerson)ServiceUtils.executeService(userView, "ReadPerson", argReadAuthor);
                InfoAuthor infoAuthor = new InfoAuthor();
                infoAuthor.setIdInternal(author.getIdInternal());
                infoAuthor.setInfoPessoa(author);
                infoAuthor.setKeyPerson(author.getIdInternal());

	        	infoAuthors.add(infoAuthor);
        	}
        }
        
        List<InfoPublicationAuthor> infoPublicationAuthors = new ArrayList<InfoPublicationAuthor>();
        for (InfoAuthor author : infoAuthors) {
            InfoPublicationAuthor infoPublicationAuthor = new InfoPublicationAuthor();
            infoPublicationAuthor.setInfoAuthor(author);
            infoPublicationAuthor.setInfoPublication(publication);
            infoPublicationAuthor.setKeyAuthor(author.getIdInternal());
            infoPublicationAuthor.setKeyPublication(publication.getIdInternal());
            infoPublicationAuthors.add(infoPublicationAuthor);
        }
        
        publication.setInfoPublicationAuthors(infoPublicationAuthors);
        
        publication.setTitle((String)dynaForm.get("title"));
        //ERROR this is wrong... a domain object is up here :(
        InfoPublicationType type = InfoPublicationType.newInfoFromDomain(
        		(IPublicationType)ServiceUtils.executeService(userView,"ReadPublicationType",
        				new Object[]{ keyPublicationType }));
        HashMap attributes = (HashMap) ServiceUtils.executeService(userView,
                "ReadAttributesByPublicationType", new Object[] { keyPublicationType });

        resetPublication(publication);
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
            Integer ed;
            if (edition.equals(""))
                ed = new Integer(0);
            else
                ed = Integer.valueOf(edition);
            publication.setEdition(ed);
        }
        if (attributes.get("fascicle") != null) {
            String fascicle = (String) dynaForm.get("fascicle");
            Integer fas;
            if (fascicle.equals(""))
                fas = new Integer(0);
            else
                fas = Integer.valueOf(fascicle);
            publication.setFascicle(fas);
        }
        if (attributes.get("serie") != null) {
            String serie = (String) dynaForm.get("serie");
            Integer ser;
            if (serie.equals(""))
                ser = new Integer(0);
            else
                ser = Integer.valueOf(serie);
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
        if (attributes.get("university") != null)
            publication.setUniversity((String) dynaForm.get("university"));
        
        publication.setIdInternal((Integer) dynaForm.get("idInternal"));
        
        Object[] argEditPublication = { publication };
        ServiceUtils.executeService(userView, "EditPublication", argEditPublication);
		
		return mapping.findForward("done");
	}
    
    private void resetPublication(InfoPublication publication) {
        publication.setSubType(null);
        publication.setJournalName(null);
        publication.setVolume(null);
        publication.setFirstPage(null);
        publication.setLastPage(null);
        publication.setLanguage(null);
        publication.setFormat(null);
        publication.setObservation(null);
        publication.setNumber(null);
        publication.setMonth(null);
        publication.setYear(null);
        publication.setMonth_end(null);
        publication.setYear_end(null);
        publication.setEditor(null);
        publication.setCountry(null);
        publication.setIssn(null);
        publication.setScope(null);
        publication.setUrl(null);
        publication.setEditorCity(null);
        publication.setNumberPages(null);
        publication.setEdition(null);
        publication.setFascicle(null);
        publication.setSerie(null);
        publication.setIsbn(null);
        publication.setLocal(null);
        publication.setConference(null);
        publication.setInstituition(null);
        publication.setOriginalLanguage(null);
        publication.setTranslatedAuthor(null);
        publication.setCriticizedAuthor(null);
        publication.setUniversity(null);
    }
	
	private void loadData(ActionForm form, HttpServletRequest request) throws FenixFilterException, FenixServiceException {
		String id = request.getParameter("idInternal");
		if (id == null) return;
		
		Integer idInternal = Integer.valueOf(id);
		
		
		IUserView userView = SessionUtils.getUserView(request);
		
		InfoPublication publication = (InfoPublication) ServiceUtils.executeService(userView,
		        "ReadPublicationByInternalId", new Object[] {idInternal,userView});
		DynaActionForm actionForm = (DynaActionForm)form;
		
		//ERROR this should not happen - domain object in presentation
		IPublicationType iPublicationType = (IPublicationType) ServiceUtils.executeService(userView,
				"ReadPublicationType", new Object[] {publication.getInfoPublicationType().getIdInternal()});
		InfoPublicationType infoPublicationType = InfoPublicationType.newInfoFromDomain(iPublicationType);
		if (infoPublicationType != null) actionForm.set("infoPublicationTypeId",infoPublicationType.getIdInternal());
		
		if (publication.getTitle() != null) actionForm.set("title",publication.getTitle());
		if (publication.getInfoPublicationType().getIdInternal() != null) actionForm.set("infoPublicationTypeId",publication.getInfoPublicationType().getIdInternal());
		if (publication.getSubType() != null) actionForm.set("subtype",publication.getSubType());
		if (publication.getJournalName() != null) actionForm.set("journalName",publication.getJournalName());
		if (publication.getVolume() != null) actionForm.set("volume",publication.getVolume());
		if (publication.getFirstPage() != null) actionForm.set("firstPage",publication.getFirstPage());
		if (publication.getLastPage() != null) actionForm.set("lastPage",publication.getLastPage());
		if (publication.getLanguage() != null) actionForm.set("language",publication.getLanguage());
		if (publication.getFormat() != null) actionForm.set("format",publication.getFormat());
		if (publication.getObservation() != null) actionForm.set("observation",publication.getObservation());
		if (publication.getNumber() != null) actionForm.set("number",publication.getNumber());
		if (publication.getMonth() != null) actionForm.set("month",publication.getMonth());
		if (publication.getYear() != null) actionForm.set("year",publication.getYear());
		if (publication.getMonth_end() != null) actionForm.set("month_end",publication.getMonth_end());
		if (publication.getYear_end() != null) actionForm.set("year_end",publication.getYear_end());
		if (publication.getEditor() != null) actionForm.set("editor",publication.getEditor());
		if (publication.getCountry() != null) actionForm.set("country",publication.getCountry());
		if (publication.getIssn() != null) actionForm.set("issn",publication.getIssn());
		if (publication.getScope() != null) actionForm.set("scope",publication.getScope());
		if (publication.getUrl() != null) actionForm.set("url",publication.getUrl());
		if (publication.getEditorCity() != null) actionForm.set("editorCity",publication.getEditorCity());
		if (publication.getNumberPages() != null) actionForm.set("numberPages",publication.getNumberPages());
		if (publication.getEdition() != null) actionForm.set("edition",publication.getEdition().toString());
		if (publication.getFascicle() != null) actionForm.set("fascicle",publication.getFascicle().toString());
		if (publication.getSerie() != null) actionForm.set("fascicle",publication.getSerie().toString());
		if (publication.getIsbn() != null) actionForm.set("isbn",publication.getIsbn());
		if (publication.getLocal() != null) actionForm.set("local",publication.getLocal());
		if (publication.getConference() != null) actionForm.set("conference",publication.getConference());
		if (publication.getInstituition() != null) actionForm.set("instituition",publication.getInstituition());
		if (publication.getOriginalLanguage() != null) actionForm.set("originalLanguage",publication.getOriginalLanguage());
		if (publication.getTranslatedAuthor() != null) actionForm.set("translatedAuthor",publication.getTranslatedAuthor());
		if (publication.getCriticizedAuthor() != null) actionForm.set("criticizedAuthor",publication.getCriticizedAuthor());
		if (publication.getPublicationType() != null) actionForm.set("publicationType",publication.getPublicationType());
		if (publication.getUniversity() != null) actionForm.set("university",publication.getUniversity());
		actionForm.set("idInternal",idInternal);
		
		
		Iterator iter = publication.getInfoPublicationAuthors().iterator();
		String[] names = new String[publication.getInfoPublicationAuthors().size()];
		Integer[] ids = new Integer[publication.getInfoPublicationAuthors().size()];
		
		for (int i=0; i<publication.getInfoPublicationAuthors().size();i++) {
			InfoAuthor author = (InfoAuthor)iter.next();
			
			if (author.getAuthor() != null && !author.getAuthor().equals("")) {
				names[i] = author.getAuthor();
			}
			else {
				InfoPerson infoPerson = (InfoPerson) ServiceUtils.executeService(userView, "ReadPerson",
												new Object[] { author.getKeyPerson() });
				names[i] = infoPerson.getNome();
			}
			
			ids[i] = author.getIdInternal();
		}

		actionForm.set("authorsName",names);
		actionForm.set("authorsId",ids);
		
		/*
			autores podem vir a null...
			os campos de selec??o n?o est?o a aparecer bem preenchidos, os malandros
		*/
	}
	
    public ActionForward lightPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    	
    	DynaActionForm insertPublicationForm = (DynaActionForm) form;    	
    	
    	Integer type = (Integer)insertPublicationForm.get("infoPublicationTypeId");
    	if (type == null || type.intValue() <= 0) {
	    	//select the first from the publication type list
	    	type = new Integer(1);
	    	insertPublicationForm.set("infoPublicationTypeId",type);
    	}    	
    	
    	IUserView userView = SessionUtils.getUserView(request);
        Object argPubType[] = { userView.getUtilizador() };
		List infoPublicationTypes = (List) ServiceUtils.executeService(userView,
		        "ReadAllPublicationTypes", argPubType);
		request.setAttribute("publicationTypesList", infoPublicationTypes);

		Object args[] = { type };
        
		List infoPublicationSubtypes = (List) ServiceUtils.executeService(userView, "ReadPublicationSubtypesByPublicationType", args);
		request.setAttribute("subTypeList", infoPublicationSubtypes);
		
		List infoPublicationFormats = (List) ServiceUtils.executeService(userView, "ReadAllPublicationFormats", args);
		request.setAttribute("formatList", infoPublicationFormats);
		
		List monthList = (List) ServiceUtils.executeService(userView, "ReadPublicationMonths", args);
		request.setAttribute("monthList", monthList);
		
		List scopeList = (List) ServiceUtils.executeService(userView, "ReadPublicationScopes", args);
		request.setAttribute("scopeList", scopeList);
		
		Integer[] authorsId = (Integer[]) insertPublicationForm.get("authorsId");
		if (authorsId.length == 0) {
			InfoPerson infoPerson = (InfoPerson) ServiceUtils.executeService(userView, "ReadPersonByUsername", new Object[] { userView.getUtilizador() });

			Integer[] newAuthorsId = new Integer[1];
	        String[] newAuthorsName = new String[1];
	        
	        newAuthorsId[newAuthorsId.length - 1] = infoPerson.getIdInternal();
	        newAuthorsName[newAuthorsName.length - 1] = infoPerson.getNome();
	
	        insertPublicationForm.set("authorsId", newAuthorsId);
	        insertPublicationForm.set("authorsName", newAuthorsName);	
		}
		return mapping.findForward("edit");
		
    }
    
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    	
        loadData(form,request);        
        lightPrepare(mapping,form,request,response);
		
        return mapping.findForward("edit");
    }
    
    public ActionForward externalAuthor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
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
        
        lightPrepare(mapping, form, request, response);

        return mapping.findForward("edit");
    }

    public ActionForward up(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        reorderAuthorsList(form, -1);
        lightPrepare(mapping, form, request, response);

        return mapping.findForward("edit");

    }

    public ActionForward down(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        reorderAuthorsList(form, 1);
        lightPrepare(mapping, form, request, response);

        return mapping.findForward("edit");
    }
    
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    		HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    	DynaActionForm insertPublicationForm = (DynaActionForm) form;

        int index = ((Integer) insertPublicationForm.get("index")).intValue();
        
        Object[] arrayId = (Object[]) insertPublicationForm.get("authorsId");
        Object[] arrayName = (Object[]) insertPublicationForm.get("authorsName");
        
        Integer[] newArrayId = new Integer[arrayId.length - 1];
        String[] newArrayName = new String[arrayId.length - 1];
        
        for (int i=0,j=0; i<arrayId.length;) {
        	if (i == index) { i++; continue; }
        	newArrayId[j] = (Integer)arrayId[i];
        	newArrayName[j] = (String)arrayName[i];
        	i++;j++;
        }
        
        insertPublicationForm.set("authorsId", newArrayId);
        insertPublicationForm.set("authorsName", newArrayName);
        
        lightPrepare(mapping, form, request, response);
        
        return mapping.findForward("edit");
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
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException {

        DynaActionForm insertPublicationForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);
        
        ActionErrors errors = new ActionErrors();

        Integer[] authorsId = (Integer[]) insertPublicationForm.get("authorsId");
        String[] authorsName = (String[]) insertPublicationForm.get("authorsName");
        Integer selectedAuthorId = (Integer) insertPublicationForm.get("selectedAuthor");

        boolean alreadyInList = false;
        for (int i=0;i<authorsId.length;i++) {
        	if (authorsId[i].equals(selectedAuthorId)) {
        		alreadyInList = true;
        	}
        }
        
        if (alreadyInList && selectedAuthorId != new Integer(-1)) {
    		errors.add("error1", new ActionError("message.publication.repeatedAuthor"));
        }
        else {

            InfoPerson infoPerson = (InfoPerson) ServiceUtils.executeService(userView, "ReadPerson",
                    new Object[] { selectedAuthorId });
            
            InfoAuthor infoAuthor = new InfoAuthor();
            infoAuthor.setAuthor(infoPerson.getNome());
            infoAuthor.setIdInternal(infoPerson.getIdInternal());
            infoAuthor.setInfoPessoa(infoPerson);
            infoAuthor.setKeyPerson(infoPerson.getIdInternal());
            infoAuthor.setOrganization("");
	        
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
        
        lightPrepare(mapping, form, request, response);

        return mapping.findForward("edit");
    }

    public ActionForward searchAuthor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm insertPublicationForm = (DynaActionForm) form;

        String searchedAuthorName = (String) insertPublicationForm.get("searchedAuthorName");
        if (searchedAuthorName == null || searchedAuthorName.equals("")) {
            ActionErrors errors = new ActionErrors();
            errors.add("error1", new ActionError("message.publication.emptySearch"));
            return mapping.findForward("searchAuthors");
        }

        Object[] arg = { searchedAuthorName };
        List infoAuthorList = new ArrayList<InfoAuthor>();

        infoAuthorList = (List<InfoAuthor>) ServiceUtils.executeService(userView, "ReadAuthorsByName", arg);

        //TODO RETIRAR o próprio autor
        
        request.setAttribute("searchedAuthorsList", infoAuthorList);
        
        return mapping.findForward("searchAuthors");
    }
    
    public ActionForward cancel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("done");
    }
}