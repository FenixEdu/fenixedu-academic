package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class GetHomepage extends FenixService {

    @Checked("RolePredicates.PERSON_PREDICATE")
    @Service
    public static Homepage run(Person person, boolean create) {
        Homepage homepage = person.getHomepage();

        if (homepage != null) {
            return homepage;
        } else if (create) {
            homepage = person.initializeSite();
            homepage.setActivated(false);

            return homepage;
        } else {
            return null;
        }
    }
}