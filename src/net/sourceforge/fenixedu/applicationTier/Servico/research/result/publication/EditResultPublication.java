package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.*;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.publication.*;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;

public class EditResultPublication extends ResultPublicationService {

    public void run(BookBean bookBean) throws FenixServiceException {
        if ((bookBean == null) || (bookBean.getIdInternal() == null))
            throw new FenixServiceException();

        Book book = (Book) rootDomainObject.readResultByOID(bookBean.getIdInternal());
        if (book == null)
            throw new FenixServiceException();

        // edit Book with required fields;
        book.edit(bookBean.getTitle(), getPublisher(bookBean), bookBean.getYear());

        // fill optional fields
        book.setVolume(bookBean.getVolume());
        book.setSeries(bookBean.getSeries());
        book.setAddress(bookBean.getAddress());
        book.setEdition(bookBean.getEdition());
        book.setIsbn(bookBean.getIsbn());
        book.setNumberPages(bookBean.getNumberPages());
        book.setLanguage(bookBean.getLanguage());
        book.setCountry(bookBean.getCountry());
        book.setScope(bookBean.getScope());
        book.setNote(bookBean.getNote());
        book.setMonth(bookBean.getMonth());
        book.setUrl(bookBean.getUrl());
    }

    public void run(BookPartBean bookPartBean) throws FenixServiceException {
        if ((bookPartBean == null) || (bookPartBean.getIdInternal() == null))
            throw new FenixServiceException();

        BookPart bookPart = (BookPart) rootDomainObject.readResultByOID(bookPartBean.getIdInternal());
        if (bookPart == null)
            throw new FenixServiceException();

        if (bookPartBean.getBookPartType().equals(BookPartType.Inbook))
            editInbook(bookPart, bookPartBean);
        else
            editIncollection(bookPart, bookPartBean);
    }

    private void editIncollection(BookPart bookPart, BookPartBean bookPartBean) {
        // edit Incollection with required fields;
        bookPart.edit(bookPartBean.getBookPartType(), bookPartBean.getTitle(), bookPartBean
                .getBookTitle(), getPublisher(bookPartBean), bookPartBean.getYear());

        // fill optional fields
        bookPart.setFirstPage(bookPartBean.getFirstPage());
        bookPart.setLastPage(bookPartBean.getLastPage());
        bookPart.setOrganization(getOrganization(bookPartBean));
        setBookPartCommonFields(bookPart, bookPartBean);
    }

    private void editInbook(BookPart bookPart, BookPartBean bookPartBean) {
        // edit Inbook with required fields;
        bookPart.edit(bookPartBean.getBookPartType(), bookPartBean.getTitle(),
                bookPartBean.getChapter(), bookPartBean.getFirstPage(), bookPartBean.getLastPage(),
                getPublisher(bookPartBean), bookPartBean.getYear());

        // fill optional fields
        bookPart.setVolume(bookPartBean.getVolume());
        bookPart.setSeries(bookPartBean.getSeries());
        bookPart.setEdition(bookPartBean.getEdition());
        setBookPartCommonFields(bookPart, bookPartBean);
    }

    private void setBookPartCommonFields(BookPart bookPart, BookPartBean bookPartBean) {
        bookPart.setCountry(bookPartBean.getCountry());
        bookPart.setAddress(bookPartBean.getAddress());
        bookPart.setNote(bookPartBean.getNote());
        bookPart.setMonth(bookPartBean.getMonth());
        bookPart.setUrl(bookPartBean.getUrl());
        bookPart.setModificationDateAndAuthor();
    }

    public void run(ArticleBean articleBean) throws FenixServiceException {
        if ((articleBean == null) || (articleBean.getIdInternal() == null))
            throw new FenixServiceException();

        Article article = (Article) rootDomainObject.readResultByOID(articleBean.getIdInternal());
        if (article == null)
            throw new FenixServiceException();

        // edit Article with required fields;
        article.edit(articleBean.getTitle(), articleBean.getJournal(), articleBean.getYear());

        // fill optional fields
        article.setVolume(articleBean.getVolume());
        article.setNumber(articleBean.getNumber());
        article.setFirstPage(articleBean.getFirstPage());
        article.setLastPage(articleBean.getLastPage());
        article.setNote(articleBean.getNote());
        article.setIssn(articleBean.getIssn());
        article.setLanguage(articleBean.getLanguage());
        article.setCountry(articleBean.getCountry());
        article.setScope(articleBean.getScope());
        article.setPublisher(getPublisher(articleBean));
        article.setMonth(articleBean.getMonth());
        article.setUrl(articleBean.getUrl());
    }

