package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import pt.ist.fenixframework.DomainObject;

public class ScientificJournalParticipantBean extends ParticipantBean implements Serializable {

    ScientificJournal scientificJournal;

    public ScientificJournalParticipantBean() {
        super();
        setScientificJournal(null);
    }

    public ScientificJournal getScientificJournal() {
        return this.scientificJournal;
    }

    public void setScientificJournal(ScientificJournal journal) {
        this.scientificJournal = journal;
    }

    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
        return ResearchActivityParticipationRole.getAllScientificJournalParticipationRoles();
    }

    @Override
    public DomainObject getActivity() {
        return getScientificJournal();
    }
}
