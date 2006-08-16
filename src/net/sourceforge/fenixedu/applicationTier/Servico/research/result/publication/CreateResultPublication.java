package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.*;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.publication.*;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;

public class CreateResultPublication extends ResultPublicationService {

    public Book run(BookBean bookBean) {
        if (bookBean == null)
            throw new NullPointerException();

        // create Book with required fields;
        Book book = new Book(bookBean.getPerson(), bookBean.getResultParticipationRole(), bookBean
                .getTitle(), getPublisher(bookBean), bookBean.getYear());

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
        book.setModificationDateAndAuthor();

        return book;
    }

    public BookPart run(BookPartBean bookPartBean) {
        if (bookPartBean == null)
            throw new NullPointerException();

        BookPart bookPart = null;
        if (bookPartBean.getBookPartType().equals(BookPartType.Inbook))
            bookPart = createInbook(bookPartBean);
        else
            bookPart = createIncollection(bookPartBean);

        return bookPart;
    }

    private BookPart createIncollection(BookPartBean bookPartBean) {
        // create Incollection with required fields;
        BookPart bookPart = new BookPart(bookPartBean.getPerson(), bookPartBean
                .getResultParticipationRole(), bookPartBean.getBookPartType(), bookPartBean.getTitle(),
                bookPartBean.getBookTitle(), getPublisher(bookPartBean), bookPartBean.getYear());

        // fill optional fields
        bookPart.setFirstPage(bookPartBean.getFirstPage());
        bookPart.setLastPage(bookPartBean.getLastPage());
        bookPart.setOrganization(getOrganization(bookPartBean));
        setBookPartCommonFields(bookPart, bookPartBean);
        return bookPart;
    }

    private BookPart createInbook(BookPartBean bookPartBean) {
        // create Inbook with required fields;
        BookPart bookPart = new BookPart(bookPartBean.getPerson(), bookPartBean
                .getResultParticipationRole(), bookPartBean.getBookPartType(), bookPartBean.getTitle(),
                bookPartBean.getChapter(), bookPartBean.getFirstPage(), bookPartBean.getLastPage(),
                getPublisher(bookPartBean), bookPartBean.getYear());

        // fill optional fields
        bookPart.setVolume(bookPartBean.getVolume());
        bookPart.setSeries(bookPartBean.getSeries());
        bookPart.setEdition(bookPartBean.getEdition());
        setBookPartCommonFields(bookPart, bookPartBean);
        return bookPart;
    }

    private void setBookPartCommonFields(BookPart bookPart, BookPartBean bookPartBean) {
        bookPart.setCountry(bookPartBean.getCountry());
        bookPart.setAddress(bookPartBean.getAddress());
        bookPart.setNote(bookPartBean.getNote());
        bookPart.setMonth(bookPartBean.getMonth());
        bookPart.setUrl(bookPartBean.getUrl());
        bookPart.setModificationDateAndAuthor();
    }

    public Article run(ArticleBean articleBean) {
        if (articleBean == null)
            throw new NullPointerException();

        // create Article with required fields;
        Article article = new Article(articleBean.getPerson(), articleBean.getTitle(), articleBean
                .getJournal(), articleBean.getYear());

        // fill optional fields
        article.setPublisher(getPublisher(articleBean));
        article.setVolume(articleBean.getVolume());
        article.setNumber(articleBean.getNumber());
        article.setFirstPage(articleBean.getFirstPage());
        article.setLastPage(articleBean.getLastPage());
        article.setNote(articleBean.getNote());
        article.setIssn(articleBean.getIssn());
        article.setLanguage(articleBean.getLanguage());
        article.setCountry(articleBean.getCountry());
        article.setScope(articleBean.getScope());
        article.setMonth(articleBean.getMonth());
        article.setUrl(articleBean.getUrl());
        article.setModificationDateAndAuthor();

        return article;
    }

    public Inproceedings run(InproceedingsBean inproceedingsBean) {
        if (inproceedingsBean == null)
            throw new NullPointerException();

        Event event = inproceedingsBean.getEvent();
        if (event == null) {
            event = new Event(inproceedingsBean.getEventEndDate(),
                    inproceedingsBean.getEventStartDate(), inproceedingsBean.getEventLocal(),
                    inproceedingsBean.getEventFee(), inproceedingsBean.getEventType(), inproceedingsBean
                            .getEventName());
        }
        // create Inproceedings with required fields;
        Inproceedings inproceedings = new Inproceedings(inproceedingsBean.getPerson(), inproceedingsBean
                .getResultParticipationRole(), inproceedingsBean.getTitle(), inproceedingsBean
                .getBookTitle(), inproceedingsBean.getYear(), event);

        // fill optional fields
        inproceedings.setPublisher(getPublisher(inproceedingsBean));
        inproceedings.setOrganization(getOrganization(inproceedingsBean));
        inproceedings.setAddress(inproceedingsBean.getAddress());
        inproceedings.setFirstPage(inproceedingsBean.getFirstPage());
        inproceedings.setLastPage(inproceedingsBean.getLastPage());
        inproceedings.setNote(inproceedingsBean.getNote());
        inproceedings.setLanguage(inproceedingsBean.getLanguage());
        inproceedings.setMonth(inproceedingsBean.getMonth());
        inproceedings.setUrl(inproceedingsBean.getUrl());
        inproceedings.setModificationDateAndAuthor();

        return inproceedings;
    }

