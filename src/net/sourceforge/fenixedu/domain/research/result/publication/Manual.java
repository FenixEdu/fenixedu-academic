package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * Technical documentation.
 * Required fields: title.
 * Optional fields: author, organization, address, edition, month, year, note.
 */
public class Manual extends Manual_Base {
    
    public  Manual() {
        super();
    }
    
    public Manual(Person participator, String title,Unit organization, Integer year, String address, String note,
            String edition, Month month, String url) {
        this();
        checkRequiredParameters(title);
        super.setCreatorParticipation(participator, ResultParticipationRole.Author);
        fillAllAttributes(title, organization, year, address, note, edition, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title,Unit organization, Integer year, String address, String note,
            String edition, Month month, String url) {
	checkRequiredParameters(title);
	fillAllAttributes(title, organization, year, address, note, edition, month, url);
	super.setModifyedByAndDate();
    }
    
    private void fillAllAttributes(String title,Unit organization, Integer year, String address, String note,
            String edition, Month month, String url) {
	super.setTitle(title);
	super.setOrganization(organization);
	super.setYear(year);
	super.setAddress(address);
	super.setNote(note);
	super.setEdition(edition);
	super.setMonth(month);
	super.setUrl(url);
    }

    private void checkRequiredParameters(String title) {
	if((title == null) || (title.length() == 0))
            throw new DomainException("error.researcher.Manual.title.null");
    }
    
    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.Manual.call","setTitle");
    }
    
    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Manual.call","setYear");
    }
    
    @Override
    public void setPublisher(Unit publisher) {
	throw new DomainException("error.researcher.Manual.call","setPublisher");
    }
    
    @Override
    public void setOrganization(Unit organization) {
	throw new DomainException("error.researcher.Manual.call","setOrganization");
    }
    
    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.Manual.call","setAddress");
    }
    
    @Override
    public void setNote(String note) {
	throw new DomainException("error.researcher.Manual.call","setNote");
    }
   
    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Manual.call","setMonth");
    }
    
    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Manual.call","setUrl");
    }
    
    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Manual.call","setCountry");
    }
    
    @Override
    public void setEdition(String edition) {
	throw new DomainException("error.researcher.Manual.call","setEdition");
    }
}
