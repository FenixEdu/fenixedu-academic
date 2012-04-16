package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreatePrize extends FenixService {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(MultiLanguageString name, MultiLanguageString description, Integer year, Person person) {
	new Prize(name, description, year, person);
    }
}