    public void run(InproceedingsBean inproceedingsBean) throws FenixServiceException {
        if ((inproceedingsBean == null) || (inproceedingsBean.getIdInternal() == null))
            throw new FenixServiceException();

        Inproceedings inproceedings = (Inproceedings) rootDomainObject.readResultByOID(inproceedingsBean
                .getIdInternal());
        if (inproceedings == null)
            throw new FenixServiceException();

        Event event = inproceedingsBean.getEvent();
        if (event == null) {
            event = new Event(inproceedingsBean.getEventEndDate(),
                    inproceedingsBean.getEventStartDate(), inproceedingsBean.getEventLocal(),
                    inproceedingsBean.getEventFee(), inproceedingsBean.getEventType(), inproceedingsBean
                            .getEventName());
        }

        // edit Inproceedings with required fields;
        inproceedings.edit(inproceedingsBean.getTitle(), inproceedingsBean.getBookTitle(),
                inproceedingsBean.getYear(), event);

        // fill optional fields
        inproceedings.setPublisher(getPublisher(inproceedingsBean));
        inproceedings.setAddress(inproceedingsBean.getAddress());
        inproceedings.setOrganization(getOrganization(inproceedingsBean));
        inproceedings.setFirstPage(inproceedingsBean.getFirstPage());
        inproceedings.setLastPage(inproceedingsBean.getLastPage());
        inproceedings.setNote(inproceedingsBean.getNote());
        inproceedings.setLanguage(inproceedingsBean.getLanguage());
        inproceedings.setMonth(inproceedingsBean.getMonth());
        inproceedings.setUrl(inproceedingsBean.getUrl());
    }

    public void run(ProceedingsBean proceedingsBean) throws FenixServiceException {
        if ((proceedingsBean == null) || (proceedingsBean.getIdInternal() == null))
            throw new FenixServiceException();

        Proceedings proceedings = (Proceedings) rootDomainObject.readResultByOID(proceedingsBean
                .getIdInternal());
        if (proceedings == null)
            throw new FenixServiceException();

        Event event = proceedingsBean.getEvent();
        if (event == null) {
            event = new Event(proceedingsBean.getEventEndDate(), proceedingsBean.getEventStartDate(),
                    proceedingsBean.getEventLocal(), proceedingsBean.getEventFee(), proceedingsBean
                            .getEventType(), proceedingsBean.getEventName());
        }

        // edit Proceedings with required fields;
        proceedings.edit(proceedingsBean.getTitle(), proceedingsBean.getYear(), event);

        // fill optional fields
        proceedings.setPublisher(getPublisher(proceedingsBean));
        proceedings.setAddress(proceedingsBean.getAddress());
        proceedings.setOrganization(getOrganization(proceedingsBean));
        proceedings.setNote(proceedingsBean.getNote());
        proceedings.setMonth(proceedingsBean.getMonth());
        proceedings.setUrl(proceedingsBean.getUrl());
    }

    public void run(ThesisBean thesisBean) throws FenixServiceException {
        if ((thesisBean == null) || (thesisBean.getIdInternal() == null))
            throw new FenixServiceException();

        Thesis thesis = (Thesis) rootDomainObject.readResultByOID(thesisBean.getIdInternal());
        if (thesis == null)
            throw new FenixServiceException();

        // edit Thesis with required fields;
        thesis.edit(thesisBean.getThesisType(), thesisBean.getTitle(), getOrganization(thesisBean),
                thesisBean.getYear());

        // fill optional fields
        thesis.setAddress(thesisBean.getAddress());
        thesis.setNote(thesisBean.getNote());
        thesis.setNumberPages(thesisBean.getNumberPages());
        thesis.setLanguage(thesisBean.getLanguage());
        thesis.setMonth(thesisBean.getMonth());
        thesis.setUrl(thesisBean.getUrl());
    }

