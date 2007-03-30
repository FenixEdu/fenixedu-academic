package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class ResearchJournalIssueCreationBean implements Serializable {
    
    private DomainReference<ScientificJournal> journal;
    private DomainReference<JournalIssue> issue;
    private String scientificJournalName;
    private String journalIssueName;
    private ResearchActivityParticipationRole role;
    
    public ResearchJournalIssueCreationBean() {
	this.issue = new DomainReference<JournalIssue>(null);
	this.journal = new DomainReference<ScientificJournal>(null);
    }
    
    public JournalIssue getJournalIssue() {
	return issue.getObject();
    }
    
    public void setJournalIssue(JournalIssue issue) {
	this.issue = new DomainReference<JournalIssue>(issue);
    }

    public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
        this.role = role;
    }

    public String getJournalIssueName() {
        return journalIssueName;
    }

    public void setJournalIssueName(String journalIssueName) {
        this.journalIssueName = journalIssueName;
    }
    
    public ScientificJournal getScientificJournal() {
	return this.journal.getObject();
    }
    
    public void setScientificJournal(ScientificJournal journal) {
	this.journal = new DomainReference<ScientificJournal>(journal);
    }

    public String getScientificJournalName() {
        return scientificJournalName;
    }

    public void setScientificJournalName(String scientificJournalName) {
        this.scientificJournalName = scientificJournalName;
    }
}
