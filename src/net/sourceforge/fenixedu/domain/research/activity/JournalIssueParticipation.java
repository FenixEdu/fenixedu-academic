package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class JournalIssueParticipation extends JournalIssueParticipation_Base {
    
    public  JournalIssueParticipation(Party party, ResearchActivityParticipationRole role, JournalIssue issue) {
	super();
	if(alreadyHasParticipation(party, role, issue)) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setParty(party);
	super.setRole(role);
	super.setJournalIssue(issue);
    }
    
    private boolean alreadyHasParticipation(Party party, ResearchActivityParticipationRole role, JournalIssue issue) {
	for(JournalIssueParticipation participation : party.getAllJournalIssueParticipations()) {
	    if(participation.match(party,role,issue)) {
		return true;
	    }
	}
	return false;	
    }
    
    public boolean match(Party party, ResearchActivityParticipationRole role, JournalIssue issue) {
	return this.getParty().equals(party) && this.getRole().equals(role) && this.getJournalIssue() .equals(issue);
    }

    @Override
    public void setJournalIssue(JournalIssue journalIssue) {
	if(alreadyHasParticipation(getParty(), getRole(), journalIssue)) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setJournalIssue(journalIssue);
    }

    @Override
    public void setParty(Party party) {
	if(alreadyHasParticipation(party, getRole(), getJournalIssue())) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setParty(party);
    }

    @Override
    public void setRole(ResearchActivityParticipationRole role) {
	if(alreadyHasParticipation(getParty(), role, getJournalIssue())) {
	    throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
	}
	super.setRole(role);
    }

    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllJournalIssueRoles();
    }

    @Override
    public String getParticipationName() {
	return this.getJournalIssue().getScientificJournal().getName();
    }

    @Override
    public boolean isLastParticipation() {
	return this.getJournalIssue().getParticipationsFor(this.getParty()).size() == 1;
    }

    @Override
    public void delete() {
	removeJournalIssue();
	super.delete();
    }
}
