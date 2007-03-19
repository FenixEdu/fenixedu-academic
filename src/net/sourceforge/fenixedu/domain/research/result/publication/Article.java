package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.ArticleAssociation;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
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
 * An article from a journal or magazine. Required fields: author, title,
 * journal, year. Optional fields: volume, number, pages, month, note.
 * 
 * Extra from previous publications: issn, language, publisher, country, scope
 */
public class Article extends Article_Base {

    private static final String usedSchema = "result.publication.presentation.Article";

    public Article() {
	super();
    }

    public Article(Person participator, String title, MultiLanguageString keywords, JournalIssue issue,
	    Integer firstPage, Integer lastPage, MultiLanguageString note, String language, String url) {
	this();
	super.checkRequiredParameters(keywords, note);
	checkRequiredParameters(title, issue);
	super.setCreatorParticipation(participator, ResultParticipationRole.Author);
	fillAllAttributes(title, keywords, issue, firstPage, lastPage, note, language, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, MultiLanguageString keywords, JournalIssue issue, Integer firstPage,
	    Integer lastPage, MultiLanguageString note, String language, String url) {
	checkRequiredParameters(title, issue);
	fillAllAttributes(title, keywords, issue, firstPage, lastPage, note, language, url);
	super.setModifiedByAndDate();
    }

    private void fillAllAttributes(String title, MultiLanguageString keywords, JournalIssue issue,
	    Integer firstPage, Integer lastPage, MultiLanguageString note, String language, String url) {
	super.setTitle(title);
	setJournalIssue(issue);
	super.setFirstPage(firstPage);
	super.setLastPage(lastPage);
	super.setNote(note);
	super.setUrl(url);
	super.setLanguage(language);
	super.setKeywords(keywords);
    }

    private void checkRequiredParameters(String title, JournalIssue issue) {
	if (title == null || title.length() == 0)
	    throw new DomainException("error.researcher.Article.title.null");
	if (issue == null)
	    throw new DomainException("error.researcher.Article.journal.null");
    }

    @Override
    public String getResume() {
	String resume = getParticipationsAndTitleString();
	if ((getJournal() != null) && (getJournal().length() > 0))
	    resume = resume + getJournal() + ", ";
	if ((getNumber() != null) && (getNumber().length() > 0))
	    resume = resume + "No. " + getNumber() + ", ";
	if ((getVolume() != null) && (getVolume().length() > 0))
	    resume = resume + "Vol. " + getVolume() + ", ";
	if ((getFirstPage() != null) && (getFirstPage() > 0) && (getLastPage() != null)
		&& (getLastPage() > 0))
	    resume = resume + "Pag. " + getFirstPage() + " - " + getLastPage() + ", ";
	if ((getYear() != null) && (getYear() > 0))
	    resume = resume + getYear() + ", ";

	resume = finishResume(resume);
	return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
	BibtexFile bibtexFile = new BibtexFile();

	BibtexEntry bibEntry = bibtexFile.makeEntry("article", generateBibtexKey());
	bibEntry.setField("title", bibtexFile.makeString(getTitle()));
	bibEntry.setField("journal", bibtexFile.makeString(getScientificJournal().getName() + ":" + getJournalIssue().getVolume() + "(" + getJournalIssue().getNumber() + ")"));
	bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
	if ((getVolume() != null) && (getVolume().length() > 0))
	    bibEntry.setField("volume", bibtexFile.makeString(getVolume()));
	if ((getNumber() != null) && (getNumber().length() > 0))
	    bibEntry.setField("series", bibtexFile.makeString(getNumber()));
	if ((getFirstPage() != null) && (getLastPage() != null) && (getFirstPage() < getLastPage()))
	    bibEntry.setField("pages", bibtexFile.makeString(getFirstPage() + "-" + getLastPage()));
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
	throw new DomainException("error.researcher.Article.call", "setTitle");
    }

    @Override
    public void setJournal(String journal) {
	throw new DomainException("error.researcher.Article.call", "setJournal");
    }

    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.Article.call", "setYear");
    }

    @Override
    public void setVolume(String volume) {
	throw new DomainException("error.researcher.Article.call", "setVolume");
    }

    @Override
    public void setNumber(String number) {
	throw new DomainException("error.researcher.Article.call", "setNumber");
    }

    @Override
    public void setFirstPage(Integer firstPage) {
	throw new DomainException("error.researcher.Article.call", "setFirstPage");
    }

    @Override
    public void setLastPage(Integer lastPage) {
	throw new DomainException("error.researcher.Article.call", "setLastPage");
    }

    @Override
    public void setNote(MultiLanguageString note) {
	throw new DomainException("error.researcher.Article.call", "setNote");
    }

    @Override
    public void setIssn(Integer issn) {
	throw new DomainException("error.researcher.Article.call", "setIssn");
    }

    @Override
    public void setLanguage(String language) {
	throw new DomainException("error.researcher.Article.call", "setLanguage");
    }

    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.Article.call", "setCountry");
    }

    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.Article.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.Article.call", "setUrl");
    }

    @Override
    public void setPublisher(String publisher) {
	throw new DomainException("error.researcher.Article.call", "setPublisher");
    }
    
    @Override
    public void setOrganization(String organization) {
	throw new DomainException("error.researcher.Article.call", "setOrganization");
    }

    public String getSchema() {
	return usedSchema;
    }

    public JournalIssue getJournalIssue() {
	return getArticleAssociation().getJournalIssue();
    }

    @Checked("ResultPredicates.writePredicate")
    public void setJournalIssue(JournalIssue journalIssue) {
	ArticleAssociation association = getArticleAssociation();
	if (association == null) {
	    Person creator = AccessControl.getPerson();
	    if (creator == null) {
		creator = getCreator();
	    }
	    association = new ArticleAssociation(journalIssue, this, creator);
	} else {
	    association.setJournalIssue(journalIssue);
	}
    }

    public ScientificJournal getScientificJournal() {
	return getJournalIssue().getScientificJournal();
    }

    @Override
    public Month getMonth() {
	return getJournalIssue().getMonth();
    }

    @Override
    public Integer getYear() {
	return getJournalIssue().getYear();
    }

    @Override
    public void delete() {
	if(hasArticleAssociation()) {
	    getArticleAssociation().delete();
	}
	super.delete();
    }
    
    public ScopeType getScope() {
	return ScopeType.valueOf(getJournalIssue().getScientificJournal().getLocationType().toString());
    }
}
 