package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;

public class RemoveResearchActivityParticipation extends Service {

    public void run(EventParticipation participation) {
	Event event = participation.getEvent();
	participation.delete();
	event.sweep();
    }

    public void run(ScientificJournalParticipation participation) {
	ScientificJournal journal = participation.getScientificJournal();
	participation.delete();
	journal.sweep();
    }

    public void run(EventEditionParticipation participation) {
	EventEdition edition = participation.getEventEdition();
	participation.delete();
	edition.sweep();
    }

    public void run(JournalIssueParticipation participation) {
	JournalIssue issue = participation.getJournalIssue();
	participation.delete();
	issue.sweep();
    }

    public void run(CooperationParticipation participation) {
	Cooperation cooperation = participation.getCooperation();
	participation.delete();
	cooperation.sweet();
    }
}
