package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookPartBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.OtherPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;

public abstract class ResultPublicationService extends Service {

	protected static CreateResultPublication getCreateService() {
		return new CreateResultPublication();
	}

	protected static DeleteResultPublication getDeleteService() {
		return new DeleteResultPublication();
	}

	/*
	 * Methods used by CreateResultPublication and ImportBibtexPublication to
	 * create publications with one participator
	 */
	protected Book createBookFromBean(BookBean bean) {
		return new Book(bean.getPerson(), bean.getRole(), bean.getTitle(), bean.getKeywords(),
				bean.getPublisher(), bean.getYear(), bean.getVolume(), bean.getSeries(), bean.getAddress(),
				bean.getEdition(), bean.getIsbn(), bean.getNumberPages(), bean.getLanguage(), bean
						.getCountry(), bean.getScope(), bean.getNote(), bean.getMonth(), bean.getUrl());
	}

	protected BookPart createBookPartFromBean(BookPartBean bean) {
	    return new BookPart(bean.getPerson(), bean.getRole(), bean.getTitle(), bean
			.getKeywords(), bean.getBookTitle(), bean.getChapter(), bean.getVolume(), bean.getPublisher(), bean.getYear(), bean.getFirstPage(), bean.getLastPage(),
			bean.getSeries(), bean.getEdition(), bean.getOrganization(),
				bean.getCountry(), bean.getAddress(), bean.getNote(), bean.getMonth(), bean.getUrl());
	}

	protected Article createArticleFromBean(ArticleBean bean) {
	    return new Article(bean.getPerson(), bean.getTitle(), bean.getKeywords(), bean.getJournalIssue(), bean.getFirstPage(),
				bean.getLastPage(), bean.getNote(), bean.getLanguage(), bean.getUrl());
	}

	protected Inproceedings createInproceedingsFromBean(InproceedingsBean bean) {

		return new Inproceedings(bean.getPerson(), bean.getRole(), bean.getTitle(), bean.getKeywords(), bean.getEventEdition(),
				bean.getPublisher(), bean.getAddress(), bean.getFirstPage(), bean.getLastPage(), bean.getNote(), bean.getLanguage(), bean.getUrl());
	}

	protected Proceedings createProceedingsFromBean(ProceedingsBean bean) {

		return new Proceedings(bean.getPerson(), bean.getTitle(), bean.getKeywords(), bean.getEventEdition(), bean.getPublisher(),
				bean.getAddress(), bean.getNote(), bean.getUrl());
	}

	protected Thesis createThesisFromBean(ThesisBean bean) {
		return new Thesis(bean.getPerson(), bean.getThesisType(), bean.getTitle(), bean.getKeywords(),
				bean.getOrganization(), bean.getYear(), bean.getAddress(), bean.getNote(), bean
						.getNumberPages(), bean.getLanguage(), bean.getMonth(), bean.getYearBegin(), bean
						.getMonthBegin(), bean.getUrl());
	}

	protected Manual createManualFromBean(ManualBean bean) {
		return new Manual(bean.getPerson(), bean.getTitle(), bean.getKeywords(), bean.getOrganization(), bean
				.getYear(), bean.getAddress(), bean.getNote(), bean.getEdition(), bean.getMonth(), bean
				.getUrl());
	}

	protected TechnicalReport createTechnicalReportFromBean(TechnicalReportBean bean) {
		return new TechnicalReport(bean.getPerson(), bean.getTitle(), bean.getKeywords(),
				bean.getOrganization(), bean.getYear(), bean.getTechnicalReportType(), bean.getNumber(), bean
						.getAddress(), bean.getNote(), bean.getNumberPages(), bean.getLanguage(), bean
						.getMonth(), bean.getUrl());
	}

	protected OtherPublication createOtherPublicationFromBean(OtherPublicationBean bean) {
		return new OtherPublication(bean.getPerson(), bean.getTitle(), bean.getKeywords(),
				bean.getPublisher(), bean.getYear(), bean.getHowPublished(), bean.getNote(),
				bean.getAddress(), bean.getOtherPublicationType(), bean.getNumberPages(), bean.getLanguage(),
				bean.getCountry(), bean.getMonth(), bean.getUrl());
	}

	}

