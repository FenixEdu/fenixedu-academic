package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * A report published by a school or other institution, usually numbered within a series.
 * Required fields: author, title, institution, year.
 * Optional fields: type, number, address, month, note.
 * 
 * Extra from previous publications: numberPages, language
 */
public class TechnicalReport extends TechnicalReport_Base {

    public TechnicalReport() {
	super();
    }

    public TechnicalReport(Person participator, String title, Unit institution, Integer year,
	    String technicalReportType, String number, String address, String note, Integer numberPages,
	    String language, Month month, String url) {
	this();
	checkRequiredParameters(title, institution, year);
	super.setParticipation(participator, ResultParticipationRole.Author);
	fillAllAttributes(title, institution, year, technicalReportType, number, address, note,
		numberPages, language, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, Unit institution, Integer year, String technicalReportType,
	    String number, String address, String note, Integer numberPages, String language,
	    Month month, String url) {
	checkRequiredParameters(title, institution, year);
	fillAllAttributes(title, institution, year, technicalReportType, number, address, note,
		numberPages, language, month, url);
	super.setModifyedByAndDate();
    }

    private void fillAllAttributes(String title, Unit institution, Integer year,
	    String technicalReportType, String number, String address, String note, Integer numberPages,
	    String language, Month month, String url) {
	super.setTitle(title);
	super.setOrganization(institution);
	super.setYear(year);
	super.setTechnicalReportType(technicalReportType);
	super.setNumber(number);
	super.setAddress(address);
	super.setNote(note);
	super.setNumberPages(numberPages);
	super.setLanguage(language);
	super.setMonth(month);
	super.setUrl(url);
    }

    private void checkRequiredParameters(String title, Unit institution, Integer year) {
	if ((title == null) || (title.length() == 0))
	    throw new DomainException("error.researcher.TechnicalReport.title.null");
	if (institution == null)
	    throw new DomainException("error.researcher.TechnicalReport.institution.null");
	if (year == null)
	    throw new DomainException("error.researcher.TechnicalReport.year.null");
    }
    
    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.TechnicalReport.call","setTitle");
    }
    
    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.TechnicalReport.call","setYear");
    }
    
    @Override
    public void setTechnicalReportType(String technicalReportType) {
	throw new DomainException("error.researcher.TechnicalReport.call","setTechnicalReportType");
    }
    
    @Override
    public void setNumber(String number) {
	throw new DomainException("error.researcher.TechnicalReport.call","setNumber");
    }
    
    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.TechnicalReport.call","setAddress");
    }
    
    @Override
    public void setNote(String note) {
	throw new DomainException("error.researcher.TechnicalReport.call","setNote");
    }
    
    @Override
    public void setNumberPages(Integer numberPages) {
	throw new DomainException("error.researcher.TechnicalReport.call","setNumberPages");
    }
    
    @Override
    public void setLanguage(String language) {
	throw new DomainException("error.researcher.TechnicalReport.call","setLanguage");
    }
    
    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.TechnicalReport.call","setMonth");
    }
    
    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.TechnicalReport.call","setUrl");
    }
    
    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.TechnicalReport.call","setCountry");
    }
    
    @Override
    public void setOrganization(Unit organization) {
	throw new DomainException("error.researcher.TechnicalReport.call","setOrganization");
    }
    
    @Override
    public void setPublisher(Unit publisher) {
	throw new DomainException("error.researcher.TechnicalReport.call","setPublisher");
    }
    
    @Override
    protected void setModifyedByAndDate() {
	throw new DomainException("error.researcher.TechnicalReport.call","setModifyedByAndDate");
    }
    
    @Override
    public void setParticipation(Person creator, ResultParticipationRole role) {
	throw new DomainException("error.researcher.TechnicalReport.call","setParticipation");
    }
}
