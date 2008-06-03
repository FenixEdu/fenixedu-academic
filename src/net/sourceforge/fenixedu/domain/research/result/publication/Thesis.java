package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificArea;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Month;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.YearMonthDay;

import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;

/**
 * mastersthesis A Master's thesis. Required fields: author, title, school,
 * year. Optional fields: address, month, note. phdthesis A Ph.D. thesis.
 * Required fields: author, title, school, year. Optional fields: address,
 * month, note.
 * 
 * Extra from previous publications: numberPages, language, yearBegin,
 * monthBegin
 * 
 * To represent school use organization (to avoid more relations)
 */
public class Thesis extends Thesis_Base {

	private static final String usedSchema="result.publication.presentation.Thesis";
    public enum ThesisType {
	PhD_Thesis, Masters_Thesis, Graduation_Thesis;
    }

    public Thesis() {
	super();
    }

    public Thesis(Person participator, ThesisType thesisType, String title, MultiLanguageString keywords, String school, Integer year,
	    String address, MultiLanguageString note, Integer numberPages, String language, Month month,
	    Integer yearBegin, Month monthBegin, String url) {
	this();
	super.checkRequiredParameters(keywords,note);
	checkRequiredParameters(thesisType, title, school, year);
	super.setCreatorParticipation(participator, ResultParticipationRole.Author);
	fillAllAttributes(thesisType, title, keywords, school, year, address, note, numberPages, language, month,
		yearBegin, monthBegin, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(ThesisType thesisType, String title, MultiLanguageString keywords, String school, Integer year,
	    String address, MultiLanguageString note, Integer numberPages, String language, Month month,
	    Integer yearBegin, Month monthBegin, String url) {
    super.checkRequiredParameters(keywords,note);
    checkRequiredParameters(thesisType, title, school, year);
	fillAllAttributes(thesisType, title, keywords, school, year, address, note, numberPages, language, month,
		yearBegin, monthBegin, url);
	super.setModifiedByAndDate();
    }

    private void fillAllAttributes(ThesisType thesisType, String title, MultiLanguageString keywords, String school, Integer year,
	    String address, MultiLanguageString note, Integer numberPages, String language, Month month,
	    Integer yearBegin, Month monthBegin, String url) {
	if(yearBegin!=null && (year<yearBegin || (year.compareTo(yearBegin)==0 && month.ordinal()<monthBegin.ordinal()))) {
		throw new DomainException("error.researcher.Thesis.dateBeginBeforeDateEnd");
	}
    super.setTitle(title);
	super.setThesisType(thesisType);
	super.setOrganization(school);
	super.setYear(year);
	super.setAddress(address);
	super.setNote(note);
	super.setNumberPages(numberPages);
	super.setLanguage(language);
	super.setMonth(month);
	super.setYearBegin(yearBegin);
	super.setMonthBegin(monthBegin);
	super.setUrl(url);
	super.setKeywords(keywords);
    }

    private void checkRequiredParameters(ThesisType thesisType, String title, String school, Integer year) {
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
    public String getResume() {
	String resume = getParticipationsAndTitleString();
	if (getThesisType() != null)
	    resume = resume + getThesisType() + ", ";
	if (getOrganization() != null)
	    resume = resume + getOrganization() + ", ";
	if ((getYear() != null) && (getYear() > 0))
	    resume = resume + getYear() + ", ";

	resume = finishResume(resume);
	return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
	BibtexFile bibtexFile = new BibtexFile();

	BibtexEntry bibEntry;
	if (getThesisType().equals(ThesisType.PhD_Thesis))
	    bibEntry = bibtexFile.makeEntry("phdthesis", generateBibtexKey());
	else
	    bibEntry = bibtexFile.makeEntry("masterthesis", generateBibtexKey());

	bibEntry.setField("title", bibtexFile.makeString(getTitle()));
	bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
	if (getOrganization() != null)// school
	    bibEntry.setField("organization", bibtexFile.makeString(getOrganization()));
	if ((getAddress() != null) && (getAddress().length() > 0))
	    bibEntry.setField("address", bibtexFile.makeString(getAddress()));
	if (getMonth() != null)
	    bibEntry.setField("month", bibtexFile.makeString(getMonth().toString().toLowerCase()));
	if ((getNote() != null) && (getNote().hasContent()))
	    bibEntry.setField("note", bibtexFile.makeString(getNote().getContent()));

	BibtexPersonList authorsList = getBibtexAuthorsList(bibtexFile, getAuthors());
	if (authorsList != null) {
	    BibtexString bplString = bibtexFile.makeString(bibtexPersonListToString(authorsList));
	    bibEntry.setField("author", bplString);
	}

	return bibEntry;

    }

    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.Thesis.call", "setTitle");
    }

    @Override
    public void setThesisType(ThesisType thesisType) {
	throw new DomainException("error.researcher.Thesis.call", "setThesisType");
    }

    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Thesis.call", "setYear");
    }

    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.Thesis.call", "setAddress");
    }

