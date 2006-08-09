package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

/**
 * A work that is printed and bound, but without a named publisher or sponsoring institution.
 * Required fields: title.
 * Optional fields: author, howpublished, address, month, year, note.
 */
public class Booklet extends Booklet_Base {
    
    public Booklet() {
        super();
    }
    
    //constructor with required fields
    public Booklet(Person participator, String title) {
        super();
        if((participator == null) || (title == null) || (title.length() == 0))
            throw new DomainException("error.publication.missingRequiredFields");
        
        //by default any participation is of type Author
        setParticipation(participator, ResultParticipationRole.Author);
        setTitle(title);
    }
    
    //edit with required fields
    public void edit(String title) {
        if((title == null) || (title.length() == 0))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
    }
}
