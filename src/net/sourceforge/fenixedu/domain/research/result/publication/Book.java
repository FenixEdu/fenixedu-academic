package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

/**
 * A book with an explicit publisher.
 * Required fields: author or editor, title, publisher, year.
 * Optional fields: volume, series, address, edition, month, note.
 * 
 * Extra from previous publications: isbn, numberPages, country, language, scope
 */
public class Book extends Book_Base {
    
    public Book() {
        super();
    }
    
    //constructor with required fields
    public Book(Person participator, String title, Unit publisher, Integer year) {
        super();
        if((participator == null) || (title == null) || (publisher == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setParticipation(participator);
        setTitle(title);
        setPublisher(publisher);
        setYear(year);
    }
    
    //edit with required fields
    public void edit(String title, Unit publisher, Integer year) {
        if((title == null) || (publisher == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setPublisher(publisher);
        setYear(year);
    }
}
