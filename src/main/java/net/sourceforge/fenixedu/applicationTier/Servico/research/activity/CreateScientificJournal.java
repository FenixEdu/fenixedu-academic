package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;


import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateScientificJournal {

    @Atomic
    public static ScientificJournal run(String name, String issn, String publisher, ScopeType locationType) {
        check(ResultPredicates.author);
        ScientificJournal journal = new ScientificJournal();
        journal.setName(name);
        journal.setLocationType(locationType);
        journal.setIssn(issn);
        journal.setPublisher(publisher);

        return journal;
    }
}