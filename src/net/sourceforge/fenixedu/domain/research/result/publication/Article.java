package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

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
        if((participator == null) || (title == null) || (title.length() == 0) 
        	|| (journal == null) || (journal.length() == 0) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        //by default any participation is of type Author
        setParticipation(participator, ResultParticipationRole.Author);
        setTitle(title);
        setJournal(journal);
        setYear(year);
    }
    
    //edit with required fields
    public void edit(String title, String journal, Integer year) {
        if((title == null) || (title.length() == 0) || (journal == null) || (journal.length() == 0) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setJournal(journal);
        setYear(year);
    }
    
    public void setNonRequiredAttributes(Unit publisher, String volume, String number, Integer firstPage, Integer lastPage,
            String note, Integer issn, String language, Country country, ScopeType scope, Month month, String url) {
        this.setPublisher(publisher);
        this.setVolume(volume);
        this.setNumber(number);
        this.setFirstPage(firstPage);
        this.setLastPage(lastPage);
        this.setNote(note);
        this.setIssn(issn);
        this.setLanguage(language);
        this.setCountry(country);
        this.setScope(scope);
        this.setMonth(month);
        this.setUrl(url);
    }
}
