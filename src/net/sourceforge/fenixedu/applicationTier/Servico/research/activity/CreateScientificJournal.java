package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

public class CreateScientificJournal extends Service{

	public ScientificJournal run(String name, String issn, ResearchActivityLocationType locationType)  {
		ScientificJournal journal = new ScientificJournal();
		journal.setName(name);
		journal.setLocationType(locationType);
		journal.setIssn(issn);
		
		return journal;
	}
}
