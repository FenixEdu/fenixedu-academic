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

    public Proceedings(Person participator, String title, MultiLanguageString keywords, EventEdition eventEdition, 
    		String publisher, String address, MultiLanguageString note, String url) {
	this();
	super.checkRequiredParameters(keywords, note);
	checkRequiredParameters(title, eventEdition);
	super.setCreatorParticipation(participator, ResultParticipationRole.Editor);
	fillAllAttributes(title, keywords, eventEdition, publisher, address, note, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, MultiLanguageString keywords, EventEdition eventEdition, String publisher, String address, 
    		MultiLanguageString note, String url) {
    super.checkRequiredParameters(keywords, note);
    checkRequiredParameters(title, eventEdition);
	fillAllAttributes(title, keywords, eventEdition, publisher, address, note, url);
	super.setModifiedByAndDate();
    }

    private void fillAllAttributes(String title, MultiLanguageString keywords, EventEdition eventEdition, String publisher, String address,
    		MultiLanguageString note, String url) {
	super.setTitle(title);
	super.setPublisher(publisher);
	super.setAddress(address);
	super.setNote(note);
	super.setUrl(url);
	super.setKeywords(keywords);
	setEventEdition(eventEdition);
    }

    private void checkRequiredParameters(String title, EventEdition eventEdition) {
	if ((title == null) || (title.length() == 0))
	    throw new DomainException("error.researcher.Proceedings.title.null");
	if (eventEdition == null)
		throw new DomainException("error.researcher.Proceedings.eventEdition.null");
    }

    @Override
    public String getResume() {
	String resume = getParticipationsAndTitleString();
	if (getPublisher() != null)
	    resume = resume + getPublisher() + ", ";
	if ((getYear() != null) && (getYear() > 0))
	    resume = resume + getYear() + ", ";
	if (getConferenceName() != null)
	    resume = resume + getConferenceName() + ", ";

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
}
