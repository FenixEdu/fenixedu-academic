package net.sourceforge.fenixedu.applicationTier.Servico.person;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ChangePassword {

    @Atomic
    public static void run(User userView, String oldPassword, String newPassword) throws Exception {
        check(RolePredicates.PERSON_PREDICATE);
        Person person = Person.readPersonByUsername(userView.getUsername());
        try {
            person.changePassword(oldPassword, newPassword);
        } catch (DomainException e) {
            throw new InvalidPasswordServiceException(e.getKey());
        }
    }
}