package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;


public class ScientificJournal extends ScientificJournal_Base {
    
    public  ScientificJournal() {
        super();
        setOjbConcreteClass(getClass().getName());
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
}
