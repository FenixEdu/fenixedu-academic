package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication.ScopeType;
import bibtex.dom.BibtexEntry;

public class ArticleBean extends ResultPublicationBean implements Serializable {
    private String volume;

    private String language;

    private ScopeType scope;

    private String journal;

    private String number;

    private Integer firstPage;

    private Integer lastPage;

    private Integer issn;

    private DomainReference<JournalIssue> journalIssue;
    
    private DomainReference<ScientificJournal> scientificJournal;
    
    private String scientificJournalName;
    
    public ScientificJournal getScientificJournal() {
        return scientificJournal.getObject();
    }

    public void setScientificJournal(ScientificJournal scientificJournal) {
        this.scientificJournal = new DomainReference<ScientificJournal>(scientificJournal);
    }

    public String getScientificJournalName() {
        return scientificJournalName;
    }

    public void setScientificJournalName(String scientificJournalName) {
        this.scientificJournalName = scientificJournalName;
    }

    public ArticleBean() {
	this.setPublicationType(ResultPublicationType.Article);
	this.setActiveSchema("result.publication.create.Article");
	this.setParticipationSchema("resultParticipation.simple");
	journalIssue = new DomainReference<JournalIssue>(null);
	scientificJournal = new DomainReference<ScientificJournal>(null);
    }

    public ArticleBean(Article article) {
	this();
	fillCommonFields(article);
	fillSpecificFields(article);
    }

    public ArticleBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }

    public ArticleBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
	this.setActiveSchema("result.publication.import.Article");
    }

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
	return ResultPublicationBeanConversions.articleTo(this, type);
    }

    @Override
    protected void fillSpecificFields(ResearchResultPublication publication) {
	final Article article = (Article) publication;

	this.setScientificJournal(article.getScientificJournal());
	this.setScientificJournalName(article.getScientificJournal().getNameAsString());
	this.setJournal(article.getJournal());
	this.setVolume(article.getVolume());
	this.setNumber(article.getNumber());
	this.setFirstPage(article.getFirstPage());
	this.setLastPage(article.getLastPage());
	this.setIssn(article.getIssn());
	this.setLanguage(article.getLanguage());
	this.setScope(article.getScope());
	this.setScientificJournal(article.getScientificJournal());
	this.setJournalIssue(article.getJournalIssue());
    }

    protected void fillBibTeXFields(BibtexEntry entry) {
	setYearFromBibtexEntry(entry);
	setMonthFromBibtexEntry(entry);

	setTitle(getStringValueFromBibtexEntry("title", entry));
	setVolume(getStringValueFromBibtexEntry("volume", entry));
	setJournal(getStringValueFromBibtexEntry("journal", entry));
	setNumber(getStringValueFromBibtexEntry("number", entry));
	setNote(getStringValueFromBibtexEntry("note", entry));
	if (getFirstPageFromBibtexEntry(entry) != null) {
	    setFirstPage(getFirstPageFromBibtexEntry(entry));
	    setLastPage(getLastPageFromBibtexEntry(entry));
	}
    }

    public String getLanguage() {
	return language;
    }

    public void setLanguage(String language) {
	this.language = language;
    }

    public ScopeType getScope() {
	return scope;
    }

    public void setScope(ScopeType scope) {
	this.scope = scope;
    }

    public String getVolume() {
	return volume;
    }

    public void setVolume(String volume) {
	this.volume = volume;
    }

    public Integer getFirstPage() {
	return firstPage;
    }

    public void setFirstPage(Integer firstPage) {
	this.firstPage = firstPage;
    }

    public Integer getIssn() {
	return issn;
    }

    public void setIssn(Integer issn) {
	this.issn = issn;
    }

    public String getJournal() {
	return journal;
    }

    public void setJournal(String journal) {
	this.journal = journal;
    }

    public Integer getLastPage() {
	return lastPage;
    }

    public void setLastPage(Integer lastPage) {
	this.lastPage = lastPage;
    }

    public String getNumber() {
	return number;
    }

    public void setNumber(String number) {
	this.number = number;
    }

    public JournalIssue getJournalIssue() {
        return journalIssue.getObject();
    }

    public void setJournalIssue(JournalIssue journalIssue) {
        this.journalIssue = new DomainReference<JournalIssue>(journalIssue);
    }
}
