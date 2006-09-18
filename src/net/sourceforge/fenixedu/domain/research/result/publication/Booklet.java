package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * A work that is printed and bound, but without a named publisher or sponsoring institution.
 * Required fields: title.
 * Optional fields: author, howpublished, address, month, year, note.
 */
public class Booklet extends Booklet_Base {
    
    public Booklet() {
        super();
    }
    
    public Booklet(Person participator, String title, String howPublished, Integer year, Month month, String address,
            String note, String url) {
        this();
        checkRequiredParameters(title);
        super.setCreatorParticipation(participator, ResultParticipationRole.Author);
        fillAllAttributes(title, howPublished, year, month, address, note, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, String howPublished, Integer year, Month month, String address,
            String note, String url) {
        checkRequiredParameters(title);
        fillAllAttributes(title, howPublished, year, month, address, note, url);
        super.setModifyedByAndDate();
    }
    
    private void fillAllAttributes(String title, String howPublished, Integer year, Month month, String address,
            String note, String url) {
	super.setTitle(title);
	super.setHowPublished(howPublished);
	super.setYear(year);
	super.setMonth(month);
	super.setAddress(address);
	super.setNote(note);
	super.setUrl(url);
    }
    
    private void checkRequiredParameters(String title) {
	if((title == null) || (title.length() == 0))
            throw new DomainException("error.researcher.Booklet.title.null");
    }
    
    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.Booklet.call","setTitle");
    }
    
    @Override
    public void setHowPublished(String howPublished) {
	throw new DomainException("error.researcher.Booklet.call","setHowPublished");
    }
    
    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Booklet.call","setYear");
    }
    
    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Booklet.call","setMonth");
    }
    
    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.Booklet.call","setAddress");
    }
    
    @Override
    public void setNote(String note) {
	throw new DomainException("error.researcher.Booklet.call","setNote");
    }
    
    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Booklet.call","setUrl");
    }
    
    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Booklet.call","setCountry");
    }
    
    @Override
    public void setOrganization(Unit organization) {
	throw new DomainException("error.researcher.Booklet.call","setOrganization");
    }
    
    @Override
    public void setPublisher(Unit publisher) {
	throw new DomainException("error.researcher.Booklet.call","setPublisher");
    } 
}
