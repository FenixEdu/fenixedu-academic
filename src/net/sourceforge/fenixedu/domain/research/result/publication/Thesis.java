package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * mastersthesis
 *   A Master's thesis.
 *   Required fields: author, title, school, year.
 *   Optional fields: address, month, note.
 * phdthesis
 *   A Ph.D. thesis.
 *   Required fields: author, title, school, year.
 *   Optional fields: address, month, note.
 *   
 *   Extra from previous publications: numberPages, language
 */
public class Thesis extends Thesis_Base {
    
    public  Thesis() {
        super();
    }

    //constructor with required fields
    public Thesis(Person participator, ThesisType thesisType, String title, Unit school, Integer year) {
        super();
        if((participator == null) || (title == null) || (title.length() == 0) || (thesisType == null) || (school == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        //by default any participation is of type Author
        setParticipation(participator, ResultParticipationRole.Author);
        setTitle(title);
        setThesisType(thesisType);
        setOrganization(school);
        setYear(year);
    }
    
    //edit with required fields
    public void edit(ThesisType thesisType, String title, Unit school, Integer year) {
        if((title == null) || (title.length() == 0) || (thesisType == null) || (school == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setThesisType(thesisType);
        setOrganization(school);
        setYear(year);
    }
    
    public void setNonRequiredAttributes(String address, String note, Integer numberPages,
            String language, Month month, String url) {

        this.setAddress(address);
        this.setNote(note);
        this.setNumberPages(numberPages);
        this.setLanguage(language);
        this.setMonth(month);
        this.setUrl(url);
    }
    
    public enum ThesisType {
        PHD_THESIS,
        MASTERS_THESIS;
    }
}
