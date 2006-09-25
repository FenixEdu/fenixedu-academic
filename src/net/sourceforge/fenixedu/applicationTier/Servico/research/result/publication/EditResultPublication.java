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

    public void run(BookBean bean) throws FenixServiceException {
        final Book book = (Book) getResultPublication(bean);

        book.setEditAll(bean.getTitle(), getPublisher(bean), bean.getYear(), bean.getVolume(), bean
                .getSeries(), bean.getAddress(), bean.getEdition(), bean.getIsbn(), bean
                .getNumberPages(), bean.getLanguage(), bean.getCountry(), bean.getScope(), bean
                .getNote(), bean.getMonth(), bean.getUrl());
    }

    public void run(InbookBean bean) throws FenixServiceException {
        final BookPart bookPart = (BookPart) getResultPublication(bean);

        bookPart.setEditAllInbook(BookPartType.Inbook, bean.getTitle(), bean.getChapter(), bean
                .getFirstPage(), bean.getLastPage(), getPublisher(bean), bean.getYear(), bean
                .getVolume(), bean.getSeries(), bean.getEdition(), bean.getCountry(), bean.getAddress(),
                bean.getNote(), bean.getMonth(), bean.getUrl());

    }

    public void run(IncollectionBean bean) throws FenixServiceException {
        final BookPart bookPart = (BookPart) getResultPublication(bean);

        bookPart.setEditAllIncollection(BookPartType.Incollection, bean.getTitle(), bean.getBookTitle(),
                getPublisher(bean), bean.getYear(), bean.getFirstPage(), bean.getLastPage(),
                getOrganization(bean), bean.getCountry(), bean.getAddress(), bean.getNote(), bean
                        .getMonth(), bean.getUrl());
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

        inproceedings.setEditAll(bean.getTitle(), bean.getYear(), event, bean.getScope(),
                getPublisher(bean), getOrganization(bean), bean.getAddress(), bean.getFirstPage(), bean
                        .getLastPage(), bean.getNote(), bean.getLanguage(), bean.getMonth(), bean
                        .getUrl());
    }

    public void run(ProceedingsBean bean) throws FenixServiceException {
        final Proceedings proceedings = (Proceedings) getResultPublication(bean);
        final Event event = getEventFromBean(bean);

        proceedings.setEditAll(bean.getTitle(), bean.getYear(), event, bean.getScope(),
                getPublisher(bean), getOrganization(bean), bean.getAddress(), bean.getNote(), bean
                        .getMonth(), bean.getUrl());
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

    public void run(OtherPublicationBean bean) throws FenixServiceException {
        final OtherPublication otherPublication = (OtherPublication) getResultPublication(bean);

        otherPublication.setEditAll(bean.getTitle(), getPublisher(bean), bean.getYear(), bean
                .getHowPublished(), bean.getNote(), bean.getAddress(), bean.getOtherPublicationType(),
                bean.getNumberPages(), bean.getLanguage(), bean.getCountry(), bean.getMonth(), bean
                        .getUrl());
    }

    private ResultPublication getResultPublication(ResultPublicationBean bean)
            throws FenixServiceException {
        if ((bean == null) || (bean.getIdInternal() == null))
            throw new FenixServiceException();

        final ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(bean
                .getIdInternal());
        if (publication == null)
            throw new FenixServiceException();
        return publication;
    }
}
