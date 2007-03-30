package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.Participation;

public class ResearchActivityEditionBean implements Serializable {
    private List<Participation> participations;

    private List<Participation> otherParticipations;

    public List<Participation> getParticipations() {
	return participations;
    }

    public void setParticipations(List<Participation> participations) {
	this.participations = participations;
    }

    public List<Participation> getOtherParticipations() {
	return otherParticipations;
    }

    public void setOtherParticipations(List<Participation> otherParticipations) {
	this.otherParticipations = otherParticipations;
    }

}
