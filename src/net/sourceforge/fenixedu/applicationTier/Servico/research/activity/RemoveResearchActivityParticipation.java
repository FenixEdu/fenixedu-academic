package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveResearchActivityParticipation extends FenixService {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(EventParticipation participation) {
	ResearchEvent event = participation.getEvent();
	participation.delete();
	event.sweep();
    }

    @Checked("ResultPredicates.author")
    @Service
    public static void run(ScientificJournalParticipation participation) {
	ScientificJournal journal = participation.getScientificJournal();
	participation.delete();
	journal.sweep();
    }

    @Checked("ResultPredicates.author")
    @Service
    public static void run(EventEditionParticipation participation) {
	EventEdition edition = participation.getEventEdition();
	participation.delete();
	edition.sweep();
    }

    @Checked("ResultPredicates.author")
    @Service
    public static void run(JournalIssueParticipation participation) {
	JournalIssue issue = participation.getJournalIssue();
	participation.delete();
	issue.sweep();
    }

    @Checked("ResultPredicates.author")
    @Service
    public static void run(CooperationParticipation participation) {
	Cooperation cooperation = participation.getCooperation();
	participation.delete();
	cooperation.sweet();
    }

    public static void run(Participation participation) {
	if (participation instanceof EventParticipation) {
	    run((EventParticipation) participation);
	} else if (participation instanceof ScientificJournalParticipation) {
	    run((ScientificJournalParticipation) participation);
	} else if (participation instanceof EventEditionParticipation) {
	    run((EventEditionParticipation) participation);
	} else if (participation instanceof JournalIssueParticipation) {
	    run((JournalIssueParticipation) participation);
	} else if (participation instanceof CooperationParticipation) {
	    run((CooperationParticipation) participation);
	}
    }
}