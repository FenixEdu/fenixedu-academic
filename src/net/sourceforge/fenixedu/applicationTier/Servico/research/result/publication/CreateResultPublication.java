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
        book.setNonRequiredAttributes(bookBean.getVolume(), bookBean.getSeries(), bookBean.getAddress(),
                bookBean.getEdition(), bookBean.getIsbn(), bookBean.getNumberPages(), bookBean
                        .getLanguage(), bookBean.getCountry(), bookBean.getScope(), bookBean.getNote(),
                bookBean.getMonth(), bookBean.getUrl());

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
        bookPart.setIncollectionNonRequiredAttributes(bookPartBean.getFirstPage(), bookPartBean
                .getLastPage(), getOrganization(bookPartBean), bookPartBean.getCountry(), bookPartBean
                .getAddress(), bookPartBean.getNote(), bookPartBean.getMonth(), bookPartBean.getUrl());
        return bookPart;
    }

    private BookPart createInbook(BookPartBean bookPartBean) {
        // create Inbook with required fields;
        BookPart bookPart = new BookPart(bookPartBean.getPerson(), bookPartBean
                .getResultParticipationRole(), bookPartBean.getBookPartType(), bookPartBean.getTitle(),
                bookPartBean.getChapter(), bookPartBean.getFirstPage(), bookPartBean.getLastPage(),
                getPublisher(bookPartBean), bookPartBean.getYear());

        // fill optional fields
        bookPart.setInbookNonRequiredAttributes(bookPartBean.getVolume(), bookPartBean.getSeries(),
                bookPartBean.getEdition(), bookPartBean.getCountry(), bookPartBean.getAddress(),
                bookPartBean.getNote(), bookPartBean.getMonth(), bookPartBean.getUrl());
        return bookPart;
    }

    public Article run(ArticleBean articleBean) {
        if (articleBean == null)
            throw new NullPointerException();

        // create Article with required fields;
        Article article = new Article(articleBean.getPerson(), articleBean.getTitle(), articleBean
                .getJournal(), articleBean.getYear());

        // fill optional fields
        article.setNonRequiredAttributes(getPublisher(articleBean), articleBean.getVolume(), articleBean
                .getNumber(), articleBean.getFirstPage(), articleBean.getLastPage(), articleBean
                .getNote(), articleBean.getIssn(), articleBean.getLanguage(), articleBean.getCountry(),
                articleBean.getScope(), articleBean.getMonth(), articleBean.getUrl());

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
        inproceedings.setNonRequiredAttributes(getPublisher(inproceedingsBean),
                getOrganization(inproceedingsBean), inproceedingsBean.getAddress(), inproceedingsBean
                        .getFirstPage(), inproceedingsBean.getLastPage(), inproceedingsBean.getNote(),
                inproceedingsBean.getLanguage(), inproceedingsBean.getMonth(), inproceedingsBean
                        .getUrl());

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
        proceedings.setNonRequiredAttributes(getPublisher(proceedingsBean),
                getOrganization(proceedingsBean), proceedingsBean.getAddress(), proceedingsBean
                        .getNote(), proceedingsBean.getMonth(), proceedingsBean.getUrl());

        return proceedings;
    }

    public Thesis run(ThesisBean thesisBean) {
        if (thesisBean == null)
            throw new NullPointerException();

        // create Thesis with required fields;
        Thesis thesis = new Thesis(thesisBean.getPerson(), thesisBean.getThesisType(), thesisBean
                .getTitle(), getOrganization(thesisBean), thesisBean.getYear());

        // fill optional fields
        thesis.setNonRequiredAttributes(thesisBean.getAddress(), thesisBean.getNote(), thesisBean
                .getNumberPages(), thesisBean.getLanguage(), thesisBean.getMonth(), thesisBean.getUrl());

        return thesis;
    }

    public Manual run(ManualBean manualBean) {
        if (manualBean == null)
            throw new NullPointerException();

        // create Manual with required fields;
        Manual manual = new Manual(manualBean.getPerson(), manualBean.getTitle());

        // fill optional fields
        manual.setNonRequiredAttributes(getOrganization(manualBean), manualBean.getYear(), manualBean
                .getAddress(), manualBean.getNote(), manualBean.getEdition(), manualBean.getMonth(),
                manualBean.getUrl());

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
        technicalReport.setNonRequiredAttributes(technicalReportBean.getTechnicalReportType(),
                technicalReportBean.getNumber(), technicalReportBean.getAddress(), technicalReportBean
                        .getNote(), technicalReportBean.getNumberPages(), technicalReportBean
                        .getLanguage(), technicalReportBean.getMonth(), technicalReportBean.getUrl());

        return technicalReport;
    }

    public Booklet run(BookletBean bookletBean) {
        if (bookletBean == null)
            throw new NullPointerException();

        // create Booklet with required fields;
        Booklet booklet = new Booklet(bookletBean.getPerson(), bookletBean.getTitle());

        // fill optional fields
        booklet.setNonRequiredAttributes(bookletBean.getHowPublished(), bookletBean.getYear(),
                bookletBean.getMonth(), bookletBean.getAddress(), bookletBean.getNote(), bookletBean
                        .getUrl());

        return booklet;
    }

    public Misc run(MiscBean miscBean) {
        if (miscBean == null)
            throw new NullPointerException();

        // create Misc with required fields;
        Misc misc = new Misc(miscBean.getPerson(), miscBean.getTitle());

        // fill optional fields
        misc.setNonRequiredAttributes(getPublisher(miscBean), miscBean.getYear(), miscBean
                .getHowPublished(), miscBean.getNote(), miscBean.getAddress(), miscBean
                .getOtherPublicationType(), miscBean.getNumberPages(), miscBean.getLanguage(), miscBean
                .getCountry(), miscBean.getMonth(), miscBean.getUrl());

        return misc;
    }

    public Unpublished run(UnpublishedBean unpublishedBean) {
        if (unpublishedBean == null)
            throw new NullPointerException();

        // create Unpublished with required fields;
        Unpublished unpublished = new Unpublished(unpublishedBean.getPerson(), unpublishedBean
                .getTitle(), unpublishedBean.getNote());

        // fill optional fields
        unpublished.setNonRequiredAttributes(unpublishedBean.getYear(), unpublishedBean.getMonth(),
                unpublishedBean.getUrl());

        return unpublished;
    }
}