    public void run(ManualBean manualBean) throws FenixServiceException {
        if ((manualBean == null) || (manualBean.getIdInternal() == null))
            throw new FenixServiceException();

        Manual manual = (Manual) rootDomainObject.readResultByOID(manualBean.getIdInternal());
        if (manual == null)
            throw new FenixServiceException();

        // edit Manual with required fields;
        manual.edit(manualBean.getTitle());

        // fill optional fields
        manual.setOrganization(getOrganization(manualBean));
        manual.setYear(manualBean.getYear());
        manual.setAddress(manualBean.getAddress());
        manual.setNote(manualBean.getNote());
        manual.setEdition(manualBean.getEdition());
        manual.setNote(manualBean.getNote());
        manual.setMonth(manualBean.getMonth());
        manual.setUrl(manualBean.getUrl());
    }

    public void run(TechnicalReportBean technicalReportBean) throws FenixServiceException {
        if ((technicalReportBean == null) || (technicalReportBean.getIdInternal() == null))
            throw new FenixServiceException();

        TechnicalReport technicalReport = (TechnicalReport) rootDomainObject
                .readResultByOID(technicalReportBean.getIdInternal());
        if (technicalReport == null)
            throw new FenixServiceException();

        // edit TechnicalReport with required fields;
        technicalReport.edit(technicalReportBean.getTitle(), getOrganization(technicalReportBean),
                technicalReportBean.getYear());

        // fill optional fields
        technicalReport.setTechnicalReportType(technicalReportBean.getTechnicalReportType());
        technicalReport.setNumber(technicalReportBean.getNumber());
        technicalReport.setAddress(technicalReportBean.getAddress());
        technicalReport.setNote(technicalReportBean.getNote());
        technicalReport.setNumberPages(technicalReportBean.getNumberPages());
        technicalReport.setLanguage(technicalReportBean.getLanguage());
        technicalReport.setMonth(technicalReportBean.getMonth());
        technicalReport.setUrl(technicalReportBean.getUrl());
    }

    public void run(BookletBean bookletBean) throws FenixServiceException {
        if ((bookletBean == null) || (bookletBean.getIdInternal() == null))
            throw new FenixServiceException();

        Booklet booklet = (Booklet) rootDomainObject.readResultByOID(bookletBean.getIdInternal());
        if (booklet == null)
            throw new FenixServiceException();

        // edit Booklet with required fields;
        booklet.edit(bookletBean.getTitle());

        // fill optional fields
        booklet.setHowPublished(bookletBean.getHowPublished());
        booklet.setYear(bookletBean.getYear());
        booklet.setAddress(bookletBean.getAddress());
        booklet.setNote(bookletBean.getNote());
        booklet.setMonth(bookletBean.getMonth());
        booklet.setUrl(bookletBean.getUrl());
    }

    public void run(MiscBean miscBean) throws FenixServiceException {
        if ((miscBean == null) || (miscBean.getIdInternal() == null))
            throw new FenixServiceException();

        Misc misc = (Misc) rootDomainObject.readResultByOID(miscBean.getIdInternal());
        if (misc == null)
            throw new FenixServiceException();

        // edit Misc with required fields;
        misc.edit(miscBean.getTitle());

        // fill optional fields
        misc.setPublisher(getPublisher(miscBean));
        misc.setYear(miscBean.getYear());
        misc.setHowPublished(miscBean.getHowPublished());
        misc.setNote(miscBean.getNote());
        misc.setAddress(miscBean.getAddress());
        misc.setNote(miscBean.getNote());
        misc.setOtherPublicationType(miscBean.getOtherPublicationType());
        misc.setNumberPages(miscBean.getNumberPages());
        misc.setCountry(miscBean.getCountry());
        misc.setLanguage(miscBean.getLanguage());
        misc.setMonth(miscBean.getMonth());
        misc.setUrl(miscBean.getUrl());
    }

    public void run(UnpublishedBean unpublishedBean) throws FenixServiceException {
        if ((unpublishedBean == null) || (unpublishedBean.getIdInternal() == null))
            throw new FenixServiceException();

        Unpublished unpublished = (Unpublished) rootDomainObject.readResultByOID(unpublishedBean
                .getIdInternal());
        if (unpublished == null)
            throw new FenixServiceException();

        // edit Unpublished with required fields;
        unpublished.edit(unpublishedBean.getTitle(), unpublishedBean.getNote());

        // fill optional fields
        unpublished.setYear(unpublishedBean.getYear());
        unpublished.setMonth(unpublishedBean.getMonth());
        unpublished.setUrl(unpublishedBean.getUrl());
    }
}
