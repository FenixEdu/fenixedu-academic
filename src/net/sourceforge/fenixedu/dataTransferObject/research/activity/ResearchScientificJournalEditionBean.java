package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

public class ResearchScientificJournalEditionBean extends ResearchActivityEditionBean implements Serializable {
	private DomainReference<ScientificJournal> journal;
    
    public ResearchScientificJournalEditionBean() {
    	setScientificJournal(null);
    }
    
    public ScientificJournal getScientificJournal() {
		return journal.getObject();
	}

	public void setScientificJournal(ScientificJournal journal) {
		this.journal = new DomainReference<ScientificJournal>(journal);
	} 
}
