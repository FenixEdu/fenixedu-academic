package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InbookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.IncollectionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.OtherPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexParticipatorBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexPublicationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;

public class ImportBibtexPublication extends ResultPublicationService {

    public ResearchResultPublication run(Person personImporting, BookBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        bean.setRole(participator.getPersonRole());
        
        ResearchResultPublication publication = createBookFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }
    
    public ResearchResultPublication run(Person personImporting, InbookBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        bean.setRole(participator.getPersonRole());
        
        ResearchResultPublication publication = createInbookFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }
    
    public ResearchResultPublication run(Person personImporting, IncollectionBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        bean.setRole(participator.getPersonRole());
        
        ResearchResultPublication publication = createIncollectionFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }
    
    public ResearchResultPublication run(Person personImporting, ArticleBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        
        ResearchResultPublication publication = createArticleFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }
    
    public ResearchResultPublication run(Person personImporting, InproceedingsBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        bean.setRole(participator.getPersonRole());
        
        ResearchResultPublication publication = createInproceedingsFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }

    public ResearchResultPublication run(Person personImporting, ProceedingsBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        
        ResearchResultPublication publication = createProceedingsFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }
    
    public ResearchResultPublication run(Person personImporting, ThesisBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        
        ResearchResultPublication publication = createThesisFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }
    
    public ResearchResultPublication run(Person personImporting, ManualBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        
        ResearchResultPublication publication = createManualFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }
    
    public ResearchResultPublication run(Person personImporting, TechnicalReportBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        
        ResearchResultPublication publication = createTechnicalReportFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }
    
    public ResearchResultPublication run(Person personImporting, OtherPublicationBean bean, BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null)
            throw new NullPointerException();
        
        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        
        ResearchResultPublication publication = createOtherPublicationFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return (ResearchResultPublication) ResearchResultMetaDataManager.addDefaultDocument(publication);
    }
    
    private void createRestOfParticipations(Person personImporting,
            List<BibtexParticipatorBean> participators, ResearchResultPublication publication)
            throws FenixServiceException {

        for (BibtexParticipatorBean participator : participators) {
            Person person = null;

            if (participator.getPerson() != null)
                person = participator.getPerson();
            else {
                // create external person and participation
                InsertExternalPerson newPerson = new InsertExternalPerson();
                if (participator.getOrganization() != null)
                    person = (newPerson
                            .run(participator.getPersonName(), participator.getOrganization()))
                            .getPerson();
                else
                    person = (newPerson.run(participator.getPersonName(), participator
                            .getOrganizationName())).getPerson();
            }
            if (person.getIdInternal() != personImporting.getIdInternal())
                publication.addParticipation(person, participator.getPersonRole());
        }
    }

    private BibtexParticipatorBean verifyPersonImporting(Person personImporting,
            List<BibtexParticipatorBean> participators) throws FenixServiceException {

        for (BibtexParticipatorBean participator : participators) {
            if (participator.getPerson() != null) {
                if (personImporting.getIdInternal() == participator.getPerson().getIdInternal())
                    return participator;
            }
        }
        throw new FenixServiceException("error.importBibtex.personImportingNotInParticipants");
    }

}
