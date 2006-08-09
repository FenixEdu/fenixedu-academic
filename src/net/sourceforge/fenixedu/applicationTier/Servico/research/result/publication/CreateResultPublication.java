package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.publication.*;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultPublication extends Service {

    public ResultPublication run(ResultPublicationCreationBean publicationBean) throws ExcepcaoPersistencia, FenixServiceException {
        if(publicationBean == null)
            throw new FenixServiceException();
        
        ResultPublication resultPublication = null;
        switch (publicationBean.getPublicationType()) {
        case Book: {
            Unit publisher = publicationBean.getPublisher();
            if (publisher == null)
                publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
            // create Book with required fields;
            Book book = new Book(publicationBean.getParticipator(), publicationBean.getTitle(), publisher, publicationBean.getYear());
            // fill optional fields
            book.setVolume(publicationBean.getVolume());
            book.setSeries(publicationBean.getSeries());
            book.setAddress(publicationBean.getAddress());
            book.setEdition(publicationBean.getEdition());
            book.setIsbn(publicationBean.getIsbn());
            book.setNumberPages(publicationBean.getNumberPages());
            book.setLanguage(publicationBean.getLanguage());
            book.setScope(publicationBean.getScope());
            book.setNote(publicationBean.getNote());
            resultPublication = book;
        }break;
        case BookPart: {
            Unit publisher = publicationBean.getPublisher();
            if (publisher == null)
                publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
            // create BookPart with required fields;
            BookPart bookPart = null;
            switch(publicationBean.getBookPartType())
            {
                case Inbook:
                {
                    bookPart = new BookPart(publicationBean.getParticipator(), publicationBean.getBookPartType(), publicationBean.getTitle(),
                            publicationBean.getChapter(), publicationBean.getFirstPage(), publicationBean.getLastPage(), publisher, publicationBean.getYear());
                    bookPart.setVolume(publicationBean.getVolume());
                    bookPart.setSeries(publicationBean.getSeries());
                    bookPart.setEdition(publicationBean.getEdition());
                    
                }
                break;
                case Incollection:
                {
                    bookPart = new BookPart(publicationBean.getParticipator(), publicationBean.getBookPartType(), publicationBean.getTitle(),
                            publicationBean.getBookTitle(), publisher, publicationBean.getYear());
                    bookPart.setFirstPage(publicationBean.getFirstPage());
                    bookPart.setLastPage(publicationBean.getLastPage());
                    
                    Unit organization = publicationBean.getOrganization();
                    if (organization == null)
                    {
                        if((publicationBean.getOrganizationName() != null) && (publicationBean.getOrganizationName().length() != 0))
                            organization = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
                    }
                    bookPart.setOrganization(organization);
                }
                break;
            }
            bookPart.setAddress(publicationBean.getAddress());
            bookPart.setNote(publicationBean.getNote());
            resultPublication = bookPart;
        }break;
        case Article: {
            // create Article with required fields;
            Article article = new Article(publicationBean.getParticipator(), publicationBean.getTitle(), publicationBean.getJournal(), publicationBean.getYear());
            // fill optional fields
            article.setVolume(publicationBean.getVolume());
            article.setNumber(publicationBean.getNumber());
            article.setFirstPage(publicationBean.getFirstPage());
            article.setLastPage(publicationBean.getLastPage());
            article.setNote(publicationBean.getNote());
            article.setIssn(publicationBean.getIssn());
            article.setLanguage(publicationBean.getLanguage());
            article.setScope(publicationBean.getScope());
            
            Unit publisher = publicationBean.getPublisher();
            if (publisher == null)
            {
                if((publicationBean.getPublisherName() != null) && (publicationBean.getPublisherName().length() != 0))
                    publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
            }
            article.setPublisher(publisher);
            resultPublication = article;
        }break;
        case Inproceedings: {
        	Event event = publicationBean.getEvent();
        	if(event == null)
        	{
        		event = new Event(publicationBean.getEventEndDate(), publicationBean.getEventStartDate(), publicationBean.getEventLocal(),
        				publicationBean.getEventFee(), publicationBean.getEventType(), publicationBean.getEventName());
        	}
        	//create Inproceedings with required fields;
            Inproceedings inproceedings = new Inproceedings(publicationBean.getParticipator(), publicationBean.getTitle(), publicationBean.getBookTitle(), publicationBean.getYear(), event);
            //fill optional fields
            Unit publisher = publicationBean.getPublisher();
            if (publisher == null)
            {
                if((publicationBean.getPublisherName() != null) && (publicationBean.getPublisherName().length() != 0))
                    publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
            }
            inproceedings.setPublisher(publisher);
            inproceedings.setAddress(publicationBean.getAddress());
            
            Unit organization = publicationBean.getOrganization();
            if (organization == null)
            {
                if((publicationBean.getOrganizationName() != null) && (publicationBean.getOrganizationName().length() != 0))
                    organization = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
            }
            inproceedings.setOrganization(organization);
            
            inproceedings.setFirstPage(publicationBean.getFirstPage());
            inproceedings.setLastPage(publicationBean.getLastPage());
            inproceedings.setNote(publicationBean.getNote());
            inproceedings.setLanguage(publicationBean.getLanguage());
            resultPublication = inproceedings;
        }break;
        case Proceedings: {
        	Event event = publicationBean.getEvent();
        	if(event == null)
        	{
        		event = new Event(publicationBean.getEventEndDate(), publicationBean.getEventStartDate(), publicationBean.getEventLocal(),
        				publicationBean.getEventFee(), publicationBean.getEventType(), publicationBean.getEventName());
        	}
            // create Proceedings with required fields;
            Proceedings proceedings = new Proceedings(publicationBean.getParticipator(), publicationBean.getTitle(), publicationBean.getYear(), event);
            // fill optional fields
            Unit publisher = publicationBean.getPublisher();
            if (publisher == null)
            {
                if((publicationBean.getPublisherName() != null) && (publicationBean.getPublisherName().length() != 0))
                    publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
            }
            proceedings.setPublisher(publisher);
            proceedings.setAddress(publicationBean.getAddress());
            
            Unit organization = publicationBean.getOrganization();
            if (organization == null)
            {
                if((publicationBean.getOrganizationName() != null) && (publicationBean.getOrganizationName().length() != 0))
                    organization = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
            }
            proceedings.setOrganization(organization);

            proceedings.setNote(publicationBean.getNote());
            resultPublication = proceedings;
        }break;
        case Thesis: {
            Unit school = publicationBean.getOrganization();
            if (school == null)
                school = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
            // create Thesis with required fields;
            Thesis thesis = new Thesis(publicationBean.getParticipator(), publicationBean.getThesisType(), publicationBean.getTitle(), school, publicationBean.getYear());
            // fill optional fields
            thesis.setAddress(publicationBean.getAddress());
            thesis.setNote(publicationBean.getNote());
            thesis.setNumberPages(publicationBean.getNumberPages());
            thesis.setLanguage(publicationBean.getLanguage());
            resultPublication = thesis;
        }break;
        case Manual: {
            // create Manual with required fields;
            Manual manual = new Manual(publicationBean.getParticipator(), publicationBean.getTitle());
            // fill optional fields
            Unit organization = publicationBean.getOrganization();
            if (organization == null)
            {
                if((publicationBean.getOrganizationName() != null) && (publicationBean.getOrganizationName().length() != 0))
                    organization = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
            }
            manual.setOrganization(organization);
            manual.setYear(publicationBean.getYear());
            manual.setAddress(publicationBean.getAddress());
            manual.setNote(publicationBean.getNote());
            manual.setEdition(publicationBean.getEdition());
            manual.setNote(publicationBean.getNote());
            resultPublication = manual;
        }break;
        case TechnicalReport: {
            Unit institution = publicationBean.getOrganization();
            if (institution == null)
                institution = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
            // create TechnicalReport with required fields;
            TechnicalReport technicalReport = new TechnicalReport(publicationBean.getParticipator(), publicationBean.getTitle(), institution, publicationBean.getYear());
            // fill optional fields
            technicalReport.setTechnicalReportType(publicationBean.getTechnicalReportType());
            technicalReport.setNumber(publicationBean.getNumber());
            technicalReport.setAddress(publicationBean.getAddress());
            technicalReport.setNote(publicationBean.getNote());
            technicalReport.setNumberPages(publicationBean.getNumberPages());
            technicalReport.setLanguage(publicationBean.getLanguage());
            resultPublication = technicalReport;
        }break;
        case Booklet: {
            // create Booklet with required fields;
            Booklet booklet = new Booklet(publicationBean.getParticipator(), publicationBean.getTitle());
            // fill optional fields
            booklet.setHowPublished(publicationBean.getHowPublished());
            booklet.setYear(publicationBean.getYear());
            booklet.setAddress(publicationBean.getAddress());
            booklet.setNote(publicationBean.getNote());
            resultPublication = booklet;
        }break;
        case Misc: {
            // create Misc with required fields;
            Misc misc = new Misc(publicationBean.getParticipator(), publicationBean.getTitle());
            // fill optional fields
            Unit publisher = publicationBean.getPublisher();
            if (publisher == null)
            {
                if((publicationBean.getPublisherName() != null) && (publicationBean.getPublisherName().length() != 0))
                    publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
            }
            misc.setPublisher(publisher);
            misc.setYear(publicationBean.getYear());
            misc.setHowPublished(publicationBean.getHowPublished());
            misc.setNote(publicationBean.getNote());
            misc.setAddress(publicationBean.getAddress());
            misc.setNote(publicationBean.getNote());
            misc.setOtherPublicationType(publicationBean.getOtherPublicationType());
            misc.setNumberPages(publicationBean.getNumberPages());
            misc.setLanguage(publicationBean.getLanguage());
            resultPublication = misc;
        }break;
        case Unpublished: {
            // create Unpublished with required fields;
            Unpublished unpublished = new Unpublished(publicationBean.getParticipator(), publicationBean.getTitle(), publicationBean.getNote());
            // fill optional fields
            unpublished.setYear(publicationBean.getYear());
            resultPublication = unpublished;
        }break;
        }

        //common fields
        resultPublication.setMonth(publicationBean.getMonth());
        resultPublication.setUrl(publicationBean.getUrl());
        
        return resultPublication;
    }
}
