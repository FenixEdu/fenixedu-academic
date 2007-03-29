package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventConferenceArticlesAssociation;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;

/**
 * (conference: The same as Inproceedings.) An article in a conference
 * proceedings. Required fields: author, title, booktitle, year. Optional
 * fields: editor, pages, organization, publisher, address, month, note.
 * 
 * Extra from previous publications: event(booktitle), language
 */
public class Inproceedings extends Inproceedings_Base {

	private static final String usedSchema = "result.publication.presentation.Inproceedings";
    public Inproceedings() {
	super();
    }

    public Inproceedings(Person participator, ResultParticipationRole participatorRole, String title, MultiLanguageString keywords,
    		EventEdition eventEdition, String publisher, String address, Integer firstPage, Integer lastPage, MultiLanguageString note,
    		String language, String url) {
	this();
	super.checkRequiredParameters(keywords, note);
	checkRequiredParameters(title, eventEdition);
	super.setCreatorParticipation(participator, participatorRole);
	fillAllAttributes(title, keywords, eventEdition, publisher, address, firstPage, lastPage, note, language, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, MultiLanguageString keywords, EventEdition eventEdition, String publisher, String address,
    		Integer firstPage, Integer lastPage, MultiLanguageString note, String language, String url) {
	super.checkRequiredParameters(keywords, note);
    checkRequiredParameters(title, eventEdition);
	fillAllAttributes(title, keywords, eventEdition, publisher, address, firstPage, lastPage, note, language, url);
	super.setModifiedByAndDate();
    }

    private void fillAllAttributes(String title,MultiLanguageString keywords, EventEdition eventEdition, String publisher, String address,
    		Integer firstPage, Integer lastPage, MultiLanguageString note, String language, String url) {
	super.setTitle(title);
	super.setPublisher(publisher);
	super.setAddress(address);
	super.setFirstPage(firstPage);
	super.setLastPage(lastPage);
	super.setNote(note);
	super.setLanguage(language);
	super.setUrl(url);
	super.setKeywords(keywords);
	setEventEdition(eventEdition);
    }

    private void checkRequiredParameters(String title, EventEdition eventEdition) {
	if ((title == null) || (title.length() == 0))
	    throw new DomainException("error.researcher.Inproceedings.title.null");
	if (eventEdition == null)
	    throw new DomainException("error.researcher.Inproceedings.eventEdition.null");
    }

    @Override
    public String getResume() {
	String resume = getParticipationsAndTitleString();
	if (getPublisher() != null)
	    resume = resume + getPublisher() + ", ";
	if ((getFirstPage() != null) && (getFirstPage() > 0) && (getLastPage() != null)
		&& (getLastPage() > 0))
	    resume = resume + "Pag. " + getFirstPage() + " - " + getLastPage() + ", ";
	resume = finishResume(resume);
	return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
	BibtexFile bibtexFile = new BibtexFile();

	BibtexEntry bibEntry = bibtexFile.makeEntry("inproceedings", generateBibtexKey());
	bibEntry.setField("title", bibtexFile.makeString(getTitle()));
	bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
	if (getConferenceName() != null && getConferenceName() != null)
	    bibEntry.setField("booktitle", bibtexFile.makeString(getConferenceName()));
	if (getPublisher() != null)
	    bibEntry.setField("publisher", bibtexFile.makeString(getPublisher()));
	if ((getFirstPage() != null) && (getLastPage() != null) && (getFirstPage() < getLastPage()))
	    bibEntry.setField("pages", bibtexFile.makeString(getFirstPage() + "-" + getLastPage()));
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

	BibtexPersonList editorsList = getBibtexEditorsList(bibtexFile, getEditors());
	if (editorsList != null) {
	    BibtexString bplString = bibtexFile.makeString(bibtexPersonListToString(editorsList));
	    bibEntry.setField("editor", bplString);
	}

	return bibEntry;
    }

    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.Inproceedings.call", "setTitle");
    }

    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Inproceedings.call", "setYear");
    }

    @Override
    public void setPublisher(String publisher) {
	throw new DomainException("error.researcher.Inproceedings.call", "setPublisher");
    }

    @Override
    public void setOrganization(String organization) {
	throw new DomainException("error.researcher.Inproceedings.call", "setOrganization");
    }

    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.Inproceedings.call", "setAddress");
    }

    @Override
    public void setFirstPage(Integer firstPage) {
	throw new DomainException("error.researcher.Inproceedings.call", "setFirstPage");
    }

    @Override
    public void setLastPage(Integer lastPage) {
	throw new DomainException("error.researcher.Inproceedings.call", "setLastPage");
    }

    @Override
    public void setNote(MultiLanguageString note) {
	throw new DomainException("error.researcher.Inproceedings.call", "setNote");
    }

    @Override
    public void setLanguage(String language) {
	throw new DomainException("error.researcher.Inproceedings.call", "setLanguage");
    }

    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Inproceedings.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Inproceedings.call", "setUrl");
    }

    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Inproceedings.call", "setCountry");
    }

    @Override
    public String getSchema() {
	return usedSchema;
    }
	
    public Event getEvent() {
	return this.getEventEdition().getEvent();
    }
	
    public EventEdition getEventEdition() {
	return this.getEventConferenceArticlesAssociation().getEventEdition();
    }
	
    @Checked("ResultPredicates.writePredicate")
    public void setEventEdition(EventEdition eventEdition) {
	EventConferenceArticlesAssociation association = this.getEventConferenceArticlesAssociation();
	
	if (association==null) {
	    Person creator = AccessControl.getPerson();
	    if(creator==null) {
	    	creator = getCreator();
	    }
	    association = new EventConferenceArticlesAssociation(eventEdition, this, creator);
	} else {
	    association.setEventEdition(eventEdition);
	}
	
    }
    
    @Override
    public Integer getYear() {
	return getEventEdition().getStartDate().getYear();
    }
		
    @Override
    public Month getMonth() {
	return Month.values()[getEventEdition().getStartDate().getMonthOfYear()-1];
    }
		
    @Override
    public String getOrganization() {
	return getEventEdition().getOrganization();
    }
	
    public String getConferenceName() {
	return getEventEdition().getFullName();
    }
    
    @Override
    public ScopeType getScope() {
	return this.getEvent().getLocationType();
    }
	
}
