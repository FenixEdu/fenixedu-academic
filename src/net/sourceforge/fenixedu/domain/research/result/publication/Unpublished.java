package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * A document with an author and title, but not formally published.
 * Required fields: author, title, note.
 * Optional fields: month, year.
 */
public class Unpublished extends Unpublished_Base {
    
    public Unpublished() {
        super();
    }
    
    //constructor with required fields
    public Unpublished(Person participator, String title, String note) {
        super();
        if((participator == null) || (title == null) || (note == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setParticipation(participator);
        setTitle(title);
        setNote(note);
    }
    
    //edit with required fields
    public void edit(String title, String note) {
        if((title == null) || (note == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setNote(note);
    }
    
}
