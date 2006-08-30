package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookPartBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookletBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.MiscBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.UnpublishedBean;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Booklet;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.Misc;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unpublished;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;

public class CreateResultPublication extends ResultPublicationService {

    public Book run(BookBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	
	return new Book(bean.getPerson(), bean.getResultParticipationRole(), bean.getTitle(), bean
		.getPublisher(), bean.getYear(), bean.getVolume(), bean.getSeries(), bean.getAddress(),
		bean.getEdition(), bean.getIsbn(), bean.getNumberPages(), bean.getLanguage(), bean
			.getCountry(), bean.getScope(), bean.getNote(), bean.getMonth(), bean.getUrl());
    }

    public BookPart run(BookPartBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	if (bean.getBookPartType().equals(BookPartType.Inbook)) {
	    return new BookPart(bean.getPerson(), bean.getResultParticipationRole(), bean
		    .getBookPartType(), bean.getTitle(), bean.getChapter(), bean.getFirstPage(), bean
		    .getLastPage(), getPublisher(bean), bean.getYear(), bean.getVolume(), bean
		    .getSeries(), bean.getEdition(), bean.getCountry(), bean.getAddress(), bean
		    .getNote(), bean.getMonth(), bean.getUrl());
	}
	return new BookPart(bean.getPerson(), bean.getResultParticipationRole(), bean.getBookPartType(),
		bean.getTitle(), bean.getBookTitle(), getPublisher(bean), bean.getYear(), bean
			.getFirstPage(), bean.getLastPage(), getOrganization(bean), bean.getCountry(),
		bean.getAddress(), bean.getNote(), bean.getMonth(), bean.getUrl());
    }

    public Article run(ArticleBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	return new Article(bean.getPerson(), bean.getTitle(), bean.getJournal(), bean.getYear(),
		getOrganization(bean), bean.getVolume(), bean.getNumber(), bean.getFirstPage(), bean
			.getLastPage(), bean.getNote(), bean.getIssn(), bean.getLanguage(), bean
			.getCountry(), bean.getScope(), bean.getMonth(), bean.getUrl());
    }

    public Inproceedings run(InproceedingsBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	final Event event = getEventFromBean(bean);

	return new Inproceedings(bean.getPerson(), bean.getResultParticipationRole(), bean.getTitle(),
		bean.getBookTitle(), bean.getYear(), event, getPublisher(bean), getOrganization(bean),
		bean.getAddress(), bean.getFirstPage(), bean.getLastPage(), bean.getNote(), bean
			.getLanguage(), bean.getMonth(), bean.getUrl());
    }

    public Proceedings run(ProceedingsBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	final Event event = getEventFromBean(bean);

	return new Proceedings(bean.getPerson(), bean.getTitle(), bean.getYear(), event,
		getPublisher(bean), getOrganization(bean), bean.getAddress(), bean.getNote(), bean
			.getMonth(), bean.getUrl());
    }

    public Thesis run(ThesisBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	return new Thesis(bean.getPerson(), bean.getThesisType(), bean.getTitle(),
		getOrganization(bean), bean.getYear(), bean.getAddress(), bean.getNote(), bean
			.getNumberPages(), bean.getLanguage(), bean.getMonth(), bean.getUrl());
    }

    public Manual run(ManualBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	return new Manual(bean.getPerson(), bean.getTitle(), getOrganization(bean), bean.getYear(), bean
		.getAddress(), bean.getNote(), bean.getEdition(), bean.getMonth(), bean.getUrl());
    }

    public TechnicalReport run(TechnicalReportBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	return new TechnicalReport(bean.getPerson(), bean.getTitle(), getOrganization(bean), bean
		.getYear(), bean.getTechnicalReportType(), bean.getNumber(), bean.getAddress(), bean
		.getNote(), bean.getNumberPages(), bean.getLanguage(), bean.getMonth(), bean.getUrl());
    }

    public Booklet run(BookletBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	return new Booklet(bean.getPerson(), bean.getTitle(), bean.getHowPublished(), bean.getYear(),
		bean.getMonth(), bean.getAddress(), bean.getNote(), bean.getUrl());
    }

    public Misc run(MiscBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	return new Misc(bean.getPerson(), bean.getTitle(), getPublisher(bean), bean.getYear(), bean
		.getHowPublished(), bean.getNote(), bean.getAddress(), bean.getOtherPublicationType(),
		bean.getNumberPages(), bean.getLanguage(), bean.getCountry(), bean.getMonth(), bean
			.getUrl());
    }

    public Unpublished run(UnpublishedBean bean) {
	if (bean == null)
	    throw new NullPointerException();

	return new Unpublished(bean.getPerson(), bean.getTitle(), bean.getNote(), bean.getYear(), bean
		.getMonth(), bean.getUrl());
    }
}