    public Proceedings run(ProceedingsBean proceedingsBean) {
        if (proceedingsBean == null)
            throw new NullPointerException();

        Event event = proceedingsBean.getEvent();
        if (event == null) {
            event = new Event(proceedingsBean.getEventEndDate(), proceedingsBean.getEventStartDate(),
                    proceedingsBean.getEventLocal(), proceedingsBean.getEventFee(), proceedingsBean
                            .getEventType(), proceedingsBean.getEventName());
        }

        // create Proceedings with required fields;
        Proceedings proceedings = new Proceedings(proceedingsBean.getPerson(), proceedingsBean
                .getTitle(), proceedingsBean.getYear(), event);

        // fill optional fields
        proceedings.setPublisher(getPublisher(proceedingsBean));
        proceedings.setOrganization(getOrganization(proceedingsBean));
        proceedings.setAddress(proceedingsBean.getAddress());
        proceedings.setNote(proceedingsBean.getNote());
        proceedings.setMonth(proceedingsBean.getMonth());
        proceedings.setUrl(proceedingsBean.getUrl());
        proceedings.setModificationDateAndAuthor();

        return proceedings;
    }

    public Thesis run(ThesisBean thesisBean) {
        if (thesisBean == null)
            throw new NullPointerException();

        // create Thesis with required fields;
        Thesis thesis = new Thesis(thesisBean.getPerson(), thesisBean.getThesisType(), thesisBean
                .getTitle(), getOrganization(thesisBean), thesisBean.getYear());

        // fill optional fields
        thesis.setAddress(thesisBean.getAddress());
        thesis.setNote(thesisBean.getNote());
        thesis.setNumberPages(thesisBean.getNumberPages());
        thesis.setLanguage(thesisBean.getLanguage());
        thesis.setMonth(thesisBean.getMonth());
        thesis.setUrl(thesisBean.getUrl());
        thesis.setModificationDateAndAuthor();

        return thesis;
    }

    public Manual run(ManualBean manualBean) {
        if (manualBean == null)
            throw new NullPointerException();

        // create Manual with required fields;
        Manual manual = new Manual(manualBean.getPerson(), manualBean.getTitle());

        // fill optional fields
        manual.setOrganization(getOrganization(manualBean));
        manual.setYear(manualBean.getYear());
        manual.setAddress(manualBean.getAddress());
        manual.setNote(manualBean.getNote());
        manual.setEdition(manualBean.getEdition());
        manual.setNote(manualBean.getNote());
        manual.setMonth(manualBean.getMonth());
        manual.setUrl(manualBean.getUrl());
        manual.setModificationDateAndAuthor();

        return manual;
    }

    public TechnicalReport run(TechnicalReportBean technicalReportBean) {
        if (technicalReportBean == null)
            throw new NullPointerException();

        // create TechnicalReport with required fields;
        TechnicalReport technicalReport = new TechnicalReport(technicalReportBean.getPerson(),
                technicalReportBean.getTitle(), getOrganization(technicalReportBean),
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
        technicalReport.setModificationDateAndAuthor();

        return technicalReport;
    }

    public Booklet run(BookletBean bookletBean) {
        if (bookletBean == null)
            throw new NullPointerException();

        // create Booklet with required fields;
        Booklet booklet = new Booklet(bookletBean.getPerson(), bookletBean.getTitle());

        // fill optional fields
        booklet.setHowPublished(bookletBean.getHowPublished());
        booklet.setYear(bookletBean.getYear());
        booklet.setMonth(bookletBean.getMonth());
        booklet.setAddress(bookletBean.getAddress());
        booklet.setNote(bookletBean.getNote());
        booklet.setUrl(bookletBean.getUrl());
        booklet.setModificationDateAndAuthor();

        return booklet;
    }

    public Misc run(MiscBean miscBean) {
        if (miscBean == null)
            throw new NullPointerException();

        // create Misc with required fields;
        Misc misc = new Misc(miscBean.getPerson(), miscBean.getTitle());

        // fill optional fields
        misc.setPublisher(getPublisher(miscBean));
        misc.setYear(miscBean.getYear());
        misc.setHowPublished(miscBean.getHowPublished());
        misc.setNote(miscBean.getNote());
        misc.setAddress(miscBean.getAddress());
        misc.setNote(miscBean.getNote());
        misc.setOtherPublicationType(miscBean.getOtherPublicationType());
        misc.setNumberPages(miscBean.getNumberPages());
        misc.setLanguage(miscBean.getLanguage());
        misc.setCountry(miscBean.getCountry());
        misc.setMonth(miscBean.getMonth());
        misc.setUrl(miscBean.getUrl());
        misc.setModificationDateAndAuthor();

        return misc;
    }

    public Unpublished run(UnpublishedBean unpublishedBean) {
        if (unpublishedBean == null)
            throw new NullPointerException();

        // create Unpublished with required fields;
        Unpublished unpublished = new Unpublished(unpublishedBean.getPerson(), unpublishedBean
                .getTitle(), unpublishedBean.getNote());
        // fill optional fields
        unpublished.setYear(unpublishedBean.getYear());
        unpublished.setMonth(unpublishedBean.getMonth());
        unpublished.setUrl(unpublishedBean.getUrl());
        unpublished.setModificationDateAndAuthor();

        return unpublished;
    }
}
