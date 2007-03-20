package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.researchActivity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean;
import net.sourceforge.fenixedu.dataTransferObject.MergeScientificJournalPageContainerBean;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

public class MergeScientificJournals extends Service {
    
    public void run(MergeScientificJournalPageContainerBean mergeScientificJournalPageContainerBean) {
	ScientificJournal scientificJournal = new ScientificJournal(mergeScientificJournalPageContainerBean.getName(), 
		mergeScientificJournalPageContainerBean.getResearchActivityLocationType());
	scientificJournal.setIssn(mergeScientificJournalPageContainerBean.getIssn());
	scientificJournal.setUrl(mergeScientificJournalPageContainerBean.getUrl());
	
	for (DomainObject domainObject : mergeScientificJournalPageContainerBean.getSelectedObjects()) {
	    ScientificJournal journal = (ScientificJournal) domainObject;
	    scientificJournal.getJournalIssuesSet().addAll(journal.getJournalIssuesSet());
	    scientificJournal.getParticipationsSet().addAll(journal.getParticipationsSet());
	    journal.delete();
	}
    }

}
