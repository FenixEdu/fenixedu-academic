package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.*;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.publication.*;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;

public class EditResultPublication extends ResultPublicationService {

    public void run(BookBean bean) throws FenixServiceException {
	final Book book = (Book) getResultPublication(bean);

	book.setEditAll(bean.getTitle(), getPublisher(bean), bean.getYear(), bean.getVolume(), bean
		.getSeries(), bean.getAddress(), bean.getEdition(), bean.getIsbn(), bean
		.getNumberPages(), bean.getLanguage(), bean.getCountry(), bean.getScope(), bean
		.getNote(), bean.getMonth(), bean.getUrl());
    }

    public void run(BookPartBean bean) throws FenixServiceException {
	final BookPart bookPart = (BookPart) getResultPublication(bean);

	if (bean.getBookPartType().equals(BookPartType.Inbook)) {
	    bookPart.setEditAllInbook(bean.getBookPartType(), bean.getTitle(), bean.getChapter(), bean
		    .getFirstPage(), bean.getLastPage(), getPublisher(bean), bean.getYear(), bean
		    .getVolume(), bean.getSeries(), bean.getEdition(), bean.getCountry(), bean
		    .getAddress(), bean.getNote(), bean.getMonth(), bean.getUrl());
	} else {
	    bookPart.setEditAllIncollection(bean.getBookPartType(), bean.getTitle(),
		    bean.getBookTitle(), getPublisher(bean), bean.getYear(), bean.getFirstPage(), bean
			    .getLastPage(), getOrganization(bean), bean.getCountry(), bean.getAddress(),
		    bean.getNote(), bean.getMonth(), bean.getUrl());
	}
    }

    public void run(ArticleBean bean) throws FenixServiceException {
	final Article article = (Article) getResultPublication(bean);

	article.setEditAll(bean.getTitle(), bean.getJournal(), bean.getYear(), getPublisher(bean), bean
		.getVolume(), bean.getNumber(), bean.getFirstPage(), bean.getLastPage(), bean.getNote(),
		bean.getIssn(), bean.getLanguage(), bean.getCountry(), bean.getScope(), bean.getMonth(),
		bean.getUrl());
    }

    public void run(InproceedingsBean bean) throws FenixServiceException {
	final Inproceedings inproceedings = (Inproceedings) getResultPublication(bean);
	final Event event = getEventFromBean(bean);

	inproceedings.setEditAll(bean.getTitle(), bean.getBookTitle(), bean.getYear(), event,
		getPublisher(bean), getOrganization(bean), bean.getAddress(), bean.getFirstPage(), bean
			.getLastPage(), bean.getNote(), bean.getLanguage(), bean.getMonth(), bean
			.getUrl());
    }

    public void run(ProceedingsBean bean) throws FenixServiceException {
	final Proceedings proceedings = (Proceedings) getResultPublication(bean);
	final Event event = getEventFromBean(bean);

	proceedings.setEditAll(bean.getTitle(), bean.getYear(), event, getPublisher(bean),
		getOrganization(bean), bean.getAddress(), bean.getNote(), bean.getMonth(),
		bean.getUrl());
    }

    public void run(ThesisBean bean) throws FenixServiceException {
	final Thesis thesis = (Thesis) getResultPublication(bean);

	thesis.setEditAll(bean.getThesisType(), bean.getTitle(), getOrganization(bean), bean.getYear(),
		bean.getAddress(), bean.getNote(), bean.getNumberPages(), bean.getLanguage(), bean
			.getMonth(), bean.getUrl());
    }

    public void run(ManualBean bean) throws FenixServiceException {
	final Manual manual = (Manual) getResultPublication(bean);

	manual.setEditAll(bean.getTitle(), getOrganization(bean), bean.getYear(), bean.getAddress(),
		bean.getNote(), bean.getEdition(), bean.getMonth(), bean.getUrl());
    }

    public void run(TechnicalReportBean bean) throws FenixServiceException {
	final TechnicalReport technicalReport = (TechnicalReport) getResultPublication(bean);

	technicalReport.setEditAll(bean.getTitle(), getOrganization(bean), bean.getYear(), bean
		.getTechnicalReportType(), bean.getNumber(), bean.getAddress(), bean.getNote(), bean
		.getNumberPages(), bean.getLanguage(), bean.getMonth(), bean.getUrl());
    }

    public void run(BookletBean bean) throws FenixServiceException {
	final Booklet booklet = (Booklet) getResultPublication(bean);

	booklet.setEditAll(bean.getTitle(), bean.getHowPublished(), bean.getYear(), bean.getMonth(),
		bean.getAddress(), bean.getNote(), bean.getUrl());
    }

    public void run(MiscBean bean) throws FenixServiceException {
	final Misc misc = (Misc) getResultPublication(bean);

	misc.setEditAll(bean.getTitle(), getPublisher(bean), bean.getYear(), bean.getHowPublished(),
		bean.getNote(), bean.getAddress(), bean.getOtherPublicationType(),
		bean.getNumberPages(), bean.getLanguage(), bean.getCountry(), bean.getMonth(), bean
			.getUrl());
    }

    public void run(UnpublishedBean bean) throws FenixServiceException {
	final Unpublished unpublished = (Unpublished) getResultPublication(bean);

	unpublished.setEditAll(bean.getTitle(), bean.getNote(), bean.getYear(), bean.getMonth(), bean
		.getUrl());
    }
}
