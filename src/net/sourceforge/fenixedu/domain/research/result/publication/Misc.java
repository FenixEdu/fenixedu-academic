package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

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
        
        //by default any participation is of type Author
        setParticipation(participator, ResultParticipationRole.Author);
        setTitle(title);
    }
    
    //edit with required fields, we need to have a title
    public void edit(String title) {
        if((title == null) || (title.length() == 0))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
    }
    
    public void setNonRequiredAttributes(Unit publisher, Integer year, String howPublished, String note,
            String address, String otherPublicationType, Integer numberPages, String language,
            Country country, Month month, String url) {

        this.setPublisher(publisher);
        this.setYear(year);
        this.setHowPublished(howPublished);
        this.setNote(note);
        this.setAddress(address);
        this.setOtherPublicationType(otherPublicationType);
        this.setNumberPages(numberPages);
        this.setLanguage(language);
        this.setCountry(country);
        this.setMonth(month);
        this.setUrl(url);
    }
}