    @Override
    public void setNote(MultiLanguageString note) {
	throw new DomainException("error.researcher.Thesis.call", "setNote");
    }

    @Override
    public void setNumberPages(Integer numberPages) {
	throw new DomainException("error.researcher.Thesis.call", "setNumberPages");
    }

    @Override
    public void setLanguage(String language) {
	throw new DomainException("error.researcher.Thesis.call", "setLanguage");
    }

    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Thesis.call", "setMonth");
    }

    @Override
    public void setYearBegin(Integer yearBegin) {
	throw new DomainException("error.researcher.Thesis.call", "setYearBegin");
    }

    @Override
    public void setMonthBegin(Month monthBegin) {
	throw new DomainException("error.researcher.Thesis.call", "setMonthBegin");
    }

    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Thesis.call", "setUrl");
    }

    @Override
    public void setPublisher(String publisher) {
	throw new DomainException("error.researcher.Thesis.call", "setPublisher");
    }

    @Override
    public void setOrganization(String organization) {
	throw new DomainException("error.researcher.Thesis.call", "setOrganization");
    }

    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Thesis.call", "setCountry");
    }
    
    public String getSchema() {
    	return usedSchema;
    }
    
    @Override
    public boolean isDeletableByCurrentUser() {
    	return !hasThesis() && super.isDeletableByCurrentUser();
    }
    
    @Override
    public boolean isEditableByCurrentUser() {
    	return !hasThesis() && super.isEditableByCurrentUser();
    }
    
    @Override
    public String getScientificArea() {
    	String customArea = super.getScientificArea();
    	
    	if (customArea != null) {
    		return customArea;
    	}

    	CurricularCourse curricularCourse = getThesis().getEnrolment().getCurricularCourse();
		ScientificArea scientificArea = curricularCourse.getScientificArea();
    	if (scientificArea != null) {
    		return scientificArea.getName();
    	}
        	
    	return null;
    }

	public boolean isLibraryDetailsConfirmed() {
		Boolean confirmation = getLibraryConfirmation();
		return confirmation != null && confirmation;
	}

	public boolean isLibraryDetailsExported() {
		Boolean exported = getLibraryExported();
		return exported != null && exported;
	}

	/**
	 * Verifies if this publication was the result of an internal thesis evaluation process, that is,
	 * if this thesis result is connected to the theses from the evaluation process.
	 * 
	 * @return <code>true</code> if the result is connected to a process thesis
	 */
	public boolean isInternalThesis() {
		return hasThesis() && getThesis().isFinalAndApprovedThesis();
	}

	public String getSubtitle() {
		if (! hasThesis()) {
			return null;
		}
		else {
			return getThesis().getFinalSubtitle().getContent(getThesis().getLanguage());
		}
	}
	
	public String getAuthorsNames() {
		StringBuilder builder = new StringBuilder();
		
		for (Person person : getAuthors()) {
			if (builder.length() > 0) {
				builder.append(", ");
			}
			
			builder.append(person.getName());
		}
		
		return builder.toString();
	}
	
	public YearMonthDay getYearMonth() {
		return new YearMonthDay(getYear(), getMonth().getNumberOfMonth(), 1);
	}
}