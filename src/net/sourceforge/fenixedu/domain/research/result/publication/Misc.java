package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * Use this type when nothing else fits.
 * Required fields: none.
 * Optional fields: author, title, howpublished, month, year, note.
 * 
 * Extra from previous publications: otherPublicationType, numberPages, language, publisher, address
 */
public class Misc extends Misc_Base {
    
    public Misc() {
        super();
    }

    //constructor with required fields, we need to have a title and a author
    public Misc(Person participator, String title) {
        super();
        if((participator == null) || (title == null) || (title.length() == 0))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setParticipation(participator);
        setTitle(title);
    }
    
    //edit with required fields, we need to have a title
    public void edit(String title) {
        if((title == null) || (title.length() == 0))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
    }
}
