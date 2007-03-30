package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;

public class ResearchJournalIssueEditonBean extends ResearchActivityEditionBean implements Serializable {

    DomainReference<JournalIssue> issue;

    public ResearchJournalIssueEditonBean() {
	super();
	issue = new DomainReference<JournalIssue>(null);
    }

    public JournalIssue getJournalIssue() {
	return this.issue.getObject();
    }

    public void setJournalIssue(JournalIssue issue) {
	this.issue = new DomainReference<JournalIssue>(issue);
    }
}
