package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;


public class ScientificJournal extends ScientificJournal_Base {
    
    public  ScientificJournal() {
        super();
        setOjbConcreteClass(getClass().getName());
    }
    
    public ScientificJournal(MultiLanguageString name, ResearchActivityLocationType type) {
	this();
	this.setName(name);
	this.setLocationType(type);
    }

    /**
     * This method is responsible for deleting the object and all its references, particularly
     * Participations
     */
    public void delete(){
        super.delete();
    }
    
    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles(){
    	return ResearchActivityParticipationRole.getAllScientificJournalParticipationRoles();
    }

    @Override
    public void removeJournalIssues(JournalIssue journalIssues) {
	super.removeJournalIssues(journalIssues);
	if(getJournalIssues().isEmpty() && getParticipations().isEmpty()) {
	    delete();
	}
    }
    
    public String getNameAsString() {
	return getName().getContent();
    }
    
    
}
