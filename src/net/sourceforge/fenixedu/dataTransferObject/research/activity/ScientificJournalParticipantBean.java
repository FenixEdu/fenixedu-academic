package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class ScientificJournalParticipantBean extends ParticipantBean implements Serializable {

    DomainReference<ScientificJournal> scientificJournal;
    
    public ScientificJournalParticipantBean() {
	super();
	setScientificJournal(null);
    }
    
    public ScientificJournal getScientificJournal() {
	return this.scientificJournal.getObject();
    }
    
    public void setScientificJournal(ScientificJournal journal) {
	this.scientificJournal = new DomainReference<ScientificJournal>(journal);
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
