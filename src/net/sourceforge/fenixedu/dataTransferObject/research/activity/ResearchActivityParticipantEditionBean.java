package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ResearchActivityParticipantEditionBean implements Serializable {

	 private DomainReference<Participation> participation;
	 private ResearchActivityParticipationRole role;
	 private MultiLanguageString roleMessage;
	 
	 public MultiLanguageString getRoleMessage() {
		return roleMessage;
	}

	public void setRoleMessage(MultiLanguageString roleMessage) {
		this.roleMessage = roleMessage;
	}

	public ResearchActivityParticipantEditionBean(Participation participation, ResearchActivityParticipationRole role, MultiLanguageString roleMessage) {
		setParticipation(participation);
		setRole(role);
		setRoleMessage(roleMessage);
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
