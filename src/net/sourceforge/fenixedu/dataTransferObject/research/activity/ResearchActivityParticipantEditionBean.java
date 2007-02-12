package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivity;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class ResearchActivityParticipantEditionBean implements Serializable {

	 DomainReference<Participation> participation;
	 ResearchActivityParticipationRole role;
	 
	 public ResearchActivityParticipantEditionBean() {
		setParticipation(null);
		//setRole(ResearchActivityParticipationRole.getDefaultEventPersonRoleType());
	}
	 
	public ResearchActivity getResearchActivity() {
		return participation.getObject().getResearchActivity();
	}
	
	public void setResearchActivity(ResearchActivity activity) {
		this.participation.getObject().setResearchActivity(activity);
	}

	public Participation getParticipation() {
		return participation.getObject();
	}

	public void setParticipation(Participation participation) {
		this.participation = new DomainReference<Participation>(participation);
	}

	public ResearchActivityParticipationRole getRole() {
		return role;
	}

	public void setRole(ResearchActivityParticipationRole role) {
		this.role = role;
	} 
	
}
