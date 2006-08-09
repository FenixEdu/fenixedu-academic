package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

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
        if((participator == null) || (title == null) || (title.length() == 0) || (note == null) || (note.length() == 0))
            throw new DomainException("error.publication.missingRequiredFields");
        
        //by default any participation is of type Author
        setParticipation(participator, ResultParticipationRole.Author);
        setTitle(title);
        setNote(note);
    }
    
    //edit with required fields
    public void edit(String title, String note) {
        if((title == null) || (title.length() == 0) || (note == null) || (note.length() == 0))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setNote(note);
    }
    
}
