package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

/**
 * (conference: The same as Inproceedings.)
 * An article in a conference proceedings.
 * Required fields: author, title, booktitle, year.
 * Optional fields: editor, pages, organization, publisher, address, month, note.
 * 
 * Extra from previous publications: event, language
 */
public class Inproceedings extends Inproceedings_Base {
    
    public  Inproceedings() {
        super();
    }
    
    //constructor with required fields
    public Inproceedings(Person participator, ResultParticipationRole participatorRole, String title, String bookTitle, Integer year, Event event) {
        super();
        if((participator == null) || (participatorRole == null) || (title == null) || (title.length() == 0) || (bookTitle == null)
        		|| (bookTitle.length() == 0) || (year == null) || (event == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setParticipation(participator, participatorRole);
        setTitle(title);
        setBookTitle(bookTitle);
        setYear(year);
        setEvent(event);
    }
    
    //edit with required fields
    public void edit(String title, String bookTitle, Integer year, Event event) {
        if((title == null) || (title.length() == 0) || (bookTitle == null) || (bookTitle.length() == 0) || (year == null) || (event == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setBookTitle(bookTitle);
        setYear(year);
        setEvent(event);
    }
}
