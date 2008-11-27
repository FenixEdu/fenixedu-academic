package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

public class CreateScientificJournal extends FenixService {

    @Checked("RolePredicates.RESEARCHER_PREDICATE")
    @Service
    public static ScientificJournal run(String name, String issn, String publisher, ScopeType locationType) {
	ScientificJournal journal = new ScientificJournal();
	journal.setName(name);
	journal.setLocationType(locationType);
	journal.setIssn(issn);
	journal.setPublisher(publisher);

	return journal;
    }
}