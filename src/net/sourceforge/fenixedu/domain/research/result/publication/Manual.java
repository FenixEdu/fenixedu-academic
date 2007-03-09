package net.sourceforge.fenixedu.domain.research.result.publication;

import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * Technical documentation. Required fields: title. Optional fields: author,
 * organization, address, edition, month, year, note.
 */
public class Manual extends Manual_Base {

	private static final String usedSchema = "result.publication.presentation.Manual";
	
    public Manual() {
	super();
    }

    public Manual(Person participator, String title, MultiLanguageString keywords, String organization, Integer year, String address,
    		MultiLanguageString note, String edition, Month month, String url) {
	this();
	super.checkRequiredParameters(keywords, note);
	checkRequiredParameters(title);
	super.setCreatorParticipation(participator, ResultParticipationRole.Author);
	fillAllAttributes(title, keywords, organization, year, address, note, edition, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, MultiLanguageString keywords, String organization, Integer year, String address, MultiLanguageString note,
	    String edition, Month month, String url) {
	super.checkRequiredParameters(keywords, note);
    checkRequiredParameters(title);
	fillAllAttributes(title, keywords, organization, year, address, note, edition, month, url);
	super.setModifiedByAndDate();
    }

    private void fillAllAttributes(String title, MultiLanguageString keywords, String organization, Integer year, String address,
    		MultiLanguageString note, String edition, Month month, String url) {
	super.setTitle(title);
	super.setOrganization(organization);
	super.setYear(year);
	super.setAddress(address);
	super.setNote(note);
	super.setEdition(edition);
	super.setMonth(month);
	super.setUrl(url);
	super.setKeywords(keywords);
    }

    private void checkRequiredParameters(String title) {
	if ((title == null) || (title.length() == 0))
	    throw new DomainException("error.researcher.Manual.title.null");
    }

    @Override
    public String getResume() {
	String resume = getParticipationsAndTitleString();
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

	BibtexEntry bibEntry = bibtexFile.makeEntry("manual", generateBibtexKey());
	bibEntry.setField("title", bibtexFile.makeString(getTitle()));
	if ((getYear() != null) && (getYear() > 0))
	    bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
	if (getPublisher() != null)
	    bibEntry.setField("publisher", bibtexFile.makeString(getPublisher()));
	if (getOrganization() != null)
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
	throw new DomainException("error.researcher.Manual.call", "setTitle");
    }

    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Manual.call", "setYear");
    }

    @Override
    public void setPublisher(String publisher) {
	throw new DomainException("error.researcher.Manual.call", "setPublisher");
    }

    @Override
    public void setOrganization(String organization) {
	throw new DomainException("error.researcher.Manual.call", "setOrganization");
    }

    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.Manual.call", "setAddress");
    }

    @Override
    public void setNote(MultiLanguageString note) {
	throw new DomainException("error.researcher.Manual.call", "setNote");
    }

    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Manual.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Manual.call", "setUrl");
    }

    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Manual.call", "setCountry");
    }

    @Override
    public void setEdition(String edition) {
	throw new DomainException("error.researcher.Manual.call", "setEdition");
    }

	@Override
	public String getSchema() {
		return usedSchema;
	}
}
