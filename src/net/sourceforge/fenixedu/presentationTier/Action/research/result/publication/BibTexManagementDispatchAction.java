package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ConferenceArticlesBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InbookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.IncollectionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.OtherPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexParticipatorBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.ImportBibtexBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.ParticipatorBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPerson;
import bibtex.dom.BibtexPersonList;
import bibtex.expansions.ExpansionException;
import bibtex.expansions.MacroReferenceExpander;
import bibtex.expansions.PersonListExpander;
import bibtex.parser.BibtexParser;
import bibtex.parser.ParseException;

public class BibTexManagementDispatchAction extends FenixDispatchAction {

    public ActionForward exportPublicationToBibtex(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        Integer publicationId = Integer.valueOf(request.getParameter("publicationId"));
        ResultPublication publication = (ResultPublication) rootDomainObject
                .readResultByOID(publicationId);

        BibtexEntry bibtexEntry = publication.exportToBibtexEntry();
        String bibtex = bibtexEntry.toString();
        request.setAttribute("bibtex", bibtex);

        return mapping.findForward("PublicationExportedToBibtex");
    }

    public ActionForward prepareOpenBibtexFile(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        OpenFileBean openFileBean = new OpenFileBean();
        RenderUtils.invalidateViewState();
        request.setAttribute("openFileBean", openFileBean);
        
        return mapping.findForward("OpenBibtexFile");
    }

    public ActionForward prepareBibtexImport(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        OpenFileBean openFileBean = (OpenFileBean) RenderUtils.getViewState("openFileBean").getMetaObject().getObject();
                
        BibtexFile bibtexFile = new BibtexFile();
        BibtexParser parser = new BibtexParser(true);
        List<BibtexEntry> entryList = new ArrayList<BibtexEntry>();

        try {
            InputStream inputStream = openFileBean.getInputStream();
            if(inputStream.available() == 0)
                throw new IOException();
            
            parser.parse(bibtexFile, new InputStreamReader(inputStream));

            //macroReference must run before others expanders
            MacroReferenceExpander macroReferenceExpander = new MacroReferenceExpander(true, true, true);
            macroReferenceExpander.expand(bibtexFile);
            PersonListExpander personListExpander = new PersonListExpander(true, true, true);
            personListExpander.expand(bibtexFile);
        } catch (IOException e) {
            addActionMessage(request, "error.importBibtex.notPossibleToReadFile");
            return prepareOpenBibtexFile(mapping, form, request, response);
        } catch (ParseException e) {
            addActionMessage(request, "error.importBibtex.parsingBibtex");
            return prepareOpenBibtexFile(mapping, form, request, response);
        } catch (ExpansionException e) {
            addActionMessage(request, "error.importBibtex.expandingReferences");
            return prepareOpenBibtexFile(mapping, form, request, response);
        }
            
        for(Object entry: bibtexFile.getEntries())
        {
            if(entry instanceof BibtexEntry)
                entryList.add((BibtexEntry)entry);
        }
        ImportBibtexBean importBibtexBean = new ImportBibtexBean();
        for (BibtexEntry entry : entryList) {
            try {
                // create publication bean
                ResultPublicationBean publicationBean = createResultPublicationBean(entry);
                if (publicationBean == null)
                    continue;

                BibtexPublicationBean bibtexPublicationBean = new BibtexPublicationBean();
                BibtexPersonList bibtexAuthors = (BibtexPersonList) entry.getFieldValue("author");
                if (bibtexAuthors != null) {
                    List<BibtexParticipatorBean> authors = getParticipators(request, bibtexAuthors,
                            ResultParticipationRole.Author);
                    bibtexPublicationBean.setAuthors(authors);
                }
                BibtexPersonList bibtexEditors = (BibtexPersonList) entry.getFieldValue("editor");
                if (bibtexEditors != null) {
                    List<BibtexParticipatorBean> editors = getParticipators(request, bibtexEditors,
                            ResultParticipationRole.Editor);
                    bibtexPublicationBean.setEditors(editors);
                }

                bibtexPublicationBean.setPublicationBean(publicationBean);
                bibtexPublicationBean.setBibtex(entry.toString());
                importBibtexBean.getBibtexPublications().add(bibtexPublicationBean);
            } catch (Exception e) {
                //ignore entry
            }
        }
        
        if(importBibtexBean.getBibtexPublications().size() == 0)
        {
            addActionMessage(request, "error.importBibtex.noEntries");
            return mapping.findForward("OpenBibtexFile");
        }
        importBibtexBean.setTotalPublicationsReaded(importBibtexBean.getBibtexPublications().size());
        request.setAttribute("importBibtexBean", importBibtexBean);
        return mapping.findForward("ImportBibtex");
    }

