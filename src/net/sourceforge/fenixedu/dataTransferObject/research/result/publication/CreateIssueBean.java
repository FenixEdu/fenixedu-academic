package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreateIssueBean implements Serializable{

    private DomainReference<ScientificJournal> journal;
    private String journalName = null;
    private MultiLanguageString scientificJournalName;
    private ResearchActivityLocationType location;
    private Integer year;
    private String issn;
    private String magazineUrl;
    
    private Month month;
    private String volume;
    private String number;
    private String publisher;
    private String url;

    private Boolean createNewJournal;
    
    public Boolean getCreateNewJournal() {
        return createNewJournal;
    }

    public void setCreateNewJournal(Boolean createNewIssue) {
        this.createNewJournal = createNewIssue;
    }

    public CreateIssueBean() {
	journal = new DomainReference<ScientificJournal> (null);
    }

    public ScientificJournal getJournal() {
        return journal.getObject();
    }

    public void setJournal(ScientificJournal journal) {
        this.journal = new DomainReference<ScientificJournal>(journal);
    }

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }
    
    public Boolean getJournalAlreadyChosen() {
	return journal.getObject() != null || scientificJournalName != null;
    }
    
    public Boolean getIssueAlreadyChosen() {
	return getVolume() != null;
    }
    
    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public ResearchActivityLocationType getLocation() {
        return location;
    }

    public void setLocation(ResearchActivityLocationType location) {
        this.location = location;
    }

    public MultiLanguageString getScientificJournalName() {
        return scientificJournalName;
    }

    public void setScientificJournalName(MultiLanguageString scientificJournalName) {
        this.scientificJournalName = scientificJournalName;
    }
     
    public String getJournalAsString() {
	return (getJournal()!=null) ? getJournal().getName().getContent() : getJournalName();
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getMagazineUrl() {
        return magazineUrl;
    }

    public void setMagazineUrl(String magazineUrl) {
        this.magazineUrl = magazineUrl;
    }
}
