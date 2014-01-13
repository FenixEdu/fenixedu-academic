package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
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
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;

public class RemoveResearchActivityParticipation {

    @Atomic
    public static void run(EventParticipation participation) {
        check(ResultPredicates.author);
        ResearchEvent event = participation.getEvent();
        participation.delete();
        event.sweep();
    }

    @Atomic
    public static void run(ScientificJournalParticipation participation) {
        check(ResultPredicates.author);
        ScientificJournal journal = participation.getScientificJournal();
        participation.delete();
        journal.sweep();
    }

    @Atomic
    public static void run(EventEditionParticipation participation) {
        check(ResultPredicates.author);
        EventEdition edition = participation.getEventEdition();
        participation.delete();
        edition.sweep();
    }

    @Atomic
    public static void run(JournalIssueParticipation participation) {
        check(ResultPredicates.author);
        JournalIssue issue = participation.getJournalIssue();
        participation.delete();
        issue.sweep();
    }

    @Atomic
    public static void run(CooperationParticipation participation) {
        check(ResultPredicates.author);
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