package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * A document with an author and title, but not formally published.
 * Required fields: author, title, note.
 * Optional fields: month, year.
 */
public class Unpublished extends Unpublished_Base {

    public Unpublished() {
	super();
    }

    public Unpublished(Person participator, String title, String note, Integer year, Month month,
	    String url) {
	this();
	checkRequiredParameters(title, note);
	super.setCreatorParticipation(participator, ResultParticipationRole.Author);
	fillAllAttributes(title, note, year, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, String note, Integer year, Month month, String url) {
	checkRequiredParameters(title, note);
	fillAllAttributes(title, note, year, month, url);
	super.setModifyedByAndDate();
    }

    private void fillAllAttributes(String title, String note, Integer year, Month month, String url) {
	super.setTitle(title);
	super.setNote(note);
	super.setYear(year);
	super.setMonth(month);
	super.setUrl(url);
    }

    private void checkRequiredParameters(String title, String note) {
	if ((title == null) || (title.length() == 0))
	    throw new DomainException("error.researcher.Unpublished.title.null");
	if ((note == null) || (note.length() == 0))
	    throw new DomainException("error.researcher.Unpublished.note.null");
    }
    
    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.Unpublished.call","setTitle");
    }
    
    @Override
    public void setNote(String note) {
	throw new DomainException("error.researcher.Unpublished.call","setNote");
    }
    
    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Unpublished.call","setYear");
    }
    
    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Unpublished.call","setMonth");
    }
    
    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Unpublished.call","setUrl");
    }
    
    @Override
    public void setPublisher(Unit publisher) {
	throw new DomainException("error.researcher.Unpublished.call","setPublisher");
    }
    
    @Override
    public void setOrganization(Unit organization) {
	throw new DomainException("error.researcher.Unpublished.call","setOrganization");
    }
    
    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Unpublished.call","setCountry");
    }
}
