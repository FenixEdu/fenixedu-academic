package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * (conference: The same as Inproceedings.)
 * An article in a conference proceedings.
 * Required fields: author, title, booktitle, year.
 * Optional fields: editor, pages, organization, publisher, address, month, note.
 * 
 * Extra from previous publications: event, language
 */
public class Inproceedings extends Inproceedings_Base {

    public Inproceedings() {
	super();
    }

    public Inproceedings(Person participator, ResultParticipationRole participatorRole, String title,
	    String bookTitle, Integer year, Event event, Unit publisher, Unit organization,
	    String address, Integer firstPage, Integer lastPage, String note, String language,
	    Month month, String url) {
	this();
	checkRequiredParameters(title, bookTitle, year, event);
	super.setParticipation(participator, participatorRole);
	fillAllAttributes(title, bookTitle, year, event, publisher, organization, address, firstPage,
		lastPage, note, language, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, String bookTitle, Integer year, Event event, Unit publisher,
	    Unit organization, String address, Integer firstPage, Integer lastPage, String note,
	    String language, Month month, String url) {
	checkRequiredParameters(title, bookTitle, year, event);
	fillAllAttributes(title, bookTitle, year, event, publisher, organization, address, firstPage,
		lastPage, note, language, month, url);
	super.setModifyedByAndDate();
    }

    private void fillAllAttributes(String title, String bookTitle, Integer year, Event event,
	    Unit publisher, Unit organization, String address, Integer firstPage, Integer lastPage,
	    String note, String language, Month month, String url) {
	super.setTitle(title);
	super.setBookTitle(bookTitle);
	super.setYear(year);
	super.setEvent(event);
	super.setPublisher(publisher);
	super.setOrganization(organization);
	super.setAddress(address);
	super.setFirstPage(firstPage);
	super.setLastPage(lastPage);
	super.setNote(note);
	super.setLanguage(language);
	super.setMonth(month);
	super.setUrl(url);
    }

    private void checkRequiredParameters(String title, String bookTitle, Integer year, Event event) {
	if ((title == null) || (title.length() == 0))
	    throw new DomainException("error.researcher.Inproceedings.title.null");
	if ((bookTitle == null) || (bookTitle.length() == 0))
	    throw new DomainException("error.researcher.Inproceedings.bookTitle.null");
	if (year == null) 
	    throw new DomainException("error.researcher.Inproceedings.year.null");
	if (event == null)
	    throw new DomainException("error.researcher.Inproceedings.event.null");
    }
    
    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.Inproceedings.call","setTitle");
    }
    
    @Override
    public void setBookTitle(String bookTitle) {
	throw new DomainException("error.researcher.Inproceedings.call","setBookTitle");
    }
    
    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Inproceedings.call","setYear");
    }
    
    @Override
    public void setEvent(Event event) {
	throw new DomainException("error.researcher.Inproceedings.call","setEvent");
    }
    
    @Override
    public void setPublisher(Unit publisher) {
	throw new DomainException("error.researcher.Inproceedings.call","setPublisher");
    }
    
    @Override
    public void setOrganization(Unit organization) {
	throw new DomainException("error.researcher.Inproceedings.call","setOrganization");
    }
    
    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.Inproceedings.call","setAddress");
    }
    
    @Override
    public void setFirstPage(Integer firstPage) {
	throw new DomainException("error.researcher.Inproceedings.call","setFirstPage");
    }
    
    @Override
    public void setLastPage(Integer lastPage) {
	throw new DomainException("error.researcher.Inproceedings.call","setLastPage");
    }
    
    @Override
    public void setNote(String note) {
	throw new DomainException("error.researcher.Inproceedings.call","setNote");
    }
    
    @Override
    public void setLanguage(String language) {
	throw new DomainException("error.researcher.Inproceedings.call","setLanguage");
    }
    
    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Inproceedings.call","setMonth");
    }
    
    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Inproceedings.call","setUrl");
    }
    
    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Inproceedings.call","setCountry");
    }
    
    @Override
    protected void setModifyedByAndDate() {
	throw new DomainException("error.researcher.Inproceedings.call","setModifyedByAndDate");
    }
    
    @Override
    public void setParticipation(Person creator, ResultParticipationRole role) {
	throw new DomainException("error.researcher.Inproceedings.call","setParticipation");
    }
}
