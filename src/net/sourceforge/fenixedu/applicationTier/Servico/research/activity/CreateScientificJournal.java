package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreateScientificJournal extends Service{

	public ScientificJournal run(MultiLanguageString name, ResearchActivityLocationType locationType)  {
		ScientificJournal journal = new ScientificJournal();
		journal.setName(name);
		journal.setLocationType(locationType);
		
		return journal;
	}
}
