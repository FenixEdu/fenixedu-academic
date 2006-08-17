package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;


/**
 * Inbook
 *   A part of a book, which may be a chapter and/or a range of pages.
 *   Required fields: author or editor, title, chapter and/or pages, publisher, year.
 *   Optional fields: volume, series, address, edition, month, note.
 * Incollection
 *   A part of a book with its own title.
 *   Required fields: author, title, booktitle, publisher, year.
 *   Optional fields: editor, pages, organization, publisher, address, month, note.
 */
public class BookPart extends BookPart_Base {
    
    public BookPart() {
        super();
    }
    
    //constructor Inbook with required fields
    public BookPart(Person participator, ResultParticipationRole participatorRole, BookPartType bookPartType, String title, String chapter, Integer firstPage,
            Integer lastPage, Unit publisher, Integer year) {
        super();
        //expected Inbook Type
        if(!bookPartType.equals(BookPartType.Inbook))
            throw new DomainException("error.publication.bookPart.wrongBookPartType");
        if((participator == null) || (bookPartType == null) || (title == null) || (title.length() == 0) || (publisher == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        if((chapter == null) || (chapter.length() == 0))
        {
            if((firstPage == null) || (firstPage <= 0) || (lastPage == null) || (lastPage <= 0))
                throw new DomainException("error.publication.bookPart.needChapterOrPages ");
        }
        if(participatorRole == null)
            throw new DomainException("error.publication.neededResultParticipationRole");
        
        setParticipation(participator, participatorRole);
        setBookPartType(bookPartType);
        setTitle(title);
        setPublisher(publisher);
        setYear(year);
        setChapter(chapter);
        setFirstPage(firstPage);
        setLastPage(lastPage);
    }
    
    //constructor Incollection with required fields
    public BookPart(Person participator, ResultParticipationRole participatorRole, BookPartType bookPartType, String title, String bookTitle, Unit publisher, Integer year) {
        super();
        //expected Incollection Type
        if(!bookPartType.equals(BookPartType.Incollection))
            throw new DomainException("error.wrongBookPartType");
        if((participator == null) || (bookPartType == null) || (title == null) || (title.length() == 0) 
        		|| (bookTitle == null) || (bookTitle.length() == 0) || (publisher == null) || (year == null))
            throw new DomainException("error.missingRequiredFields");
        if(participatorRole == null)
            throw new DomainException("error.publication.neededResultParticipationRole");
        
        setParticipation(participator, participatorRole);
        setBookPartType(bookPartType);
        setTitle(title);
        setBookTitle(bookTitle);
        setPublisher(publisher);
        setYear(year);
    }
    
    //edit of Inbook with required fields
    public void edit(BookPartType bookPartType, String title, String chapter, Integer firstPage,
            Integer lastPage, Unit publisher, Integer year) {
        //expected Inbook Type
        if(!bookPartType.equals(BookPartType.Inbook))
            throw new DomainException("error.publication.bookPart.wrongBookPartType");
        if((bookPartType == null) || (title == null) || (title.length() == 0) || (publisher == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        if((chapter == null) || (chapter.length() == 0))
        {
            if((firstPage == null) || (firstPage <= 0) || (lastPage == null) || (lastPage <= 0))
                throw new DomainException("error.publication.bookPart.needChapterOrPages ");
        }
        
        if(!this.getBookPartType().equals(bookPartType))
        {
            //if changing type of BookPart remove fields that don't belong to the new type
            setBookTitle(null);
            removeOrganization();
        }
        
        setBookPartType(bookPartType);
        setTitle(title);
        setPublisher(publisher);
        setYear(year);
        setChapter(chapter);
        setFirstPage(firstPage);
        setLastPage(lastPage);
    }
    
    //edit of Incollection with required fields
    public void edit(BookPartType bookPartType, String title, String bookTitle, Unit publisher, Integer year) {
        //expected Incollection Type
        if(!bookPartType.equals(BookPartType.Incollection))
            throw new DomainException("error.wrongBookPartType");
        if((bookPartType == null) || (title == null) || (title.length() == 0) || (bookTitle == null) || (bookTitle.length() == 0)
        		|| (publisher == null) || (year == null))
            throw new DomainException("error.missingRequiredFields");
        
        if(!this.getBookPartType().equals(bookPartType))
        {
            //if changing type of BookPart remove fields that don't belong to the new type
            setChapter(null);
            setVolume(null);
            setSeries(null);
            setEdition(null);
        }
        
        setBookPartType(bookPartType);
        setTitle(title);
        setBookTitle(bookTitle);
        setPublisher(publisher);
        setYear(year);
    }
    
    public void setInbookNonRequiredAttributes(String volume, String series, String edition,
            Country country, String address, String note, Month month, String url) {
        this.setVolume(volume);
        this.setSeries(series);
        this.setEdition(edition);
        this.setCountry(country);
        this.setAddress(address);
        this.setNote(note);
        this.setMonth(month);
        this.setUrl(url);
    }

    public void setIncollectionNonRequiredAttributes(Integer firstPage, Integer lastPage,
            Unit organization, Country country, String address, String note, Month month, String url) {
        this.setFirstPage(firstPage);
        this.setLastPage(lastPage);
        this.setOrganization(organization);
        this.setCountry(country);
        this.setAddress(address);
        this.setNote(note);
        this.setMonth(month);
        this.setUrl(url);
    }

    public enum BookPartType {
        Inbook,
        Incollection;
    }
}
