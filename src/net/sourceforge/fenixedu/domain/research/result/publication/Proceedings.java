package net.sourceforge.fenixedu.domain.research.result.publication;

import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * (collection: A collection of works. Same as Proceedings.) The proceedings of
 * a conference. Required fields: title, year. Optional fields: editor,
 * publisher, organization, address, month, note.
 * 
 * Extra from previous publications: conference
 */
public class Proceedings extends Proceedings_Base {

	private static final String usedSchema = "result.publication.presentation.Proceedings";
	
    public Proceedings() {
	super();
    }

    public Proceedings(Person participator, String title, MultiLanguageString keywords, Integer year, String conference, ScopeType scope,
	    String publisher, String organization, String address, MultiLanguageString note, Month month, String url) {
	this();
	super.checkRequiredParameters(keywords, note);
	checkRequiredParameters(title, year, conference);
	super.setCreatorParticipation(participator, ResultParticipationRole.Editor);
	fillAllAttributes(title, keywords, year, conference, scope, publisher, organization, address, note, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, MultiLanguageString keywords, Integer year, String conference, ScopeType scope, String publisher,
	    String organization, String address, MultiLanguageString note, Month month, String url) {
    super.checkRequiredParameters(keywords, note);
    checkRequiredParameters(title, year, conference);
	fillAllAttributes(title, keywords, year, conference, scope, publisher, organization, address, note, month, url);
	super.setModifiedByAndDate();
    }

    private void fillAllAttributes(String title, MultiLanguageString keywords, Integer year, String conference, ScopeType scope,
	    String publisher, String organization, String address, MultiLanguageString note, Month month, String url) {
	super.setTitle(title);
	super.setYear(year);
	super.setConference(conference);
	super.setScope(scope);
	super.setPublisher(publisher);
	super.setOrganization(organization);
	super.setAddress(address);
	super.setNote(note);
	super.setMonth(month);
	super.setUrl(url);
	super.setKeywords(keywords);
    }

    private void checkRequiredParameters(String title, Integer year, String conference) {
	if ((title == null) || (title.length() == 0))
	    throw new DomainException("error.researcher.Proceedings.title.null");
	if (year == null)
	    throw new DomainException("error.researcher.Proceedings.year.null");
	if (conference == null)
	    throw new DomainException("error.researcher.Proceedings.event.null");
    }

    @Override
    public String getResume() {
	String resume = getParticipationsAndTitleString();
	if (getPublisher() != null)
	    resume = resume + getPublisher() + ", ";
	if ((getYear() != null) && (getYear() > 0))
	    resume = resume + getYear() + ", ";
	if (getConference() != null)
	    resume = resume + getConference() + ", ";

	resume = finishResume(resume);
	return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
	BibtexFile bibtexFile = new BibtexFile();

	BibtexEntry bibEntry = bibtexFile.makeEntry("proceedings", generateBibtexKey());
	bibEntry.setField("title", bibtexFile.makeString(getTitle()));
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

	BibtexPersonList editorsList = getBibtexEditorsList(bibtexFile, getEditors());
	if (editorsList != null) {
	    BibtexString bplString = bibtexFile.makeString(bibtexPersonListToString(editorsList));
	    bibEntry.setField("editor", bplString);
	}

	return bibEntry;
    }

    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.Proceedings.call", "setTitle");
    }

    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Proceedings.call", "setYear");
    }

    @Override
    public void setConference(String conference) {
	throw new DomainException("error.researcher.Proceedings.call", "setConference");
    }

    @Override
    public void setPublisher(String publisher) {
	throw new DomainException("error.researcher.Proceedings.call", "setPublisher");
    }

    @Override
    public void setOrganization(String organization) {
	throw new DomainException("error.researcher.Proceedings.call", "setOrganization");
    }

    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.Proceedings.call", "setAddress");
    }

    @Override
    public void setNote(MultiLanguageString note) {
	throw new DomainException("error.researcher.Proceedings.call", "setNote");
    }

    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Proceedings.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Proceedings.call", "setUrl");
    }

    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Proceedings.call", "setCountry");
    }

    @Override
    public void setScope(ScopeType scope) {
	throw new DomainException("error.researcher.Inproceedings.call", "setScope");
    }

	@Override
	public String getSchema() {
		return usedSchema;
}
}
