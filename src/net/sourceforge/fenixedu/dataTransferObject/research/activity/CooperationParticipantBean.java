package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class CooperationParticipantBean extends ParticipantBean implements Serializable {

	Cooperation cooperation;

	public CooperationParticipantBean() {
		super();
		this.setCooperation(null);
	}

	public Cooperation getCooperation() {
		return this.cooperation;
	}

	public void setCooperation(Cooperation cooperation) {
		this.cooperation = cooperation;
	}

	@Override
	public List<ResearchActivityParticipationRole> getAllowedRoles() {
		return ResearchActivityParticipationRole.getAllBilateralCooperationRoles();
	}

	@Override
	public DomainObject getActivity() {
		return getCooperation();
	}
}
