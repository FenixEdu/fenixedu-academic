package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.util.Month;

public class MergeJournalIssuePageContainerBean extends MergeResearchActivityPageContainerBean {
    private String volume;
    private Integer year;
    private String number;
    private Month month;
    private String url;
    private Boolean specialIssue;
    private String specialIssueComment;
    
    private DomainReference<ScientificJournal> scientificJournal;
    
    public MergeJournalIssuePageContainerBean(ScientificJournal scientificJournal) {
	this.scientificJournal = new DomainReference<ScientificJournal>(scientificJournal);
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSpecialIssue() {
        return specialIssue;
    }

    public void setSpecialIssue(Boolean specialIssue) {
        this.specialIssue = specialIssue;
    }

    public String getSpecialIssueComment() {
        return specialIssueComment;
    }

    public void setSpecialIssueComment(String specialIssueComment) {
        this.specialIssueComment = specialIssueComment;
    }

    public ScientificJournal getScientificJournal() {
	return (this.scientificJournal != null) ? this.scientificJournal.getObject() : null;
    }

    public void setScientificJournal(ScientificJournal scientificJournal) {
	this.scientificJournal = (scientificJournal != null) ? new DomainReference<ScientificJournal>(scientificJournal) : null;
    }



}
