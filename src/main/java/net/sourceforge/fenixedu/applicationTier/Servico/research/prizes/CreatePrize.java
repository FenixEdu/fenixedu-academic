package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreatePrize {

    @Atomic
    public static void run(MultiLanguageString name, MultiLanguageString description, Integer year, Person person) {
        check(ResultPredicates.author);
        new Prize(name, description, year, person);
    }
}