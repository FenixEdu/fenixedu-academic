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

		Organizing_Chair, Organizing_Member, Program_Chair, Program_Member, Invited_Speaker, Reviewer, Editor_in_Chief, Associate_Editor, Editorial_Board, Guest_Editor, President, Vice_President, Secretary, Local_Chair, National_Representative, Visitor, Expert, Member;

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

			return eventRoles;
		}

		public static List<ResearchActivityParticipationRole> getAllScientificJournalParticipationRoles() {
			List<ResearchActivityParticipationRole> journalRoles = new ArrayList<ResearchActivityParticipationRole>();

			journalRoles.add(Editor_in_Chief);
			journalRoles.add(Associate_Editor);
			journalRoles.add(Editorial_Board);

			return journalRoles;
		}

		public static List<ResearchActivityParticipationRole> getAllScientificOrganizationsAndNetworksRoles() {
			List<ResearchActivityParticipationRole> cooperationRoles = new ArrayList<ResearchActivityParticipationRole>();

			cooperationRoles.add(President);
			cooperationRoles.add(Vice_President);
			cooperationRoles.add(Secretary);
			cooperationRoles.add(Member);
			cooperationRoles.add(Local_Chair);
			cooperationRoles.add(National_Representative);

			return cooperationRoles;
		}

		public static List<ResearchActivityParticipationRole> getAllBilateralCooperationRoles() {
			List<ResearchActivityParticipationRole> cooperationRoles = new ArrayList<ResearchActivityParticipationRole>();

			cooperationRoles.add(Visitor);
			cooperationRoles.add(Expert);

			return cooperationRoles;
		}

		public static List<ResearchActivityParticipationRole> getAllCommissionRoles() {
			List<ResearchActivityParticipationRole> cooperationRoles = new ArrayList<ResearchActivityParticipationRole>();

			cooperationRoles.add(President);
			cooperationRoles.add(Member);

			return cooperationRoles;
		}

		public static List<ResearchActivityParticipationRole> getAllJournalIssueRoles() {
			List<ResearchActivityParticipationRole> issueRoles = new ArrayList<ResearchActivityParticipationRole>();

			issueRoles.add(Guest_Editor);
			issueRoles.add(Reviewer);

			return issueRoles;
		}

		public static List<ResearchActivityParticipationRole> getAllEventEditionRoles() {
			List<ResearchActivityParticipationRole> eventEditionRoles = new ArrayList<ResearchActivityParticipationRole>();

			eventEditionRoles.add(Organizing_Chair);
			eventEditionRoles.add(Organizing_Member);
			eventEditionRoles.add(Program_Chair);
			eventEditionRoles.add(Program_Member);
			eventEditionRoles.add(Invited_Speaker);
			eventEditionRoles.add(Reviewer);

			return eventEditionRoles;
		}
	}

}
