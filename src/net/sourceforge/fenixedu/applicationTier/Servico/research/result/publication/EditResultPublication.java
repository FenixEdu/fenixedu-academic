package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

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
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;

public class EditResultPublication extends ResultPublicationService {

    public ResearchResultPublication run(BookBean bean) throws FenixServiceException {
	final ResearchResultPublication publication = getResultPublication(bean);
	final ResearchResultPublication finalPublication;

	if (publication instanceof Book) {
	    ((Book) publication).setEditAll(bean.getTitle(), bean.getKeywords(), bean.getPublisher(), bean
		    .getYear(), bean.getVolume(), bean.getSeries(), bean.getAddress(), bean.getEdition(),
		    bean.getIsbn(), bean.getNumberPages(), bean.getLanguage(), bean.getCountry(), bean
			    .getScope(), bean.getNote(), bean.getMonth(), bean.getUrl());
	    finalPublication = publication;
	} else {
	    final Book book = getCreateService().createBookFromBean(bean);
	    publication.copyReferecesTo(book);
	    getDeleteService().run(publication.getIdInternal());
	    finalPublication = book;
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(finalPublication);
	return finalPublication;
    }

    public ResearchResultPublication run(BookPartBean bean) throws FenixServiceException {
	final ResearchResultPublication publication = getResultPublication(bean);
	final ResearchResultPublication finalPublication;

	if (publication instanceof BookPart) {
	    ((BookPart) publication).setEditAll(bean.getTitle(), bean.getKeywords(), bean.getBookTitle(), bean.getChapter(),
		    bean.getFirstPage(), bean.getLastPage(),bean. getPublisher(), bean.getYear(), bean
			    .getVolume(), bean.getSeries(), bean.getEdition(), bean.getCountry(), bean
			    .getAddress(), bean.getNote(), bean.getMonth(), bean.getUrl());
	    finalPublication = publication;

	} else {
	    final BookPart bookPart = getCreateService().createBookPartFromBean(bean);
	    publication.copyReferecesTo(bookPart);
	    getDeleteService().run(publication.getIdInternal());
	    finalPublication = bookPart;
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(finalPublication);
	return finalPublication;
    }
    
    public ResearchResultPublication run(ArticleBean bean) throws FenixServiceException {
	final ResearchResultPublication publication = getResultPublication(bean);
	final ResearchResultPublication finalPublication;

	if (publication instanceof Article) {
	    ((Article) publication).setEditAll(bean.getTitle(), bean.getKeywords(), bean.getJournalIssue(),
		    bean.getFirstPage(), bean.getLastPage(), bean.getNote(), bean.getLanguage(), bean
			    .getUrl());
	    finalPublication = publication;
	} else {
	    final Article article = getCreateService().createArticleFromBean(bean);
	    publication.copyReferecesTo(article);
	    getDeleteService().run(publication.getIdInternal());
	    finalPublication = article;
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(finalPublication);
	return finalPublication;
    }

    public ResearchResultPublication run(InproceedingsBean bean) throws FenixServiceException {
	final ResearchResultPublication publication = getResultPublication(bean);
	final ResearchResultPublication finalPublication;

	if (publication instanceof Inproceedings) {

	    ((Inproceedings) publication).setEditAll(bean.getTitle(), bean.getKeywords(), bean.getYear(),
		    bean.getConference(), bean.getScope(), bean.getPublisher(), bean.getOrganization(), bean
			    .getAddress(), bean.getFirstPage(), bean.getLastPage(), bean.getNote(), bean
			    .getLanguage(), bean.getMonth(), bean.getUrl());

	    finalPublication = publication;
	} else {
	    final Inproceedings inproceedings = getCreateService().createInproceedingsFromBean(bean);
	    publication.copyReferecesTo(inproceedings);
	    getDeleteService().run(publication.getIdInternal());
	    finalPublication = inproceedings;
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(finalPublication);
	return finalPublication;
    }

    public ResearchResultPublication run(ProceedingsBean bean) throws FenixServiceException {
	final ResearchResultPublication publication = getResultPublication(bean);
	final ResearchResultPublication finalPublication;

	if (publication instanceof Proceedings) {

	    ((Proceedings) publication).setEditAll(bean.getTitle(), bean.getKeywords(), bean.getYear(), bean
		    .getConference(), bean.getScope(), bean.getPublisher(), bean.getOrganization(), bean
		    .getAddress(), bean.getNote(), bean.getMonth(), bean.getUrl());
	    finalPublication = publication;

	} else {
	    final Proceedings proceedings = getCreateService().createProceedingsFromBean(bean);
	    publication.copyReferecesTo(proceedings);
	    getDeleteService().run(publication.getIdInternal());
	    finalPublication = proceedings;
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(finalPublication);
	return finalPublication;
    }

    public ResearchResultPublication run(ThesisBean bean) throws FenixServiceException {
	final ResearchResultPublication publication = getResultPublication(bean);
	final ResearchResultPublication finalPublication;

	if (publication instanceof Thesis) {
	    ((Thesis) publication).setEditAll(bean.getThesisType(), bean.getTitle(), bean.getKeywords(),
		    bean.getOrganization(), bean.getYear(), bean.getAddress(), bean.getNote(), bean
			    .getNumberPages(), bean.getLanguage(), bean.getMonth(), bean.getYearBegin(), bean
			    .getMonthBegin(), bean.getUrl());

	    finalPublication = publication;
	} else {
	    final Thesis thesis = getCreateService().createThesisFromBean(bean);
	    publication.copyReferecesTo(thesis);
	    getDeleteService().run(publication.getIdInternal());
	    finalPublication = thesis;
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(finalPublication);
	return finalPublication;
    }

    public ResearchResultPublication run(ManualBean bean) throws FenixServiceException {
	final ResearchResultPublication publication = getResultPublication(bean);
	final ResearchResultPublication finalPublication;

	if (publication instanceof Manual) {
	    ((Manual) publication).setEditAll(bean.getTitle(), bean.getKeywords(), bean.getOrganization(),
		    bean.getYear(), bean.getAddress(), bean.getNote(), bean.getEdition(), bean.getMonth(),
		    bean.getUrl());
	    finalPublication = publication;

	} else {
	    final Manual manual = getCreateService().createManualFromBean(bean);
	    publication.copyReferecesTo(manual);
	    getDeleteService().run(publication.getIdInternal());
	    finalPublication = manual;
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(finalPublication);
	return finalPublication;
    }

    public ResearchResultPublication run(TechnicalReportBean bean) throws FenixServiceException {
	final ResearchResultPublication publication = getResultPublication(bean);
	final ResearchResultPublication finalPublication;

	if (publication instanceof TechnicalReport) {
	    ((TechnicalReport) publication).setEditAll(bean.getTitle(), bean.getKeywords(),
		    bean.getOrganization(), bean.getYear(), bean.getTechnicalReportType(), bean.getNumber(),
		    bean.getAddress(), bean.getNote(), bean.getNumberPages(), bean.getLanguage(), bean
			    .getMonth(), bean.getUrl());
	    finalPublication = publication;
	} else {
	    final TechnicalReport techReport = getCreateService().createTechnicalReportFromBean(bean);
	    publication.copyReferecesTo(techReport);
	    getDeleteService().run(publication.getIdInternal());
	    finalPublication = techReport;
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(finalPublication);
	return finalPublication;
    }

    public ResearchResultPublication run(OtherPublicationBean bean) throws FenixServiceException {
	final ResearchResultPublication publication = getResultPublication(bean);
	final ResearchResultPublication finalPublication;
	if (publication instanceof OtherPublication) {
	    ((OtherPublication) publication).setEditAll(bean.getTitle(), bean.getKeywords(),
		    bean.getPublisher(), bean.getYear(), bean.getHowPublished(), bean.getNote(), bean
			    .getAddress(), bean.getOtherPublicationType(), bean.getNumberPages(), bean
			    .getLanguage(), bean.getCountry(), bean.getMonth(), bean.getUrl());
	    finalPublication = publication;

	} else {
	    final OtherPublication other = getCreateService().createOtherPublicationFromBean(bean);
	    publication.copyReferecesTo(other);
	    getDeleteService().run(publication.getIdInternal());
	    finalPublication = other;
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(finalPublication);
	return finalPublication;
    }

    private ResearchResultPublication getResultPublication(ResultPublicationBean bean)
	    throws FenixServiceException {
	if ((bean == null) || (bean.getIdInternal() == null))
	    throw new FenixServiceException();

	ResearchResultPublication publication = (ResearchResultPublication) ResearchResult.readByOid(bean
		.getIdInternal());
	if (publication == null)
	    throw new FenixServiceException();
	return publication;
    }

}
