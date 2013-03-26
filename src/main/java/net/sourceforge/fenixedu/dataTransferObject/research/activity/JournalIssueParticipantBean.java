package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import pt.ist.fenixframework.DomainObject;

public class JournalIssueParticipantBean extends ParticipantBean implements Serializable {

    JournalIssue issue;

    public JournalIssueParticipantBean() {
        super();
        setJournalIssue(null);
    }

    public void setJournalIssue(JournalIssue issue) {
        this.issue = issue;
    }

    public JournalIssue getJournalIssue() {
        return this.issue;
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
