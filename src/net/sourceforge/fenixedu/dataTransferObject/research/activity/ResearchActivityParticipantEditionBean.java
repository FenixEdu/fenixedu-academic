package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class ResearchActivityParticipantEditionBean implements Serializable {

	 DomainReference<Participation> participation;
	 ResearchActivityParticipationRole role;
	 
	 public ResearchActivityParticipantEditionBean(Participation participation, ResearchActivityParticipationRole role) {
		setParticipation(participation);
		setRole(role);
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
	
	public List<ResearchActivityParticipationRole> getAllowedRoles() {
	    Participation participation = this.getParticipation();
	    return (participation==null) ? null : participation.getAllowedRoles();
	}
	
	
}