    private List<ParticipatorBean> searchPersons(HttpServletRequest request, String name) throws FenixFilterException, FenixServiceException {
        List<Person> persons = null;
        List<ParticipatorBean> personsFound = new ArrayList<ParticipatorBean>();
        ParticipatorBean participator = null;

        Map<String, String> map = new HashMap<String, String>();
        map.put("slot", "name");

        persons = (List) ServiceUtils.executeService(getUserView(request), "SearchObjects",
                new Object[] { Person.class, name, 5, map });

        for (Person person : persons) {
            participator = new ParticipatorBean(person);
            personsFound.add(participator);
        }

        personsFound.add(createOtherParticipator());
        return personsFound;
    }

    private ParticipatorBean createOtherParticipator() {
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ResearcherResources", locale);
        String other = bundle.getString("label.otherPerson");
        ParticipatorBean otherParticipator = new ParticipatorBean(other);
        return otherParticipator;
    }

    private List<BibtexParticipatorBean> getParticipators(HttpServletRequest request,
            BibtexPersonList bibtexPersons, ResultParticipationRole role) throws FenixFilterException, FenixServiceException {
        List<BibtexParticipatorBean> participators = new ArrayList<BibtexParticipatorBean>();
        List<BibtexPerson> persons = bibtexPersons.getList();
        for (BibtexPerson person : persons) {
            if (!person.isOthers()) {
                BibtexParticipatorBean participator = new BibtexParticipatorBean();
                participator.setBibtexPerson(person);
                participator.setPersonRole(role);
                participator.setActiveSchema("bibtex.participator");

                participator.setPersonsFound(searchPersons(request, participator.getBibtexPerson()));
                if (participator.getPersonsFound().size() == 1)
                    participator.setPersonChosen(participator.getPersonsFound().get(0));

                participators.add(participator);
            }
        }
        return participators;
    }

    private ResultPublicationBean createResultPublicationBean(BibtexEntry entry) {
        String type = entry.getEntryType();
        ResultPublicationBean publicationBean = null;

        if (type.equalsIgnoreCase("book"))
            publicationBean = new BookBean(entry);
        if (type.equalsIgnoreCase("inbook"))
            publicationBean = new InbookBean(entry);
        if (type.equalsIgnoreCase("incollection"))
            publicationBean = new IncollectionBean(entry);
        else if (type.equalsIgnoreCase("article"))
            publicationBean = new ArticleBean(entry);
        else if (type.equalsIgnoreCase("inproceedings"))
            publicationBean = new InproceedingsBean(entry);
        else if (type.equalsIgnoreCase("proceedings"))
            publicationBean = new ProceedingsBean(entry);
        else if (type.equalsIgnoreCase("mastersthesis") || type.equalsIgnoreCase("phdthesis"))
            publicationBean = new ThesisBean(entry);
        else if (type.equalsIgnoreCase("manual"))
            publicationBean = new ManualBean(entry);
        else if (type.equalsIgnoreCase("techreport"))
            publicationBean = new TechnicalReportBean(entry);
        else if (type.equalsIgnoreCase("booklet") || type.equalsIgnoreCase("misc") || type.equalsIgnoreCase("unpublished"))
            publicationBean = new OtherPublicationBean(entry);

        return publicationBean;
    }

    public ActionForward setParticipations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ImportBibtexBean importBibtexBean = (ImportBibtexBean) RenderUtils.getViewState(
                "importBibtexBean").getMetaObject().getObject();
        int neededExternalPersonCreation = 0;

        try {
            verifyPersonImportingInParticipators(getUserView(request).getPerson(), importBibtexBean.getCurrentParticipators());
            
            neededExternalPersonCreation += verifyParticipatorsCreation(importBibtexBean
                    .getCurrentAuthors());
            neededExternalPersonCreation += verifyParticipatorsCreation(importBibtexBean
                    .getCurrentEditors());
        } catch (FenixActionException ex) {
            addActionMessage(request, ex.getMessage());
            request.setAttribute("importBibtexBean", importBibtexBean);
            return mapping.findForward("ImportBibtex");
        }

        if (neededExternalPersonCreation > 0) {
            RenderUtils.invalidateViewState();
            request.setAttribute("importBibtexBean", importBibtexBean);
            return mapping.findForward("ImportBibtex");
        }
        //else

        // proccess publication
        importBibtexBean.setCurrentProcessedParticipators(true);
        request.setAttribute("importBibtexBean", importBibtexBean);

