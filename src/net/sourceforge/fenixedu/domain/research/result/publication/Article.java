package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 *  An article from a journal or magazine.
 *  Required fields: author, title, journal, year. 
 *  Optional fields: volume, number, pages, month, note.
 *  
 *  Extra from previous publications: issn, language, publisher, country, scope
 */
public class Article extends Article_Base {

    public Article() {
        super();
    }
    
    //constructor with required fields
    public Article(Person participator, String title, String journal, Integer year) {
        super();
        if((participator == null) || (title == null) || (journal == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setParticipation(participator);
        setTitle(title);
        setJournal(journal);
        setYear(year);
    }
    
    //edit with required fields
    public void edit(String title, String journal, Integer year) {
        if((title == null) || (journal == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setJournal(journal);
        setYear(year);
    }
}
