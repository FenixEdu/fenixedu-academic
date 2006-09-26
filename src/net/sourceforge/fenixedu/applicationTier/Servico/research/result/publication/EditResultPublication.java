package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InbookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.IncollectionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.OtherPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;

public class EditResultPublication extends ResultPublicationService {

    public ResultPublication run(BookBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof Book) {
	    ((Book) publication).setEditAll(bean.getTitle(), getPublisher(bean), bean.getYear(), bean
		    .getVolume(), bean.getSeries(), bean.getAddress(), bean.getEdition(),
		    bean.getIsbn(), bean.getNumberPages(), bean.getLanguage(), bean.getCountry(), bean
			    .getScope(), bean.getNote(), bean.getMonth(), bean.getUrl());
	} else {
	    final Book book = getCreateService().createBookFromBean(bean);
	    updateResultReferences(publication, book);
	    getDeleteService().run(publication.getIdInternal());
	    return book;
	}
	return publication;
    }

    public ResultPublication run(InbookBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof BookPart) {
	    if (((BookPart) publication).getBookPartType().equals(BookPartType.Inbook)) {
		((BookPart) publication)
			.setEditAllInbook(BookPartType.Inbook, bean.getTitle(), bean.getChapter(), bean
				.getFirstPage(), bean.getLastPage(), getPublisher(bean), bean.getYear(),
				bean.getVolume(), bean.getSeries(), bean.getEdition(),
				bean.getCountry(), bean.getAddress(), bean.getNote(), bean.getMonth(),
				bean.getUrl());
	    }
	} else {
	    final BookPart inbook = getCreateService().createInbookFromBean(bean);
	    updateResultReferences(publication, inbook);
	    getDeleteService().run(publication.getIdInternal());
	    return inbook;
	}
	return publication;
    }

    public ResultPublication run(IncollectionBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof BookPart) {
	    if (((BookPart) publication).getBookPartType().equals(BookPartType.Incollection)) {
		((BookPart) publication).setEditAllIncollection(BookPartType.Incollection, bean
			.getTitle(), bean.getBookTitle(), getPublisher(bean), bean.getYear(), bean
			.getFirstPage(), bean.getLastPage(), getOrganization(bean), bean.getCountry(),
			bean.getAddress(), bean.getNote(), bean.getMonth(), bean.getUrl());

	    }
	} else {
	    final BookPart incollection = getCreateService().createIncollectionFromBean(bean);
	    updateResultReferences(publication, incollection);
	    getDeleteService().run(publication.getIdInternal());
	    return incollection;
	}
	return publication;
    }

    public ResultPublication run(ArticleBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof Article) {
	    ((Article) publication).setEditAll(bean.getTitle(), bean.getJournal(), bean.getYear(),
		    getPublisher(bean), bean.getVolume(), bean.getNumber(), bean.getFirstPage(), bean
			    .getLastPage(), bean.getNote(), bean.getIssn(), bean.getLanguage(), bean
			    .getCountry(), bean.getScope(), bean.getMonth(), bean.getUrl());
	} else {
	    final Article article = getCreateService().createArticleFromBean(bean);
	    updateResultReferences(publication, article);
	    getDeleteService().run(publication.getIdInternal());
	    return article;
	}
	return publication;
    }

    public ResultPublication run(InproceedingsBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof Inproceedings) {
	    final Event event = getEventFromBean(bean);

	    ((Inproceedings) publication).setEditAll(bean.getTitle(), bean.getYear(), event, bean.getScope(),
                getPublisher(bean), getOrganization(bean), bean.getAddress(), bean.getFirstPage(), bean
                        .getLastPage(), bean.getNote(), bean.getLanguage(), bean.getMonth(), bean
                        .getUrl());
	} else {
	    final Inproceedings inproceedings = getCreateService().createInproceedingsFromBean(bean);
	    updateResultReferences(publication, inproceedings);
	    getDeleteService().run(publication.getIdInternal());
	    return inproceedings;
	}
	return publication;
    }

    public ResultPublication run(ProceedingsBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof Proceedings) {
	    final Event event = getEventFromBean(bean);

	    ((Proceedings) publication).setEditAll(bean.getTitle(), bean.getYear(), event, bean.getScope(),
                getPublisher(bean), getOrganization(bean), bean.getAddress(), bean.getNote(), bean
                        .getMonth(), bean.getUrl());
	} else {
	    final Proceedings proceedings = getCreateService().createProceedingsFromBean(bean);
	    updateResultReferences(publication, proceedings);
	    getDeleteService().run(publication.getIdInternal());
	    return proceedings;
	}
	return publication;
    }

    public ResultPublication run(ThesisBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof Thesis) {
	    ((Thesis) publication).setEditAll(bean.getThesisType(), bean.getTitle(),
		    getOrganization(bean), bean.getYear(), bean.getAddress(), bean.getNote(), bean
			    .getNumberPages(), bean.getLanguage(), bean.getMonth(), bean.getUrl());
	} else {
	    final Thesis thesis = getCreateService().createThesisFromBean(bean);
	    updateResultReferences(publication, thesis);
	    getDeleteService().run(publication.getIdInternal());
	    return thesis;
	}
	return publication;
    }

    public ResultPublication run(ManualBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof Manual) {
	    ((Manual) publication)
		    .setEditAll(bean.getTitle(), getOrganization(bean), bean.getYear(), bean
			    .getAddress(), bean.getNote(), bean.getEdition(), bean.getMonth(), bean
			    .getUrl());
	} else {
	    final Manual manual = getCreateService().createManualFromBean(bean);
	    updateResultReferences(publication, manual);
	    getDeleteService().run(publication.getIdInternal());
	    return manual;
	}
	return publication;
    }

    public ResultPublication run(TechnicalReportBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof TechnicalReport) {
	    ((TechnicalReport) publication).setEditAll(bean.getTitle(), getOrganization(bean), bean
		    .getYear(), bean.getTechnicalReportType(), bean.getNumber(), bean.getAddress(), bean
		    .getNote(), bean.getNumberPages(), bean.getLanguage(), bean.getMonth(), bean
		    .getUrl());
	} else {
	    final TechnicalReport techReport = getCreateService().createTechnicalReportFromBean(bean);
	    updateResultReferences(publication, techReport);
	    getDeleteService().run(publication.getIdInternal());
	    return techReport;
	}
	return publication;
    }

    public ResultPublication run(OtherPublicationBean bean) throws FenixServiceException {
	final ResultPublication publication = getResultPublication(bean);

	if (publication instanceof OtherPublication) {
	    ((OtherPublication) publication).setEditAll(bean.getTitle(), getPublisher(bean), bean.getYear(), bean
                .getHowPublished(), bean.getNote(), bean.getAddress(), bean.getOtherPublicationType(),
                bean.getNumberPages(), bean.getLanguage(), bean.getCountry(), bean.getMonth(), bean
                        .getUrl());
   
	} else {
	    final OtherPublication other = getCreateService().createOtherPublicationFromBean(bean);
	    updateResultReferences(publication, other);
	    getDeleteService().run(publication.getIdInternal());
	    return other;
	}
	return publication;
    }

    private ResultPublication getResultPublication(ResultPublicationBean bean)
	    throws FenixServiceException {
	if ((bean == null) || (bean.getIdInternal() == null))
	    throw new FenixServiceException();

	ResultPublication publication = (ResultPublication) Result.readByOid(bean.getIdInternal());
	if (publication == null)
	    throw new FenixServiceException();
	return publication;
    }
}
