package net.sourceforge.fenixedu.applicationTier.Servico.person;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class GetHomepage {

    @Atomic
    public static Homepage run(Person person, boolean create) {
        check(RolePredicates.PERSON_PREDICATE);
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