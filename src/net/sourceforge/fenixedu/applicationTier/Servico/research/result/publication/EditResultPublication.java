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
        book.setNonRequiredAttributes(bookBean.getVolume(), bookBean.getSeries(), bookBean.getAddress(),
                bookBean.getEdition(), bookBean.getIsbn(), bookBean.getNumberPages(), bookBean
                        .getLanguage(), bookBean.getCountry(), bookBean.getScope(), bookBean.getNote(),
                bookBean.getMonth(), bookBean.getUrl());
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
        bookPart.setIncollectionNonRequiredAttributes(bookPartBean.getFirstPage(), bookPartBean
                .getLastPage(), getOrganization(bookPartBean), bookPartBean.getCountry(), bookPartBean
                .getAddress(), bookPartBean.getNote(), bookPartBean.getMonth(), bookPartBean.getUrl());
    }

    private void editInbook(BookPart bookPart, BookPartBean bookPartBean) {
        // edit Inbook with required fields;
        bookPart.edit(bookPartBean.getBookPartType(), bookPartBean.getTitle(),
                bookPartBean.getChapter(), bookPartBean.getFirstPage(), bookPartBean.getLastPage(),
                getPublisher(bookPartBean), bookPartBean.getYear());

        // fill optional fields
        bookPart.setInbookNonRequiredAttributes(bookPartBean.getVolume(), bookPartBean.getSeries(),
                bookPartBean.getEdition(), bookPartBean.getCountry(), bookPartBean.getAddress(),
                bookPartBean.getNote(), bookPartBean.getMonth(), bookPartBean.getUrl());
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
        article.setNonRequiredAttributes(getPublisher(articleBean), articleBean.getVolume(), articleBean
                .getNumber(), articleBean.getFirstPage(), articleBean.getLastPage(), articleBean
                .getNote(), articleBean.getIssn(), articleBean.getLanguage(), articleBean.getCountry(),
                articleBean.getScope(), articleBean.getMonth(), articleBean.getUrl());
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
        inproceedings.setNonRequiredAttributes(getPublisher(inproceedingsBean),
                getOrganization(inproceedingsBean), inproceedingsBean.getAddress(), inproceedingsBean
                        .getFirstPage(), inproceedingsBean.getLastPage(), inproceedingsBean.getNote(),
                inproceedingsBean.getLanguage(), inproceedingsBean.getMonth(), inproceedingsBean
                        .getUrl());
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
        proceedings.setNonRequiredAttributes(getPublisher(proceedingsBean),
                getOrganization(proceedingsBean), proceedingsBean.getAddress(), proceedingsBean
                        .getNote(), proceedingsBean.getMonth(), proceedingsBean.getUrl());
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
        thesis.setNonRequiredAttributes(thesisBean.getAddress(), thesisBean.getNote(), thesisBean
                .getNumberPages(), thesisBean.getLanguage(), thesisBean.getMonth(), thesisBean.getUrl());
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
        manual.setNonRequiredAttributes(getOrganization(manualBean), manualBean.getYear(), manualBean
                .getAddress(), manualBean.getNote(), manualBean.getEdition(), manualBean.getMonth(),
                manualBean.getUrl());
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
        technicalReport.setNonRequiredAttributes(technicalReportBean.getTechnicalReportType(),
                technicalReportBean.getNumber(), technicalReportBean.getAddress(), technicalReportBean
                        .getNote(), technicalReportBean.getNumberPages(), technicalReportBean
                        .getLanguage(), technicalReportBean.getMonth(), technicalReportBean.getUrl());
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
        booklet.setNonRequiredAttributes(bookletBean.getHowPublished(), bookletBean.getYear(),
                bookletBean.getMonth(), bookletBean.getAddress(), bookletBean.getNote(), bookletBean
                        .getUrl());
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
        misc.setNonRequiredAttributes(getPublisher(miscBean), miscBean.getYear(), miscBean
                .getHowPublished(), miscBean.getNote(), miscBean.getAddress(), miscBean
                .getOtherPublicationType(), miscBean.getNumberPages(), miscBean.getLanguage(), miscBean
                .getCountry(), miscBean.getMonth(), miscBean.getUrl());
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
        unpublished.setNonRequiredAttributes(unpublishedBean.getYear(), unpublishedBean.getMonth(),
                unpublishedBean.getUrl());
    }
}
