package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
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

    public enum ThesisType {
	PHD_THESIS, MASTERS_THESIS;
    }

    public Thesis() {
	super();
    }

    public Thesis(Person participator, ThesisType thesisType, String title, Unit school, Integer year,
	    String address, String note, Integer numberPages, String language, Month month, String url) {
	this();
	checkRequiredParameters(thesisType, title, school, year);
	super.setParticipation(participator, ResultParticipationRole.Author);
	fillAllAttributes(thesisType, title, school, year, address, note, numberPages, language, month,
		url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(ThesisType thesisType, String title, Unit school, Integer year,
	    String address, String note, Integer numberPages, String language, Month month, String url){
	checkRequiredParameters(thesisType, title, school, year);
	fillAllAttributes(thesisType, title, school, year, address, note, numberPages, language, month,
		url);
	super.setModifyedByAndDate();
    }

    private void fillAllAttributes(ThesisType thesisType, String title, Unit school, Integer year,
	    String address, String note, Integer numberPages, String language, Month month, String url) {
	super.setTitle(title);
	super.setThesisType(thesisType);
	super.setOrganization(school);
	super.setYear(year);
	super.setAddress(address);
	super.setNote(note);
	super.setNumberPages(numberPages);
	super.setLanguage(language);
	super.setMonth(month);
	super.setUrl(url);
    }

    private void checkRequiredParameters(ThesisType thesisType, String title, Unit school, Integer year) {
	if ((title == null) || (title.length() == 0))
	    throw new DomainException("error.researcher.Thesis.title.null");
	if (thesisType == null) 
	    throw new DomainException("error.researcher.Thesis.thesisType.null");
	if (school == null)
	    throw new DomainException("error.researcher.Thesis.school.null");
	if (year == null)
	    throw new DomainException("error.researcher.Thesis.year.null");
    }
    
    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.Thesis.call","setTitle");
    }
    
    @Override
    public void setThesisType(ThesisType thesisType) {
	throw new DomainException("error.researcher.Thesis.call","setThesisType");
    }
    
    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Thesis.call","setYear");
    }
    
    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.Thesis.call","setAddress");
    }
    
    @Override
    public void setNote(String note) {
	throw new DomainException("error.researcher.Thesis.call","setNote");
    }
    
    @Override
    public void setNumberPages(Integer numberPages) {
	throw new DomainException("error.researcher.Thesis.call","setNumberPages");
    }
    
    @Override
    public void setLanguage(String language) {
	throw new DomainException("error.researcher.Thesis.call","setLanguage");
    }
    
    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Thesis.call","setMonth");
    }
    
    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Thesis.call","setUrl");
    }
    
    @Override
    public void setPublisher(Unit publisher) {
	throw new DomainException("error.researcher.Thesis.call","setPublisher");
    }
    
    @Override
    public void setOrganization(Unit organization) {
	throw new DomainException("error.researcher.Thesis.call","setOrganization");
    }
    
    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Thesis.call","setCountry");
    }
    
    @Override
    public void setParticipation(Person creator, ResultParticipationRole role) {
	throw new DomainException("error.researcher.Thesis.call","setParticipation");
    }
}
