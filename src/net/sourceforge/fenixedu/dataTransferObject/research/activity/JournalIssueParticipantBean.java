package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class JournalIssueParticipantBean extends ParticipantBean implements Serializable {

    DomainReference<JournalIssue> issue;
    
    public JournalIssueParticipantBean() {
	super();
	setJournalIssue(null);
    }
    
    public void setJournalIssue(JournalIssue issue) {
	this.issue = new DomainReference<JournalIssue>(issue);
    }
    
    public JournalIssue getJournalIssue() {
	return this.issue.getObject();
    }
    
    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllJournalIssueRoles();
    }

    @Override
    public DomainObject getActivity() {
	return getJournalIssue();
    }
}
