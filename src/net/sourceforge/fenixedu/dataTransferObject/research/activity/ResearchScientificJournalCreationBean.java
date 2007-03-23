package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ResearchScientificJournalCreationBean implements Serializable {
    private DomainReference<ScientificJournal> scientificJournal;
    private String userInput;
    private MultiLanguageString scientificJournalName;
    private ResearchActivityParticipationRole role;
    private ResearchActivityLocationType locationType;
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

    public MultiLanguageString getScientificJournalName() {
        return scientificJournalName;
    }
    
    public void setScientificJournalName(MultiLanguageString scientificJournalName) {
        this.scientificJournalName = scientificJournalName;
    }
    
    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
    	this.userInput = userInput;
        this.scientificJournalName = new MultiLanguageString(this.userInput);
    }

    public ScientificJournal getScientificJournal() {
        return this.scientificJournal.getObject();
    }

    public void setScientificJournal(ScientificJournal scientificJournal) {
        this.scientificJournal = new DomainReference<ScientificJournal>(scientificJournal);
    }
    
    public ResearchActivityLocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(ResearchActivityLocationType locationType) {
        this.locationType = locationType;
    }
    
}
