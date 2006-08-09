package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.event.Event;

/**
 * (collection: A collection of works. Same as Proceedings.)
 * The proceedings of a conference.
 * Required fields: title, year.
 * Optional fields: editor, publisher, organization, address, month, note.
 * 
 * Extra from previous publications: conference
 */
public class Proceedings extends Proceedings_Base {
    
    public  Proceedings() {
        super();
    }
    
    //constructor with required fields, we need a participator
    public Proceedings(Person participator, String title, Integer year, Event event) {
        super();
        if((participator == null) || (title == null) || (title.length() == 0) || (year == null) || (event == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setParticipation(participator);
        setTitle(title);
        setYear(year);
        setEvent(event);
    }
    
    //edit with required fields
    public void edit(String title, Integer year, Event event) {
        if((title == null) || (title.length() == 0) || (year == null) || (event == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setYear(year);
        setEvent(event);
    }
    
}
