package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

public abstract class Participation extends Participation_Base {

    public Participation() {
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }
    
    public Participation(Party party, ResearchActivityParticipationRole role) {
	this();
	super.setParty(party);
	super.setRole(role);
    }

    public void delete() {
	super.setParty(null);
	super.setRole(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean isEventParticipation() {
	return (this instanceof EventParticipation);
    }

    public boolean isScientificJournaltParticipation() {
	return (this instanceof ScientificJournalParticipation);
    }

    public boolean isJournalIssueParticipation() {
	return (this instanceof JournalIssueParticipation);
    }
    public boolean isCooperationParticipation() {
	return (this instanceof CooperationParticipation);
    }

    public boolean isEventEditionParticipation() {
	return (this instanceof EventEditionParticipation);
    }

    public boolean isPersonParticipation() {
	return getParty().isPerson();
    }
    
    public boolean isPartyParticipation() {
	return getParty().isUnit();
    }
        
    public abstract List<ResearchActivityParticipationRole> getAllowedRoles();
    public abstract String getParticipationName();
    public abstract boolean isLastParticipation();
    public abstract Integer getCivilYear();
    public abstract boolean scopeMatches(ScopeType type);
    
    public enum ResearchActivityParticipationRole {
	Editor_in_Chief, Associate_Editor, Committee_Chair, General_Chair, Committee_Member, Invited_Speaker, Reviewer,

	// ScientificOrganizationsAndNetworks
	President, Vice_President, Secretary,

	Advisory_Board, Local_Chair, National_Representative,

	// BilateralCooperation
	Visitor, Research_Advisorship, Joint_Research_Work, Invited_Expert;

	public static ResearchActivityParticipationRole getEventDefaultRole() {
	    return Invited_Speaker;
	}

	public static ResearchActivityParticipationRole getScientificJournalDefaultRole() {
	    return Reviewer;
	}

	public static ResearchActivityParticipationRole getDefaultScientificOrganizationsAndNetworksCooperationRole() {

	    return National_Representative;
	}

	public static ResearchActivityParticipationRole getDefaultBilateralCooperationRole() {
	    return Visitor;
	}

	public static List<ResearchActivityParticipationRole> getAllEventParticipationRoles() {
	    List<ResearchActivityParticipationRole> eventRoles = new ArrayList<ResearchActivityParticipationRole>();

	    eventRoles.add(General_Chair);
	    eventRoles.add(Committee_Member);
	    eventRoles.add(Committee_Chair);
	    eventRoles.add(Invited_Speaker);
	    eventRoles.add(Reviewer);

	    return eventRoles;
	}

	public static List<ResearchActivityParticipationRole> getAllScientificJournalParticipationRoles() {
	    List<ResearchActivityParticipationRole> journalRoles = new ArrayList<ResearchActivityParticipationRole>();

	    journalRoles.add(Editor_in_Chief);
	    journalRoles.add(Committee_Member);
	    journalRoles.add(Associate_Editor);
	    journalRoles.add(Reviewer);

	    return journalRoles;
	}

	public static List<ResearchActivityParticipationRole> getAllScientificOrganizationsAndNetworksRoles() {
	    List<ResearchActivityParticipationRole> cooperationRoles = new ArrayList<ResearchActivityParticipationRole>();

	    cooperationRoles.add(President);
	    cooperationRoles.add(Vice_President);
	    cooperationRoles.add(Secretary);
	    cooperationRoles.add(Committee_Chair);
	    cooperationRoles.add(Committee_Member);
	    cooperationRoles.add(Advisory_Board);
	    cooperationRoles.add(Local_Chair);
	    cooperationRoles.add(National_Representative);

	    return cooperationRoles;
	}

	public static List<ResearchActivityParticipationRole> getAllBilateralCooperationRoles() {
	    List<ResearchActivityParticipationRole> cooperationRoles = new ArrayList<ResearchActivityParticipationRole>();

	    cooperationRoles.add(Visitor);
	    cooperationRoles.add(Research_Advisorship);
	    cooperationRoles.add(Joint_Research_Work);
	    cooperationRoles.add(Invited_Expert);

	    return cooperationRoles;
	}

	public static List<ResearchActivityParticipationRole> getAllCommissionRoles() {
	    List<ResearchActivityParticipationRole> cooperationRoles = new ArrayList<ResearchActivityParticipationRole>();

	    cooperationRoles.add(Committee_Member);

	    return cooperationRoles;
	}
	
	public static List<ResearchActivityParticipationRole> getAllJournalIssueRoles() {
	    return getAllScientificJournalParticipationRoles();
	}

	public static List<ResearchActivityParticipationRole> getAllEventEditionRoles() {
	    return getAllEventParticipationRoles();
	}
    }

}
