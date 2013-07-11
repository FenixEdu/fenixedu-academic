package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreatePrize {

    @Checked("ResultPredicates.author")
    @Atomic
    public static void run(MultiLanguageString name, MultiLanguageString description, Integer year, Person person) {
        new Prize(name, description, year, person);
    }
}