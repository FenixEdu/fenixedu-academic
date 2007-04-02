package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ParticipationsInterface;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

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

    public static ResearchActivityEditionBean getResearchActivityEditionBean(
	    ParticipationsInterface objectWithParticipations) {
	ResearchActivityEditionBean bean = null;

	if (objectWithParticipations instanceof Event) {
	    bean = new ResearchEventEditionBean();
	    ((ResearchEventEditionBean) bean).setEvent((Event) objectWithParticipations);
	}
	if (objectWithParticipations instanceof EventEdition) {
	    bean = new ResearchEventEditionEditionBean();
	    ((ResearchEventEditionEditionBean) bean).setEventEdition((EventEdition) objectWithParticipations);
	}
	if (objectWithParticipations instanceof ScientificJournal) {
	    bean = new ResearchScientificJournalEditionBean();
	    ((ResearchScientificJournalEditionBean) bean)
		    .setScientificJournal((ScientificJournal) objectWithParticipations);
	}
	if (objectWithParticipations instanceof JournalIssue) {
	    bean = new ResearchJournalIssueEditonBean();
	    ((ResearchJournalIssueEditonBean) bean).setJournalIssue((JournalIssue)objectWithParticipations);
	}
	if (objectWithParticipations instanceof Cooperation) {
	    bean = new ResearchCooperationEditionBean();
	    ((ResearchCooperationEditionBean) bean).setCooperation((Cooperation) objectWithParticipations);
	}
	return bean;
    }

}
