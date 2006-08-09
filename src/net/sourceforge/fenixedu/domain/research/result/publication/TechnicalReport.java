package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

/**
 * A report published by a school or other institution, usually numbered within a series.
 * Required fields: author, title, institution, year.
 * Optional fields: type, number, address, month, note.
 * 
 * Extra from previous publications: numberPages, language
 */
public class TechnicalReport extends TechnicalReport_Base {
    
    public  TechnicalReport() {
        super();
    }
    
    //constructor with required fields
    public TechnicalReport(Person participator, String title, Unit institution, Integer year) {
        super();
        if((participator == null) || (title == null) || (title.length() == 0) || (institution == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        //by default any participation is of type Author
        setParticipation(participator, ResultParticipationRole.Author);
        setTitle(title);
        setOrganization(institution);
        setYear(year);
    }
    
    //edit with required fields
    public void edit(String title, Unit institution, Integer year) {
        if((title == null) || (title.length() == 0) || (institution == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setOrganization(institution);
        setYear(year);
    }
}
