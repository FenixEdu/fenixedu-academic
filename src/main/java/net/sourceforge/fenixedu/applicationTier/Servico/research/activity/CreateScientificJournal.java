package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;


import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class CreateScientificJournal {

    @Checked("ResultPredicates.author")
    @Atomic
    public static ScientificJournal run(String name, String issn, String publisher, ScopeType locationType) {
        ScientificJournal journal = new ScientificJournal();
        journal.setName(name);
        journal.setLocationType(locationType);
        journal.setIssn(issn);
        journal.setPublisher(publisher);

        return journal;
    }
}