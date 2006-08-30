package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

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

    public Book(Person participator, ResultParticipationRole participatorRole, String title,
	    Unit publisher, Integer year, String volume, String series, String address, String edition,
	    Integer isbn, Integer numberPages, String language, Country country, ScopeType scope,
	    String note, Month month, String url) {
	this();
	checkRequiredParameters(title, publisher, year);
	super.setParticipation(participator, participatorRole);
	fillAllAttributes(title, publisher, year, volume, series, address, edition, isbn, numberPages,
		language, country, scope, note, month, url);
    }
    
    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, Unit publisher, Integer year, String volume, String series,
	    String address, String edition, Integer isbn, Integer numberPages, String language,
	    Country country, ScopeType scope, String note, Month month, String url) {
	checkRequiredParameters(title, publisher, year);
	fillAllAttributes(title, publisher, year, volume, series, address, edition, isbn, numberPages,
		language, country, scope, note, month, url);
	super.setModifyedByAndDate();
    }

    private void fillAllAttributes(String title, Unit publisher, Integer year, String volume,
	    String series, String address, String edition, Integer isbn, Integer numberPages,
	    String language, Country country, ScopeType scope, String note, Month month, String url) {
	super.setTitle(title);
	super.setPublisher(publisher);
	super.setYear(year);
	super.setVolume(volume);
	super.setSeries(series);
	super.setAddress(address);
	super.setEdition(edition);
	super.setIsbn(isbn);
	super.setNumberPages(numberPages);
	super.setLanguage(language);
	super.setCountry(country);
	super.setScope(scope);
	super.setNote(note);
	super.setMonth(month);
	super.setUrl(url);
    }

    private void checkRequiredParameters(String title, Unit publisher, Integer year) {
	if (title == null || title.length() == 0)
	    throw new DomainException("error.researcher.Book.title.null");
	if(publisher==null)
	    throw new DomainException("error.researcher.Book.publisher.null");
	if(year==null)
	    throw new DomainException("error.researcher.Book.year.null");
    }
    
    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.Book.call","setTitle");
    }

    @Override
    public void setPublisher(Unit publisher) {
	throw new DomainException("error.researcher.Book.call","setPublisher");    
    }

    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Book.call","setYear");
    }
    
    @Override
    public void setVolume(String volume) {
	throw new DomainException("error.researcher.Book.call","setVolume");
    }
    
    @Override
    public void setSeries(String series) {
	throw new DomainException("error.researcher.Book.call","setSeries");
    }
    
    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.Book.call","setAddress");
    }
    
    @Override
    public void setEdition(String edition) {
	throw new DomainException("error.researcher.Book.call","setEdition");    
    }
    
    @Override
    public void setNote(String note) {
	throw new DomainException("error.researcher.Book.call","setNote");
    }
    
    @Override
    public void setIsbn(Integer isbn) {
	throw new DomainException("error.researcher.Book.call","setIsbn");
    }
    
    @Override
    public void setLanguage(String language) {
	throw new DomainException("error.researcher.Book.call","setLanguage");
    }
    
    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Book.call","setCountry");
    }
    
    @Override
    public void setScope(ScopeType scope) {
	throw new DomainException("error.researcher.Book.call","setScope");
    }
    
    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Book.call","setUrl");
    }
    
    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Book.call","setMonth");    
    }
    
    @Override
    public void setNumberPages(Integer numberPages) {
	throw new DomainException("error.researcher.Book.call","setNumberPages");
    }
    
    @Override
    public void setOrganization(Unit organization) {
	throw new DomainException("error.researcher.Book.call","setOrganization");
    }
    
    @Override
    protected void setModifyedByAndDate() {
	throw new DomainException("error.researcher.Book.call","setModifyedByAndDate");
    }
    
    @Override
    public void setParticipation(Person creator, ResultParticipationRole role) {
	throw new DomainException("error.researcher.Book.call","setParticipation");
    }
}
