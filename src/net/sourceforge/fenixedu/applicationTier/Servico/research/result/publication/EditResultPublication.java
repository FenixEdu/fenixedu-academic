package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.publication.*;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditResultPublication extends Service {

    public void run(ResultPublicationCreationBean publicationBean) throws ExcepcaoPersistencia, FenixServiceException{
        if((publicationBean == null) || (publicationBean.getIdInternal() == null))
            throw new FenixServiceException();
        
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(publicationBean.getIdInternal());
        if(publication == null)
            throw new FenixServiceException();
        
        switch (publicationBean.getPublicationType()) {
        case Book: {
            Unit publisher = publicationBean.getPublisher();
            if (publisher == null)
                publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
            // edit Book with required fields;
            Book book = (Book) publication;
            book.edit(publicationBean.getTitle(), publisher, publicationBean.getYear());
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
        }break;
        case BookPart: {
            Unit publisher = publicationBean.getPublisher();
            if (publisher == null)
                publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
            // edit BookPart with required fields;
            BookPart bookPart = (BookPart) publication;
            switch(publicationBean.getBookPartType())
            {
                case Inbook:
                {
                    bookPart.edit(publicationBean.getBookPartType(), publicationBean.getTitle(),
                            publicationBean.getChapter(), publicationBean.getFirstPage(), publicationBean.getLastPage(), publisher, publicationBean.getYear());
                    bookPart.setVolume(publicationBean.getVolume());
                    bookPart.setSeries(publicationBean.getSeries());
                    bookPart.setEdition(publicationBean.getEdition());
                    
                }
                break;
                case Incollection:
                {
                    bookPart.edit(publicationBean.getBookPartType(), publicationBean.getTitle(),
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
        }break;
        case Article: {
            // edit Article with required fields;
            Article article = (Article) publication;
            article.edit(publicationBean.getTitle(), publicationBean.getJournal(), publicationBean.getYear());
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
        }break;
        case Inproceedings: {
            // edit Inproceedings with required fields;
            Inproceedings inproceedings = (Inproceedings) publication;
            inproceedings.edit(publicationBean.getTitle(), publicationBean.getBookTitle(), publicationBean.getYear());
            // fill optional fields
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
        }break;
        case Proceedings: {
            // edit Proceedings with required fields;
            Proceedings proceedings = (Proceedings) publication;
            proceedings.edit(publicationBean.getTitle(), publicationBean.getYear());
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
        }break;
        case Thesis: {
            Unit school = publicationBean.getOrganization();
            if (school == null)
                school = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
            // edit Thesis with required fields;
            Thesis thesis = (Thesis) publication;
            thesis.edit(publicationBean.getThesisType(), publicationBean.getTitle(), school, publicationBean.getYear());
            // fill optional fields
            thesis.setAddress(publicationBean.getAddress());
            thesis.setNote(publicationBean.getNote());
            thesis.setNumberPages(publicationBean.getNumberPages());
            thesis.setLanguage(publicationBean.getLanguage());
        }break;
        case Manual: {
            // edit Manual with required fields;
            Manual manual = (Manual) publication;
            manual.edit(publicationBean.getTitle());
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
        }break;
        case TechnicalReport: {
            Unit institution = publicationBean.getOrganization();
            if (institution == null)
                institution = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
            // edit TechnicalReport with required fields;
            TechnicalReport technicalReport = (TechnicalReport) publication;
            technicalReport.edit(publicationBean.getTitle(), institution, publicationBean.getYear());
            // fill optional fields
            technicalReport.setTechnicalReportType(publicationBean.getTechnicalReportType());
            technicalReport.setNumber(publicationBean.getNumber());
            technicalReport.setAddress(publicationBean.getAddress());
            technicalReport.setNote(publicationBean.getNote());
            technicalReport.setNumberPages(publicationBean.getNumberPages());
            technicalReport.setLanguage(publicationBean.getLanguage());
        }break;
        case Booklet: {
            // edit Booklet with required fields;
            Booklet booklet = (Booklet) publication;
            booklet.edit(publicationBean.getTitle());
            // fill optional fields
            booklet.setHowPublished(publicationBean.getHowPublished());
            booklet.setYear(publicationBean.getYear());
            booklet.setAddress(publicationBean.getAddress());
            booklet.setNote(publicationBean.getNote());
        }break;
        case Misc: {
            // edit Misc with required fields;
            Misc misc = (Misc) publication;
            misc.edit(publicationBean.getTitle());
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
        }break;
        case Unpublished: {
            // edit Unpublished with required fields;
            Unpublished unpublished = (Unpublished) publication;
            unpublished.edit(publicationBean.getTitle(), publicationBean.getNote());
            // fill optional fields
            unpublished.setYear(publicationBean.getYear());
        }break;
        }

        //common fields
        publication.setMonth(publicationBean.getMonth());
        publication.setUrl(publicationBean.getUrl());
        
    }
}
