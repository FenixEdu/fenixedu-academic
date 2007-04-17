package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

public class CreateScientificJournal extends Service{

	public ScientificJournal run(String name, String issn, String publisher, ScopeType locationType)  {
		ScientificJournal journal = new ScientificJournal();
		journal.setName(name);
		journal.setLocationType(locationType);
		journal.setIssn(issn);
		journal.setPublisher(publisher);
		
		return journal;
	}
}