        return mapping.findForward("ImportBibtex");
    }
    
    private void verifyPersonImportingInParticipators(Person personImporting, List<BibtexParticipatorBean> participators) throws FenixActionException {

        for (BibtexParticipatorBean participator : participators) {
            // verify option list
            if (participator.getPersonChosen() != null
                    && participator.getPersonChosen().getPerson() != null
                    && (personImporting.getIdInternal() == participator.getPersonChosen().getPerson()
                            .getIdInternal()))
                return;
            // verify other participator
            if (participator.getPerson() != null
                    && (personImporting.getIdInternal() == participator.getPerson().getIdInternal()))
                return;
        }
        
        
         
        throw new FenixActionException("error.importBibtex.personImportingNotInParticipants");
    }

    private int verifyParticipatorsCreation(List<BibtexParticipatorBean> participators)
            throws FenixActionException {
        if (participators == null)
            return 0;

        int neededExternalPersonCreation = 0;
        for (BibtexParticipatorBean participator : participators) {
            if (participator.isParticipatorProcessed())
                continue;
            if (participator.getPersonChosen() != null
                    && participator.getPersonChosen().getPerson() != null) {
                participator.setActiveSchema("bibtex.participatorChosed");
                participator.setPerson(participator.getPersonChosen().getPerson());
                participator.setParticipatorProcessed(true);
            } else {
                // another person not in the list
                if (participator.getPerson() != null) {
                    participator.setActiveSchema("bibtex.participatorChosed");
                    participator.setParticipatorProcessed(true);
                } else {
                    // creation of external person needed
                    if (participator.getPersonName() == null
                            || participator.getPersonName().length() == 0)
                        throw new FenixActionException("error.importBibtex.needOtherPerson");

                    if (!participator.isCreateExternalPerson()) {
                        participator.setActiveSchema("bibtex.participatorExternal");
                        participator.setCreateExternalPerson(true);
                        neededExternalPersonCreation++;
                    } else {
                        if (participator.getOrganizationName() == null
                                || participator.getOrganizationName().length() == 0)
                            throw new FenixActionException("error.importBibtex.needOrganization");
                        else
                            participator.setParticipatorProcessed(true);
                    }
                }
            }
        }
        return neededExternalPersonCreation;
    }

    public ActionForward invalidSubmit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ImportBibtexBean importBibtexBean = (ImportBibtexBean) RenderUtils.getViewState(
                "importBibtexBean").getMetaObject().getObject();
        request.setAttribute("importBibtexBean", importBibtexBean);

        return mapping.findForward("ImportBibtex");
    }
    
    private boolean publicationBeanNeedEventCreation(ResultPublicationBean publicationBean) {
        if(!(publicationBean instanceof ConferenceArticlesBean))
            return false;
        ConferenceArticlesBean conferenceBean = (ConferenceArticlesBean) publicationBean;
        if ((conferenceBean.getEvent() == null) && (conferenceBean.getCreateEvent() == false)) {
            conferenceBean.setCreateEvent(true);
            return true;
        }
        return false;
    }
    
    public ActionForward createPublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ImportBibtexBean importBibtexBean = (ImportBibtexBean) RenderUtils.getViewState(
                "importBibtexBean").getMetaObject().getObject();
        
        if(publicationBeanNeedEventCreation(importBibtexBean.getCurrentPublicationBean()))
        {
            //RenderUtils.invalidateViewState();
            request.setAttribute("importBibtexBean", importBibtexBean);
            return mapping.findForward("ImportBibtex");
        }

        try {
            Object[] arguments = new Object[] { getUserView(request).getPerson(),
                    importBibtexBean.getCurrentPublicationBean(),
                    importBibtexBean.getCurrentBibtexPublication() };
            ServiceUtils.executeService(getUserView(request), "ImportBibtexPublication", arguments);
        } catch (Exception ex) {
            addActionMessage(request, ex.getMessage());

            request.setAttribute("importBibtexBean", importBibtexBean);
            return mapping.findForward("ImportBibtex");
        }

        if (!importBibtexBean.moveToNextPublicaton())
            return mapping.findForward("PublicationsManagement");

        request.setAttribute("importBibtexBean", importBibtexBean);
        return mapping.findForward("ImportBibtex");
    }

    public ActionForward ignorePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ImportBibtexBean importBibtexBean = (ImportBibtexBean) RenderUtils.getViewState()
                .getMetaObject().getObject();

        if (!importBibtexBean.moveToNextPublicaton())
            return mapping.findForward("PublicationsManagement");

        request.setAttribute("importBibtexBean", importBibtexBean);
        return mapping.findForward("ImportBibtex");
    }
}
