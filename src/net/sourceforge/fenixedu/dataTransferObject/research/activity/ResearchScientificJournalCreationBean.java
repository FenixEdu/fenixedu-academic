package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;

import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

public class ResearchScientificJournalCreationBean implements Serializable {
    private DomainReference<ScientificJournal> scientificJournal;
    private String scientificJournalName;
    private ResearchActivityParticipationRole role;
    private ScopeType locationType;
    private String issn;
    
    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public ResearchScientificJournalCreationBean() {
    	setScientificJournal(null);
    }
    
	public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole participationRole) {
        this.role = participationRole;
    }

    public String getScientificJournalName() {
        return scientificJournalName;
    }
    
    public void setScientificJournalName(String scientificJournalName) {
        this.scientificJournalName = scientificJournalName;
    }
    

    public ScientificJournal getScientificJournal() {
        return this.scientificJournal.getObject();
    }

    public void setScientificJournal(ScientificJournal scientificJournal) {
        this.scientificJournal = new DomainReference<ScientificJournal>(scientificJournal);
    }
    
    public ScopeType getLocationType() {
        return locationType;
    }

    public void setLocationType(ScopeType locationType) {
        this.locationType = locationType;
    }
    
}
