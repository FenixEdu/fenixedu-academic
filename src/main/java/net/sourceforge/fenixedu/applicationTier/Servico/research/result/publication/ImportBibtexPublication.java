package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookPartBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.OtherPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexParticipatorBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexPublicationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import pt.ist.fenixframework.Atomic;

public class ImportBibtexPublication extends ResultPublicationService {

    protected ResearchResultPublication run(Person personImporting, BookBean bean, BibtexPublicationBean bibtexPublicationBean)
            throws FenixServiceException {
        if (bean == null) {
            throw new NullPointerException();
        }

        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        bean.setRole(participator.getPersonRole());

        ResearchResultPublication publication = createBookFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return publication;
    }

    protected ResearchResultPublication run(Person personImporting, BookPartBean bean, BibtexPublicationBean bibtexPublicationBean)
            throws FenixServiceException {
        if (bean == null) {
            throw new NullPointerException();
        }

        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        bean.setRole(participator.getPersonRole());

        ResearchResultPublication publication = createBookPartFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return publication;
    }

    protected ResearchResultPublication run(Person personImporting, ArticleBean bean, BibtexPublicationBean bibtexPublicationBean)
            throws FenixServiceException {
        if (bean == null) {
            throw new NullPointerException();
        }

        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());

        ResearchResultPublication publication = createArticleFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return publication;
    }

    protected ResearchResultPublication run(Person personImporting, InproceedingsBean bean,
            BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null) {
            throw new NullPointerException();
        }

        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());
        bean.setRole(participator.getPersonRole());

        ResearchResultPublication publication = createInproceedingsFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return publication;
    }

    protected ResearchResultPublication run(Person personImporting, ProceedingsBean bean,
            BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null) {
            throw new NullPointerException();
        }

        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());

        ResearchResultPublication publication = createProceedingsFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return publication;
    }

    protected ResearchResultPublication run(Person personImporting, ThesisBean bean, BibtexPublicationBean bibtexPublicationBean)
            throws FenixServiceException {
        if (bean == null) {
            throw new NullPointerException();
        }

        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());

        ResearchResultPublication publication = createThesisFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return publication;
    }

    protected ResearchResultPublication run(Person personImporting, ManualBean bean, BibtexPublicationBean bibtexPublicationBean)
            throws FenixServiceException {
        if (bean == null) {
            throw new NullPointerException();
        }

        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());

        ResearchResultPublication publication = createManualFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return publication;
    }

    protected ResearchResultPublication run(Person personImporting, TechnicalReportBean bean,
            BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null) {
            throw new NullPointerException();
        }

        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());

        ResearchResultPublication publication = createTechnicalReportFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return publication;
    }

    protected ResearchResultPublication run(Person personImporting, OtherPublicationBean bean,
            BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        if (bean == null) {
            throw new NullPointerException();
        }

        BibtexParticipatorBean participator = verifyPersonImporting(personImporting, bibtexPublicationBean.getParticipators());
        bean.setPerson(participator.getPerson());

        ResearchResultPublication publication = createOtherPublicationFromBean(bean);
        createRestOfParticipations(personImporting, bibtexPublicationBean.getParticipators(), publication);
        return publication;
    }

    private void createRestOfParticipations(Person personImporting, List<BibtexParticipatorBean> participators,
            ResearchResultPublication publication) throws FenixServiceException {

        for (BibtexParticipatorBean participator : participators) {
            Person person = null;

            if (participator.getPerson() != null) {
                person = participator.getPerson();
            } else {
                // create external person and participation
                InsertExternalPerson newPerson = new InsertExternalPerson();
                if (participator.getOrganization() != null) {
                    person =
                            (newPerson.run(new InsertExternalPerson.ServiceArguments(participator.getPersonName(), participator
                                    .getOrganization()))).getPerson();
                } else {
                    person = (newPerson.run(participator.getPersonName(), participator.getOrganizationName())).getPerson();
                }
            }
            if (person != personImporting) {
                publication.addParticipation(person, participator.getPersonRole());
            }
        }
    }

    private BibtexParticipatorBean verifyPersonImporting(Person personImporting, List<BibtexParticipatorBean> participators)
            throws FenixServiceException {

        for (BibtexParticipatorBean participator : participators) {
            if (participator.getPerson() != null) {
                if (personImporting == participator.getPerson()) {
                    return participator;
                }
            }
        }
        throw new FenixServiceException("error.importBibtex.personImportingNotInParticipants");
    }

    // Service Invokers migrated from Berserk

    private static final ImportBibtexPublication serviceInstance = new ImportBibtexPublication();

    @Atomic
    public static ResearchResultPublication runImportBibtexPublication(Person personImporting, BookBean bean,
            BibtexPublicationBean bibtexPublicationBean) throws FenixServiceException {
        return serviceInstance.run(personImporting, bean, bibtexPublicationBean);
    }

    @Atomic
    public static ResearchResultPublication runImportBibtexPublication(Person person,
            ResultPublicationBean currentPublicationBean, BibtexPublicationBean currentBibtexPublication)
            throws FenixServiceException {
        if (currentPublicationBean instanceof BookBean) {
            return serviceInstance.run(person, (BookBean) currentPublicationBean, currentBibtexPublication);
        } else if (currentPublicationBean instanceof BookPartBean) {
            return serviceInstance.run(person, (BookPartBean) currentPublicationBean, currentBibtexPublication);
        } else if (currentPublicationBean instanceof ArticleBean) {
            return serviceInstance.run(person, (ArticleBean) currentPublicationBean, currentBibtexPublication);
        } else if (currentPublicationBean instanceof InproceedingsBean) {
            return serviceInstance.run(person, (InproceedingsBean) currentPublicationBean, currentBibtexPublication);
        } else if (currentPublicationBean instanceof ProceedingsBean) {
            return serviceInstance.run(person, (ProceedingsBean) currentPublicationBean, currentBibtexPublication);
        } else if (currentPublicationBean instanceof ThesisBean) {
            return serviceInstance.run(person, (ThesisBean) currentPublicationBean, currentBibtexPublication);
        } else if (currentPublicationBean instanceof ManualBean) {
            return serviceInstance.run(person, (ManualBean) currentPublicationBean, currentBibtexPublication);
        } else if (currentPublicationBean instanceof TechnicalReportBean) {
            return serviceInstance.run(person, (TechnicalReportBean) currentPublicationBean, currentBibtexPublication);
        } else if (currentPublicationBean instanceof OtherPublicationBean) {
            return serviceInstance.run(person, (OtherPublicationBean) currentPublicationBean, currentBibtexPublication);
        } else {
            throw new UnsupportedOperationException("Sorry, I don't know how to handle "
                    + currentPublicationBean.getClass().getSimpleName());
        }
    }
}